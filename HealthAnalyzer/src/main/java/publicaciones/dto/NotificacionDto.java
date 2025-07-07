package publicaciones.dto;



import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class NotificacionDto {
	private String eventId;
	private String deviceId;
	private String type;
	private float value;
	private ZonedDateTime timestamp;

	public NotificacionDto(String eventId, String deviceId, String type, float value, ZonedDateTime timestamp) {
		this.eventId = eventId;
		this.deviceId = deviceId;
		this.type = type;
		this.value = value;
		this.timestamp = timestamp;
	}
}
