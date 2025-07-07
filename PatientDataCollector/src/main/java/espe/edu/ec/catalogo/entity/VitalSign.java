package espe.edu.ec.catalogo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter

@Table(name = "vital_signs")
public class VitalSign {



    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private float value;

    @Column(nullable = false)
    private ZonedDateTime timestamp;

    // Getters and Setters
}