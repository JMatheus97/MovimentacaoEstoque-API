package com.project.server.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoDeBarra;

    private String nome;

    private Integer quantidadeMinima;

    private Long saldoInicial;


}
