package ec.edu.ec.ms_sincronizacion.config;

import ec.edu.ec.ms_sincronizacion.service.SincronizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    private SincronizacionService sincronizacionService;

    @Scheduled(fixedRate = 10000)
    public void ejecutarSincronizacion() {
        System.out.println("Ejecutando sincronizacion...");
        sincronizacionService.sincronizarRelojes();
    }

}
