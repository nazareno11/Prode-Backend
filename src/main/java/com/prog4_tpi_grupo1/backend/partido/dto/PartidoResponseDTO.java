package com.prog4_tpi_grupo1.backend.partido.dto;

import com.prog4_tpi_grupo1.backend.partido.entity.EstadoPartido;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PartidoResponseDTO {

    private Long id;

    private Long externalId;

    private LocalDateTime fechaHora;

    private EstadoPartido estado;

    private Integer matchday;

    private String grupo;

    private String stage;

    private String equipoLocal;

    private String equipoVisitante;

    private Integer resultadoLocal;

    private Integer resultadoVisitante;

    private String fecha;
}
