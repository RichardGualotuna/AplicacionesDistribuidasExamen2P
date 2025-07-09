package espe.edu.ec.catalogo.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Configuration
@EnableScheduling
public class ResilienceConfig {

    private final Queue<String> localEventQueue = new ConcurrentLinkedQueue<>();
    private volatile boolean rabbitMQAvailable = true;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEventWithResilience(String queue, String message) {
        if (rabbitMQAvailable) {
            try {
                rabbitTemplate.convertAndSend(queue, message);
            } catch (Exception e) {
                System.err.println("RabbitMQ send failed, storing locally: " + e.getMessage());
                storeEventLocally(queue + ":" + message);
                rabbitMQAvailable = false;
            }
        } else {
            storeEventLocally(queue + ":" + message);
        }
    }

    private void storeEventLocally(String event) {
        localEventQueue.offer(event);
        System.out.println("Evento almacenado localmente: " + event);
    }

    @Scheduled(fixedRate = 10000)
    public void retryPendingEvents() {
        if (!rabbitMQAvailable) {
            try {
                rabbitTemplate.convertAndSend("test-queue", "test-message");
                rabbitMQAvailable = true;
                System.out.println("RabbitMQ connection restored, sending pending events");

                String event;
                while ((event = localEventQueue.poll()) != null) {
                    String[] parts = event.split(":", 2);
                    if (parts.length == 2) {
                        rabbitTemplate.convertAndSend(parts[0], parts[1]);
                        System.out.println("Evento reenviado: " + event);
                    }
                }
            } catch (Exception e) {
                System.out.println("RabbitMQ still unavailable, retrying...");
            }
        }
    }
}