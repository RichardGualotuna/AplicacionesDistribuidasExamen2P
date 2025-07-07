package ec.edu.ec.ms_sincronizacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoraClienteDto {
    private String nombreNodo;
    private long horaEnviada;
}
