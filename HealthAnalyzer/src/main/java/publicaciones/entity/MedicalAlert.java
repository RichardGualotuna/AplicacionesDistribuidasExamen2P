package publicaciones.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "medical_alerts", indexes = {
        @Index(name = "idx_device_id", columnList = "device_id"),
        @Index(name = "idx_type", columnList = "type"),
        @Index(name = "idx_timestamp", columnList = "timestamp")
})
public class MedicalAlert {

    @Id
    @GeneratedValue
    private UUID alertId;

    @Column(nullable = false)
    private String type;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(nullable = false)
    private float value;

    @Column(nullable = false)
    private float threshold;

    @Column(nullable = false)
    private ZonedDateTime timestamp;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = ZonedDateTime.now();
        }
    }
}