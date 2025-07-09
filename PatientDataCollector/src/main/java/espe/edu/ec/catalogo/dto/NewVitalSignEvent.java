package espe.edu.ec.catalogo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewVitalSignEvent {
    private String eventId;
    private String deviceId;
    private String type;
    private float value;
    private ZonedDateTime timestamp;
}