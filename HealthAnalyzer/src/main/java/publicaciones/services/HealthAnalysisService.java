package publicaciones.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import publicaciones.entity.MedicalAlert;
import publicaciones.repository.MedicalAlertRepository;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class HealthAnalysisService {

    @Autowired
    private MedicalAlertRepository alertRepository;

    public void analyzeVitalSign(String deviceId, String type, float value) {
        // Analizar y generar alerta si se cumplen condiciones
        if ("heart-rate".equalsIgnoreCase(type) && (value > 140 || value < 40)) {
            generateAlert(deviceId, "CriticalHeartRateAlert", value, value > 140 ? 140 : 40);
        } else if ("oxygen".equalsIgnoreCase(type) && value < 90) {
            generateAlert(deviceId, "OxygenLevelCritical", value, 90);
        }
        // Otras reglas según sea necesario
    }

    private void generateAlert(String deviceId, String alertType, float value, float threshold) {
        MedicalAlert alert = new MedicalAlert();
        alert.setAlertId(UUID.randomUUID());
        alert.setDeviceId(deviceId);
        alert.setType(alertType);
        alert.setValue(value);
        alert.setThreshold(threshold);
        alert.setTimestamp(ZonedDateTime.now());
        alertRepository.save(alert);

        // Aquí enviarías el evento de alerta a RabbitMQ
    }
}