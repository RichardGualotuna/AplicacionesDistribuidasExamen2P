package publicaciones.config;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue newVitalSignEventQueue() {
        return QueueBuilder.durable("NewVitalSignEvent").build();
    }

    @Bean
    public Queue criticalHeartRateAlertQueue() {
        return QueueBuilder.durable("CriticalHeartRateAlert").build();
    }

    @Bean
    public Queue oxygenLevelCriticalQueue() {
        return QueueBuilder.durable("OxygenLevelCritical").build();
    }

    @Bean
    public Queue deviceOfflineAlertQueue() {
        return QueueBuilder.durable("DeviceOfflineAlert").build();
    }

    @Bean
    public Queue dailyReportGeneratedQueue() {
        return QueueBuilder.durable("DailyReportGenerated").build();
    }
}
