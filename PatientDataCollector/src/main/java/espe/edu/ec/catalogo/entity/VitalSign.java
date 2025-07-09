package espe.edu.ec.catalogo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "vital_signs", indexes = {
        @Index(name = "idx_device_id", columnList = "device_id"),
        @Index(name = "idx_timestamp", columnList = "timestamp")
})
public class VitalSign {

    @Id
    @GeneratedValue
    private UUID id;

    // NUEVO: Campo para event ID único
    @Column(name = "event_id", nullable = false, unique = true)
    private String eventId;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private float value;

    @Column(nullable = false)
    private ZonedDateTime timestamp;

    // NUEVO: Campo para auditoría
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = ZonedDateTime.now();
        }
        if (eventId == null) {
            eventId = "EVT-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
        }
    }
}