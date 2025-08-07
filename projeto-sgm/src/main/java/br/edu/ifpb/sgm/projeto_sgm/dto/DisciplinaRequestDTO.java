package br.edu.ifpb.sgm.projeto_sgm.dto;

import lombok.Data;

@Data
public class DisciplinaRequestDTO {

    private String nome;
    private int cargaHoraria;
    private Long cursoId;
    private Long professorId;

}
