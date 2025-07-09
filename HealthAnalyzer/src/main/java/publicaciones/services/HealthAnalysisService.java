package publicaciones.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import publicaciones.dto.AlertEventDto;
import publicaciones.dto.VitalSignEventDto;
import publicaciones.entity.MedicalAlert;
import publicaciones.repository.MedicalAlertRepository;

import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Service
public class HealthAnalysisService {

    @Autowired
    private MedicalAlertRepository alertRepository;

    @Autowired
    private AlertProducerService alertProducerService;

    @Transactional
    public void analyzeVitalSign(VitalSignEventDto vitalSignEvent) {
        log.info("Analizando signo vital - Device: {}, Type: {}, Value: {}",
                vitalSignEvent.getDeviceId(), vitalSignEvent.getType(), vitalSignEvent.getValue());

        String alertType = null;
        float threshold = 0;
        boolean isAlert = false;

        // Aplicar reglas de negocio para detectar anomalías
        switch (vitalSignEvent.getType().toLowerCase()) {
            case "heart-rate":
                if (vitalSignEvent.getValue() > 140) {
                    alertType = "CriticalHeartRateAlert";
                    threshold = 140;
                    isAlert = true;
                } else if (vitalSignEvent.getValue() < 40) {
                    alertType = "CriticalHeartRateAlert";
                    threshold = 40;
                    isAlert = true;
                }
                break;

            case "oxygen":
                if (vitalSignEvent.getValue() < 90) {
                    alertType = "OxygenLevelCritical";
                    threshold = 90;
                    isAlert = true;
                }
                break;

            case "blood-pressure-systolic":
                if (vitalSignEvent.getValue() > 180) {
                    alertType = "HighBloodPressureAlert";
                    threshold = 180;
                    isAlert = true;
                }
                break;

            case "blood-pressure-diastolic":
                if (vitalSignEvent.getValue() > 120) {
                    alertType = "HighBloodPressureAlert";
                    threshold = 120;
                    isAlert = true;
                }
                break;

            default:
                log.warn("Tipo de signo vital no reconocido: {}", vitalSignEvent.getType());
        }

        if (isAlert) {
            generateAlert(vitalSignEvent, alertType, threshold);
        }
    }

    private void generateAlert(VitalSignEventDto vitalSign, String alertType, float threshold) {
        log.warn("Generando alerta {} para dispositivo {} - Valor: {}, Umbral: {}",
                alertType, vitalSign.getDeviceId(), vitalSign.getValue(), threshold);

        // Crear y guardar la alerta en la base de datos
        MedicalAlert alert = new MedicalAlert();
        alert.setAlertId(UUID.randomUUID());
        alert.setType(alertType);
        alert.setDeviceId(vitalSign.getDeviceId());
        alert.setValue(vitalSign.getValue());
        alert.setThreshold(threshold);
        alert.setTimestamp(ZonedDateTime.now());

        MedicalAlert savedAlert = alertRepository.save(alert);
        log.info("Alerta guardada con ID: {}", savedAlert.getAlertId());

        // Crear DTO para enviar evento
        AlertEventDto alertEvent = new AlertEventDto(
                savedAlert.getAlertId().toString(),
                alertType,
                savedAlert.getDeviceId(),
                savedAlert.getValue(),
                savedAlert.getThreshold(),
                savedAlert.getTimestamp()
        );

        // Enviar evento de alerta
        alertProducerService.sendAlert(alertEvent);
    }
}