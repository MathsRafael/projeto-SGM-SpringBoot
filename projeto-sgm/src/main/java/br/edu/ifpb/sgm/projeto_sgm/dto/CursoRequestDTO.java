package br.edu.ifpb.sgm.projeto_sgm.dto;
import lombok.Data;

import java.util.List;

@Data
public class CursoRequestDTO {

    private String nome;
    private String nivelString;
    private int duracao;
    private Long instituicaoId;

}
