package br.edu.ifpb.sgm.projeto_sgm.dto;

import br.edu.ifpb.sgm.projeto_sgm.model.Aluno;
import br.edu.ifpb.sgm.projeto_sgm.model.Monitoria;
import br.edu.ifpb.sgm.projeto_sgm.model.embeddable.MonitoriaInscritoId;
import lombok.Data;

@Data
public class MonitoriaInscritosResponseDTO {

    private MonitoriaInscritoId id;
    private AlunoResponseDTO alunoResponseDTO;
    private boolean selecionado;
}
