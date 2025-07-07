package publicaciones.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter

@Table(name = "medical_alerts")
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

}