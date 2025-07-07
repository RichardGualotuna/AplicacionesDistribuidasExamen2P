package publicaciones.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import publicaciones.dto.NotificacionDto;
import publicaciones.services.HealthAnalysisService;

@Component
public class NotificacionListener {

    @Autowired
    private HealthAnalysisService service;

    @Autowired
    private ObjectMapper mapper;

    @RabbitListener(queues = "NewVitalSignEvent")
    public void recibirMensajes(String mensajeJson) {
        try{
            NotificacionDto dto = mapper.readValue(mensajeJson, NotificacionDto.class);
            service.analyzeVitalSign(dto.getDeviceId(), dto.getType(), dto.getValue());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
