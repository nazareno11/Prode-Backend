package com.prog4_tpi_grupo1.backend.grupo.dtos.response;

import lombok.Data;

@Data
public class GrupoResponseDTO {

    private Long id;

    private String nombre;

    private String codigoInvitacion;

    private String creador;

    private Integer cantidadMiembros;

}
