package espe.edu.ec.catalogo.controller;

import espe.edu.ec.catalogo.dto.VitalSignRequest;
import espe.edu.ec.catalogo.entity.VitalSign;
import espe.edu.ec.catalogo.service.VitalSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vital-signs")
public class PatientDataCollectorController {

    @Autowired
    private VitalSignService vitalSignService;

    @PostMapping
    public ResponseEntity<String> receiveVitalSign(@RequestBody VitalSignRequest request) {
        try {
            if (!isValidVitalSign(request)) {
                return ResponseEntity.badRequest().body("Datos inválidos");
            }

            VitalSign saved = vitalSignService.saveVitalSign(request);
            return ResponseEntity.ok("Signo vital registrado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<List<VitalSign>> getHistory(@PathVariable String deviceId) {
        List<VitalSign> signs = vitalSignService.getByDeviceId(deviceId);
        return ResponseEntity.ok(signs);
    }

    private boolean isValidVitalSign(VitalSignRequest request) {
        if (request.getDeviceId() == null || request.getType() == null || request.getValue() == null) {
            return false;
        }

        if ("heart-rate".equals(request.getType())) {
            return request.getValue() >= 30 && request.getValue() <= 200;
        }
        if ("oxygen".equals(request.getType())) {
            return request.getValue() >= 0 && request.getValue() <= 100;
        }
        return true;
    }
}