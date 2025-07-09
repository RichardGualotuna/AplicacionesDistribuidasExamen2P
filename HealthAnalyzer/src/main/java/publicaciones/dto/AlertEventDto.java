package publicaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertEventDto {
    private String alertId;
    private String type;
    private String deviceId;
    private float value;
    private float threshold;
    private ZonedDateTime timestamp;
}
