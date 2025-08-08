package br.edu.ifpb.sgm.projeto_sgm.dto;

import lombok.Data;
import java.util.List;
import java.util.Set;

@Data
public class ProfessorResponseDTO{

    private Long id;
    protected String cpf;
    protected String nome;
    protected String email;
    protected String emailAcademico;
    protected String matricula;
    //protected InstituicaoResponseDTO instituicaoResponseDTO;
    //private List<DisciplinaResponseDTO> disciplinasResponseDTO;
    private Set<String> cursosCoordenados;

}
