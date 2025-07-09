package espe.edu.ec.catalogo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import espe.edu.ec.catalogo.dto.NewVitalSignEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class NotificacionProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void enviarNotificacion(String eventId, String deviceId, String type, float value, ZonedDateTime timestamp){
        try {
            NewVitalSignEvent notificacionDto = new NewVitalSignEvent(eventId, deviceId, type, value, timestamp);
            String json = objectMapper.writeValueAsString(notificacionDto);

            rabbitTemplate.convertAndSend("NewVitalSignEvent", json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}