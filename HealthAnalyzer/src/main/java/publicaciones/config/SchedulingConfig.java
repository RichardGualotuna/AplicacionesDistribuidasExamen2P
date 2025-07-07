package publicaciones.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import publicaciones.services.RelojProducer;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Autowired
    private RelojProducer relojProducer;

    @Scheduled(fixedRate = 10000)
    public void reportarHora() {
        try {
            relojProducer.enviarHora();
            System.out.println("ms-publicaciones: reportando hora.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
