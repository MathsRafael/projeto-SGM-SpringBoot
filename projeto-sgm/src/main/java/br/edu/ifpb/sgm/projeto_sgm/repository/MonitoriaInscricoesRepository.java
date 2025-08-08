package br.edu.ifpb.sgm.projeto_sgm.repository;

import br.edu.ifpb.sgm.projeto_sgm.model.MonitoriaInscritos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonitoriaInscricoesRepository extends JpaRepository<MonitoriaInscritos, Long> {
    List<MonitoriaInscritos> findByAluno_Pessoa_Matricula(String matricula);
}
