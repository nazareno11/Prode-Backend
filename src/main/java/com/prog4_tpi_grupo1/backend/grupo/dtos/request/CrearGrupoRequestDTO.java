package com.prog4_tpi_grupo1.backend.grupo.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CrearGrupoRequestDTO {

    @NotBlank(message = "El nombre del grupo es obligatorio")
    private String nombre;

}