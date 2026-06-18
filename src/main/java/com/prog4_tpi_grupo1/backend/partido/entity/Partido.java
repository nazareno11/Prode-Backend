package com.prog4_tpi_grupo1.backend.partido.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.prog4_tpi_grupo1.backend.equipo.entity.Equipo;
import com.prog4_tpi_grupo1.backend.fecha.entity.Fecha;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "partidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long externalId;

    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    private EstadoPartido estado;

    private Integer matchday;

    private String stage;

    private String grupo;

    @ManyToOne
    @JoinColumn(name = "equipo_local_id")
    private Equipo equipoLocal;

    @ManyToOne
    @JoinColumn(name = "equipo_visitante_id")
    private Equipo equipoVisitante;

    private Integer resultadoLocal;

    private Integer resultadoVisitante;

    @Enumerated(EnumType.STRING)
    private Tendencia tendencia;

    @ManyToOne
    @JoinColumn(name = "fecha_id")
    @JsonBackReference
    private Fecha fecha;
}