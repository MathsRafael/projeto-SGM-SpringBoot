package br.edu.ifpb.sgm.projeto_sgm.dto;

import lombok.Data;

@Data
public class DisciplinaResponseDTO {

    private Long id;
    private String nome;
    private int cargaHoraria;
    private CursoResponseDTO cursoResponseDTO;
    private ProfessorResponseDTO professorResponseDTO;

}
