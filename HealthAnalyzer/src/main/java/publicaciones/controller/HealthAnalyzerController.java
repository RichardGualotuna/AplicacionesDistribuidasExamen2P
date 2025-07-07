package publicaciones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import publicaciones.entity.MedicalAlert;
import publicaciones.repository.MedicalAlertRepository;
import java.util.List;


@RestController
@RequestMapping("/health-analyzer")

public class HealthAnalyzerController {

    @Autowired
    private MedicalAlertRepository medicalAlertRepository;

    @GetMapping
    public List<MedicalAlert> getAllAlerts() {
        return medicalAlertRepository.findAll();
    }

    @GetMapping("/device/{deviceId}")
    public List<MedicalAlert> getAlertsByDevice(@PathVariable String deviceId) {
        return medicalAlertRepository.findByDeviceId(deviceId);
    }
}