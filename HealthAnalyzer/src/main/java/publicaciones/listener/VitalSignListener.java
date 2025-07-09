package publicaciones.listener;


import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import publicaciones.dto.VitalSignEventDto;
import publicaciones.services.HealthAnalysisService;

@Slf4j
@Component
public class VitalSignListener {

    @Autowired
    private HealthAnalysisService healthAnalysisService;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = "NewVitalSignEvent")
    public void receiveVitalSign(String messageJson) {
        try {
            log.debug("Recibiendo evento de signo vital: {}", messageJson);

            VitalSignEventDto vitalSignEvent = objectMapper.readValue(messageJson, VitalSignEventDto.class);

            healthAnalysisService.analyzeVitalSign(vitalSignEvent);

        } catch (Exception e) {
            log.error("Error procesando evento de signo vital: ", e);
        }
    }
}
