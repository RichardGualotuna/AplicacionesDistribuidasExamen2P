package publicaciones.services;



import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import publicaciones.dto.AlertEventDto;

@Slf4j
@Service
public class AlertProducerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendAlert(AlertEventDto alertEvent) {
        try {
            String json = objectMapper.writeValueAsString(alertEvent);

            // Enviar a la cola específica según el tipo de alerta
            String queueName = getQueueNameForAlertType(alertEvent.getType());

            rabbitTemplate.convertAndSend(queueName, json);

            log.info("Alerta enviada a cola {}: {}", queueName, alertEvent);

        } catch (Exception e) {
            log.error("Error enviando alerta: ", e);
        }
    }

    private String getQueueNameForAlertType(String alertType) {
        return switch (alertType) {
            case "CriticalHeartRateAlert" -> "CriticalHeartRateAlert";
            case "OxygenLevelCritical" -> "OxygenLevelCritical";
            case "HighBloodPressureAlert" -> "HighBloodPressureAlert";
            case "DeviceOfflineAlert" -> "DeviceOfflineAlert";
            default -> "GeneralAlert";
        };
    }
}
