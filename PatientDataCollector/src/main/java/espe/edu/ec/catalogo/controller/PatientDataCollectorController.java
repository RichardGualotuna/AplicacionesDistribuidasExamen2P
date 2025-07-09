package espe.edu.ec.catalogo.controller;

import espe.edu.ec.catalogo.dto.VitalSignRequest;
import espe.edu.ec.catalogo.entity.VitalSign;
import espe.edu.ec.catalogo.service.VitalSignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/vital-signs")
public class PatientDataCollectorController {

    @Autowired
    private VitalSignService vitalSignService;

    @PostMapping
    public ResponseEntity<?> receiveVitalSign(@RequestBody VitalSignRequest request) {
        try {
            log.info("Recibiendo signo vital: {}", request);

            VitalSign saved = vitalSignService.saveVitalSign(request);

            return ResponseEntity.ok().body(Map.of(
                    "message", "Signo vital registrado exitosamente",
                    "eventId", saved.getEventId(),
                    "id", saved.getId()
            ));

        } catch (IllegalArgumentException e) {
            log.warn("Datos inválidos: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Datos inválidos",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error interno procesando signo vital: ", e);
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Error interno del servidor",
                    "message", "No se pudo procesar el signo vital"
            ));
        }
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<List<VitalSign>> getHistory(@PathVariable String deviceId) {
        try {
            List<VitalSign> signs = vitalSignService.getByDeviceId(deviceId);
            return ResponseEntity.ok(signs);
        } catch (Exception e) {
            log.error("Error obteniendo historial para device {}: ", deviceId, e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<VitalSign>> getAllVitalSigns() {
        try {
            List<VitalSign> signs = vitalSignService.getAllVitalSigns();
            return ResponseEntity.ok(signs);
        } catch (Exception e) {
            log.error("Error obteniendo todos los signos vitales: ", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "PatientDataCollector",
                "timestamp", java.time.ZonedDateTime.now()
        ));
    }
}