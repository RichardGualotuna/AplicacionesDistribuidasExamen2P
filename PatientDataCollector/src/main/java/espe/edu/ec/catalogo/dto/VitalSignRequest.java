package espe.edu.ec.catalogo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VitalSignRequest {
    private String deviceId;
    private String type;
    private Float value;
    private String timestamp;
}