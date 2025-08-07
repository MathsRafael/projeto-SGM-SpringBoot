package br.edu.ifpb.sgm.projeto_sgm.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ProcessoSeletivoResponseDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private LocalDate dataInicioInscricoes;
    private LocalDate dataFimInscricoes;
    private String status;
    private InstituicaoResponseDTO instituicaoResponseDTO;
    private List<MonitoriaResponseDTO> monitorias;

}
