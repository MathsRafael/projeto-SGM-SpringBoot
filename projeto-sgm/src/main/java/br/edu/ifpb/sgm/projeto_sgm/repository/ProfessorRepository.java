package br.edu.ifpb.sgm.projeto_sgm.repository;

import br.edu.ifpb.sgm.projeto_sgm.model.Aluno;
import br.edu.ifpb.sgm.projeto_sgm.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    List<Professor> findByCadastradoTrue();
    @Query("SELECT p FROM Professor p JOIN p.pessoa.roles r WHERE r.role = :roleName")
    List<Professor> findProfessoresByRoleName(@Param("roleName") String roleName);
}
