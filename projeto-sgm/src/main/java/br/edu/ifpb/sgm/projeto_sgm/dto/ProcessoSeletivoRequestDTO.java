package br.edu.ifpb.sgm.projeto_sgm.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProcessoSeletivoRequestDTO {

    private String titulo;
    private String descricao;
    private LocalDate dataInicioInscricoes;
    private LocalDate dataFimInscricoes;
    private Long instituicaoId;
    private List<Long> monitoriasIds;
}
