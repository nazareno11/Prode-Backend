package com.prog4_tpi_grupo1.backend.equipo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equipos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long externalId;

    private String nombre;

    private String abreviatura;

    private String escudo;
}