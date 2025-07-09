package espe.edu.ec.catalogo.service;

import espe.edu.ec.catalogo.dto.VitalSignRequest;
import espe.edu.ec.catalogo.entity.VitalSign;
import espe.edu.ec.catalogo.repository.VitalSignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class VitalSignService {

    @Autowired
    private VitalSignRepository vitalSignRepository;

    @Autowired
    private NotificacionProducer notificacionProducer;

    public VitalSign saveVitalSign(VitalSignRequest request) {
        validateVitalSign(request);

        VitalSign vitalSign = new VitalSign();
        vitalSign.setDeviceId(request.getDeviceId());
        vitalSign.setType(request.getType());
        vitalSign.setValue(request.getValue());
        vitalSign.setTimestamp(parseTimestamp(request.getTimestamp()));

        VitalSign saved = vitalSignRepository.save(vitalSign);

        notificacionProducer.enviarNotificacion(
                "EVT-" + System.currentTimeMillis(),
                saved.getDeviceId(),
                saved.getType(),
                saved.getValue(),
                saved.getTimestamp()
        );

        return saved;
    }

    private void validateVitalSign(VitalSignRequest request) {
        if ("heart-rate".equalsIgnoreCase(request.getType())) {
            if (request.getValue() < 30 || request.getValue() > 200) {
                throw new IllegalArgumentException("Heart rate fuera de rango (30-200)");
            }
        } else if ("oxygen".equalsIgnoreCase(request.getType())) {
            if (request.getValue() < 0 || request.getValue() > 100) {
                throw new IllegalArgumentException("Oxígeno fuera de rango (0-100)");
            }
        }
    }

    private ZonedDateTime parseTimestamp(String timestamp) {
        if (timestamp == null || timestamp.isEmpty()) {
            return ZonedDateTime.now();
        }
        try {
            return ZonedDateTime.parse(timestamp);
        } catch (Exception e) {
            return ZonedDateTime.now();
        }
    }

    public List<VitalSign> getByDeviceId(String deviceId) {
        return vitalSignRepository.findByDeviceId(deviceId);
    }
}
