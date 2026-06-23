package com.prog4_tpi_grupo1.backend.pronostico.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PronosticoRequestDTO {
    @NotNull(message = "El id del partido es obligatorio")
    private Long partidoId;

    @NotNull(message = "Los goles locales no pueden ser nulos")
    @Min(value = 0, message = "Los goles no pueden ser negativos")
    private Integer golesLocal;

    @NotNull(message = "Los goles visitantes no pueden ser nulos")
    @Min(value = 0, message = "Los goles no pueden ser negativos")
    private Integer golesVisitante;
}