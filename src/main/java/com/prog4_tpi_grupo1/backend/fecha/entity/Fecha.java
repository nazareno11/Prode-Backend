package com.prog4_tpi_grupo1.backend.fecha.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prog4_tpi_grupo1.backend.partido.entity.Partido;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "fechas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fecha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private Integer matchday;

    private String grupo;

    @Enumerated(EnumType.STRING)
    private EstadoFecha estado;

    @OneToMany(mappedBy = "fecha")
    @JsonIgnore
    private List<Partido> partidos;
}