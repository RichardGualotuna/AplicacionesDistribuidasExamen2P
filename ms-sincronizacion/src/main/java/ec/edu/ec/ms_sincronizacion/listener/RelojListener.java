package ec.edu.ec.ms_sincronizacion.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.ec.ms_sincronizacion.dto.HoraClienteDto;
import ec.edu.ec.ms_sincronizacion.service.SincronizacionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RelojListener {
    @Autowired
    private SincronizacionService sincronizacionService;

    @Autowired
    private ObjectMapper mapper;

    @RabbitListener(queues = "reloj.solicitud")
    public void recibirSolicitud(String mensajeJson) {
        try {
            HoraClienteDto dto = mapper.readValue(mensajeJson, HoraClienteDto.class);
            System.out.println("Recibo solicitud de reloj: " + dto);
            sincronizacionService.registrarTiempoCliente(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
