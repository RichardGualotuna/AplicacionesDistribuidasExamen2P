package espe.edu.ec.catalogo.controller;
import java.util.List;
import espe.edu.ec.catalogo.entity.VitalSign;
import espe.edu.ec.catalogo.repository.VitalSignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vital-signs")
public class PatientDataCollectorController {

    @Autowired
    private VitalSignRepository vitalSignRepository;

    @PostMapping
    public ResponseEntity<VitalSign> receiveVitalSign(@RequestBody VitalSign vitalSign) {
        // Validaciones de rango podrían ir aquí también
        VitalSign saved = vitalSignRepository.save(vitalSign);
        // Emisión de evento NewVitalSignEvent se haría desde un servicio, no aquí directamente
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<List<VitalSign>> getHistory(@PathVariable String deviceId) {
        List<VitalSign> signs = vitalSignRepository.findByDeviceId(deviceId);
        return ResponseEntity.ok(signs);
    }
}