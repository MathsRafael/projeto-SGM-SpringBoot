package br.edu.ifpb.sgm.projeto_sgm.repository;

import br.edu.ifpb.sgm.projeto_sgm.model.ProcessoSeletivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProcessoSeletivoRepository extends JpaRepository<ProcessoSeletivo, Long> {
//    List<ProcessoSeletivo> findByInstituicao_Id(Long instituicaoId);
//    List<ProcessoSeletivo> findByInicioBeforeAndFimAfter(LocalDate before, LocalDate after);
}
