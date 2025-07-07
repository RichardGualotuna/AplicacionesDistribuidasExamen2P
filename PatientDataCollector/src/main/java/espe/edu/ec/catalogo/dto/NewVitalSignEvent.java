package espe.edu.ec.catalogo.dto;

import java.time.ZonedDateTime;

public class NewVitalSignEvent {

    private String eventId;
    private String deviceId;
    private String type;
    private float value;
    private ZonedDateTime timestamp;

    public NewVitalSignEvent(String eventId, String deviceId, String type, float value, ZonedDateTime timestamp) {
        this.eventId = eventId;
        this.deviceId = deviceId;
        this.type = type;
        this.value = value;
        this.timestamp = timestamp;
    }

}