package espe.edu.ec.notificaciones.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import espe.edu.ec.notificaciones.dto.NotificacionDto;
import espe.edu.ec.notificaciones.service.NotificacionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificacionListener {

    @Autowired
    private NotificacionService service;

    @Autowired
    private ObjectMapper mapper;

    @RabbitListener(queues = "NewVitalSignEvent")
    public void recibirMensajes(String mensajeJson) {
        try{
            NotificacionDto dto = mapper.readValue(mensajeJson, NotificacionDto.class);
            service.sendNotification(dto.getType(), dto.getDeviceId());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
