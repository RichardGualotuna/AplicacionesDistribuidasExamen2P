package espe.edu.ec.catalogo.controller;

import espe.edu.ec.catalogo.entity.VitalSign;
import espe.edu.ec.catalogo.service.NotificacionProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vital-signs")
public class CatalogController {

    @Autowired
    private NotificacionProducer service;

    @GetMapping
    public List<VitalSign> listarTodas() {
        return service.getAll();
    }
}
