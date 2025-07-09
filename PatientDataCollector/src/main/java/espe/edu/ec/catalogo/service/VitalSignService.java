package espe.edu.ec.catalogo.service;

import espe.edu.ec.catalogo.dto.NewVitalSignEvent;
import espe.edu.ec.catalogo.dto.VitalSignRequest;
import espe.edu.ec.catalogo.entity.VitalSign;
import espe.edu.ec.catalogo.repository.VitalSignRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
public class VitalSignService {

    @Autowired
    private VitalSignRepository vitalSignRepository;

    @Autowired
    private ResilienceService resilienceService;

    @Transactional
    public VitalSign saveVitalSign(VitalSignRequest request) {
        log.info("Procesando signo vital - Device: {}, Type: {}, Value: {}",
                request.getDeviceId(), request.getType(), request.getValue());

        validateVitalSign(request);

        VitalSign vitalSign = new VitalSign();
        vitalSign.setDeviceId(request.getDeviceId());
        vitalSign.setType(request.getType());
        vitalSign.setValue(request.getValue());
        vitalSign.setTimestamp(parseTimestamp(request.getTimestamp()));

        VitalSign saved = vitalSignRepository.save(vitalSign);
        log.info("Signo vital guardado con ID: {}", saved.getId());

        // Enviar evento con resiliencia
        NewVitalSignEvent event = new NewVitalSignEvent(
                saved.getEventId(),
                saved.getDeviceId(),
                saved.getType(),
                saved.getValue(),
                saved.getTimestamp()
        );

        resilienceService.sendEventWithResilience("NewVitalSignEvent", event);

        return saved;
    }

    private void validateVitalSign(VitalSignRequest request) {
        if (request.getDeviceId() == null || request.getDeviceId().trim().isEmpty()) {
            throw new IllegalArgumentException("Device ID es requerido");
        }

        if (request.getType() == null || request.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de signo vital es requerido");
        }

        if (request.getValue() == null) {
            throw new IllegalArgumentException("Valor es requerido");
        }

        // Validaciones específicas por tipo
        switch (request.getType().toLowerCase()) {
            case "heart-rate":
                if (request.getValue() < 30 || request.getValue() > 200) {
                    throw new IllegalArgumentException("Frecuencia cardíaca fuera de rango válido (30-200 bpm)");
                }
                break;
            case "oxygen":
                if (request.getValue() < 0 || request.getValue() > 100) {
                    throw new IllegalArgumentException("Nivel de oxígeno fuera de rango válido (0-100%)");
                }
                break;
            case "blood-pressure-systolic":
                if (request.getValue() < 50 || request.getValue() > 250) {
                    throw new IllegalArgumentException("Presión sistólica fuera de rango válido (50-250 mmHg)");
                }
                break;
            case "blood-pressure-diastolic":
                if (request.getValue() < 30 || request.getValue() > 150) {
                    throw new IllegalArgumentException("Presión diastólica fuera de rango válido (30-150 mmHg)");
                }
                break;
            default:
                log.warn("Tipo de signo vital no reconocido: {}", request.getType());
        }
    }

    private ZonedDateTime parseTimestamp(String timestamp) {
        if (timestamp == null || timestamp.isEmpty()) {
            return ZonedDateTime.now();
        }
        try {
            return ZonedDateTime.parse(timestamp);
        } catch (Exception e) {
            log.warn("Error parseando timestamp '{}', usando tiempo actual", timestamp);
            return ZonedDateTime.now();
        }
    }

    public List<VitalSign> getByDeviceId(String deviceId) {
        log.debug("Obteniendo historial para device: {}", deviceId);
        return vitalSignRepository.findByDeviceId(deviceId);
    }

    public List<VitalSign> getAllVitalSigns() {
        return vitalSignRepository.findAll();
    }
}