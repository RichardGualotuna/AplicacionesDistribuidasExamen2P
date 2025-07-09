package publicaciones.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import publicaciones.entity.MedicalAlert;
import publicaciones.repository.MedicalAlertRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/health-analyzer")
public class HealthAnalyzerController {

    @Autowired
    private MedicalAlertRepository medicalAlertRepository;

    @GetMapping("/alerts")
    public ResponseEntity<List<MedicalAlert>> getAllAlerts() {
        try {
            List<MedicalAlert> alerts = medicalAlertRepository.findAll();
            log.info("Obteniendo {} alertas médicas", alerts.size());
            return ResponseEntity.ok(alerts);
        } catch (Exception e) {
            log.error("Error obteniendo alertas: ", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/alerts/device/{deviceId}")
    public ResponseEntity<List<MedicalAlert>> getAlertsByDevice(@PathVariable String deviceId) {
        try {
            List<MedicalAlert> alerts = medicalAlertRepository.findByDeviceId(deviceId);
            log.info("Obteniendo {} alertas para dispositivo {}", alerts.size(), deviceId);
            return ResponseEntity.ok(alerts);
        } catch (Exception e) {
            log.error("Error obteniendo alertas para dispositivo {}: ", deviceId, e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/alerts/type/{alertType}")
    public ResponseEntity<List<MedicalAlert>> getAlertsByType(@PathVariable String alertType) {
        try {
            List<MedicalAlert> alerts = medicalAlertRepository.findByType(alertType);
            log.info("Obteniendo {} alertas de tipo {}", alerts.size(), alertType);
            return ResponseEntity.ok(alerts);
        } catch (Exception e) {
            log.error("Error obteniendo alertas de tipo {}: ", alertType, e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "HealthAnalyzer",
                "timestamp", ZonedDateTime.now()
        ));
    }
}