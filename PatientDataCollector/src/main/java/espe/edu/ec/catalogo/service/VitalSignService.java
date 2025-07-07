package espe.edu.ec.catalogo.service;

import espe.edu.ec.catalogo.entity.VitalSign;
import espe.edu.ec.catalogo.repository.VitalSignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VitalSignService {

    @Autowired
    private VitalSignRepository vitalSignRepository;

    @Autowired
    private NotificacionProducer notificacionProducer;

    public VitalSign saveVitalSign(VitalSign vitalSign) {
        // Validación de rango
        if ("heart-rate".equalsIgnoreCase(vitalSign.getType())) {
            if (vitalSign.getValue() < 30 || vitalSign.getValue() > 200) {
                throw new IllegalArgumentException("Heart rate fuera de rango");
            }
        }

        notificacionProducer.enviarNotificacion(
                "EVT-" + vitalSign.getDeviceId(),
                vitalSign.getDeviceId(),
                vitalSign.getType(),
                vitalSign.getValue(),
                vitalSign.getTimestamp()
        );

        return vitalSignRepository.save(vitalSign);
    }

    public List<VitalSign> getByDeviceId(String deviceId) {
        return vitalSignRepository.findByDeviceId(deviceId);
    }
}