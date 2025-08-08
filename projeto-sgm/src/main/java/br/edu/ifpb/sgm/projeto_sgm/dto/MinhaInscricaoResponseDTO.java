package br.edu.ifpb.sgm.projeto_sgm.dto;

import lombok.Data;

@Data
public class MinhaInscricaoResponseDTO {
    private String nomeDisciplina;
    private String nomeProfessor;
    private String statusEdital;
    private boolean foiSelecionado;
}
