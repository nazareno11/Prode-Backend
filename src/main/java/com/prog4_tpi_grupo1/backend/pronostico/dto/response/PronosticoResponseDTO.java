package com.prog4_tpi_grupo1.backend.pronostico.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PronosticoResponseDTO {
    private Long id;
    private Long usuarioId;
    private String nombreUsuario;
    private Long partidoId;
    private String detallePartido; 
    private Integer golesLocal;
    private Integer golesVisitante;
    private LocalDateTime fechaRegistro;
}