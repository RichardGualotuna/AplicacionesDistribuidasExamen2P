package espe.edu.ec.notificaciones.service;

import espe.edu.ec.notificaciones.entity.Notificacion;
import espe.edu.ec.notificaciones.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class NotificacionService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void sendNotification(String eventType, String recipient) {
        Notificacion notification = new Notificacion();
        notification.setNotificationId(UUID.randomUUID());
        notification.setEventType(eventType);
        notification.setRecipient(recipient);
        notification.setStatus("SENT"); // Simulación
        notification.setTimestamp(ZonedDateTime.now());

        notificationRepository.save(notification);

        // Simular envío: correo, SMS, push
        System.out.println("Notificación enviada a " + recipient + " - Evento: " + eventType);
    }
}
