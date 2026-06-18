package com.prog4_tpi_grupo1.backend.grupo.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UnirseGrupoRequestDTO {

    @NotBlank(message = "El código de invitación es obligatorio")
    private String codigoInvitacion;

}
