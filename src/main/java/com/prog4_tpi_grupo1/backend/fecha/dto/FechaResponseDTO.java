package com.prog4_tpi_grupo1.backend.fecha.dto;

import com.prog4_tpi_grupo1.backend.fecha.entity.EstadoFecha;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FechaResponseDTO {

    private Long id;

    private String nombre;

    private String grupo;

    private Integer matchday;

    private EstadoFecha estado;
}