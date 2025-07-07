package ec.edu.ec.ms_sincronizacion.service;

import ec.edu.ec.ms_sincronizacion.dto.HoraClienteDto;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SincronizacionService {
    private final Map<String, Long> tiemposClientes = new ConcurrentHashMap<>();
    private static final int INTERVALO_SEGUNDOS = 10;

    public void registrarTiempoCliente(HoraClienteDto dto) {
        tiemposClientes.put(dto.getNombreNodo(), dto.getHoraEnviada());
    }

    public void sincronizarRelojes() {
        if (tiemposClientes.size() >= 2) {
            long ahora = Instant.now().toEpochMilli();
            long promedio = (ahora + tiemposClientes.values().stream().mapToLong(Long::longValue).sum()) / (tiemposClientes.size() + 1);
            tiemposClientes.clear();
            enviarAjuste(promedio);
        }
    }

    public void enviarAjuste(long promedio) {
        System.out.println("Enviando ajuste a los nodos: " + new Date(promedio));
    }

}
