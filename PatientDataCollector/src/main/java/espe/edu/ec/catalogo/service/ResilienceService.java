package espe.edu.ec.catalogo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class ResilienceService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataSource dataSource;

    private final AtomicBoolean rabbitMQAvailable = new AtomicBoolean(true);
    private Connection sqliteConnection;

    public void initializeSQLite() {
        try {
            // Crear conexión SQLite para almacenamiento temporal
            sqliteConnection = DriverManager.getConnection("jdbc:sqlite:temp_events.db");
            createTempTable();
        } catch (SQLException e) {
            log.error("Error inicializando SQLite: ", e);
        }
    }

    private void createTempTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS temp_events (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                queue_name TEXT NOT NULL,
                message TEXT NOT NULL,
                created_at TEXT NOT NULL,
                retry_count INTEGER DEFAULT 0
            )
            """;
        try (Statement stmt = sqliteConnection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void sendEventWithResilience(String queueName, Object event) {
        if (rabbitMQAvailable.get()) {
            try {
                String json = objectMapper.writeValueAsString(event);
                rabbitTemplate.convertAndSend(queueName, json);
                log.debug("Evento enviado exitosamente a {}: {}", queueName, json);
            } catch (Exception e) {
                log.warn("RabbitMQ no disponible, almacenando localmente: {}", e.getMessage());
                rabbitMQAvailable.set(false);
                storeEventLocally(queueName, event);
            }
        } else {
            storeEventLocally(queueName, event);
        }
    }

    private void storeEventLocally(String queueName, Object event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            String sql = "INSERT INTO temp_events (queue_name, message, created_at) VALUES (?, ?, ?)";

            try (PreparedStatement pstmt = sqliteConnection.prepareStatement(sql)) {
                pstmt.setString(1, queueName);
                pstmt.setString(2, json);
                pstmt.setString(3, ZonedDateTime.now().toString());
                pstmt.executeUpdate();
                log.info("Evento almacenado localmente: {}", json);
            }
        } catch (Exception e) {
            log.error("Error almacenando evento localmente: ", e);
        }
    }

    @Scheduled(fixedRate = 10000) // Cada 10 segundos
    public void retryPendingEvents() {
        if (!rabbitMQAvailable.get()) {
            try {
                // Probar conexión con RabbitMQ
                rabbitTemplate.convertAndSend("test-queue", "test-message");
                rabbitMQAvailable.set(true);
                log.info("RabbitMQ connection restored, processing pending events");

                // Procesar eventos pendientes
                processPendingEvents();

            } catch (Exception e) {
                log.debug("RabbitMQ still unavailable: {}", e.getMessage());
            }
        }
    }

    private void processPendingEvents() {
        try {
            String selectSql = "SELECT id, queue_name, message, retry_count FROM temp_events ORDER BY id";
            List<PendingEvent> events = new ArrayList<>();

            try (Statement stmt = sqliteConnection.createStatement();
                 ResultSet rs = stmt.executeQuery(selectSql)) {

                while (rs.next()) {
                    events.add(new PendingEvent(
                            rs.getLong("id"),
                            rs.getString("queue_name"),
                            rs.getString("message"),
                            rs.getInt("retry_count")
                    ));
                }
            }

            for (PendingEvent event : events) {
                try {
                    rabbitTemplate.convertAndSend(event.queueName, event.message);
                    deleteLocalEvent(event.id);
                    log.info("Evento reenviado exitosamente: {}", event.message);
                } catch (Exception e) {
                    updateRetryCount(event.id, event.retryCount + 1);
                    if (event.retryCount >= 3) {
                        log.error("Evento descartado tras 3 intentos: {}", event.message);
                        deleteLocalEvent(event.id);
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error procesando eventos pendientes: ", e);
        }
    }

    private void deleteLocalEvent(long id) throws SQLException {
        String sql = "DELETE FROM temp_events WHERE id = ?";
        try (PreparedStatement pstmt = sqliteConnection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    private void updateRetryCount(long id, int retryCount) throws SQLException {
        String sql = "UPDATE temp_events SET retry_count = ? WHERE id = ?";
        try (PreparedStatement pstmt = sqliteConnection.prepareStatement(sql)) {
            pstmt.setInt(1, retryCount);
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
        }
    }

    private static class PendingEvent {
        final long id;
        final String queueName;
        final String message;
        final int retryCount;

        PendingEvent(long id, String queueName, String message, int retryCount) {
            this.id = id;
            this.queueName = queueName;
            this.message = message;
            this.retryCount = retryCount;
        }
    }
}