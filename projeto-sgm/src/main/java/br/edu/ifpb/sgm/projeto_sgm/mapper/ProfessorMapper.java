package br.edu.ifpb.sgm.projeto_sgm.mapper;

import br.edu.ifpb.sgm.projeto_sgm.dto.ProfessorRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProfessorResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Curso;
import br.edu.ifpb.sgm.projeto_sgm.model.Professor;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {PessoaMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProfessorMapper {

    @Mapping(target = "pessoa", ignore = true)
    Professor toEntity(ProfessorRequestDTO professorRequestDTO);

    @Mapping(source = "pessoa.id", target = "id")
    @Mapping(source = "pessoa.cpf", target = "cpf")
    @Mapping(source = "pessoa.nome", target = "nome")
    @Mapping(source = "pessoa.email", target = "email")
    @Mapping(source = "pessoa.matricula", target = "matricula")
    @Mapping(source = "pessoa.emailAcademico", target = "emailAcademico")
    ProfessorResponseDTO toResponseDTO(Professor professor);

    default Set<String> mapCursosToNomes(Set<Curso> cursos) {
        if (cursos == null || cursos.isEmpty()) {
            return null;
        }
        return cursos.stream()
                .map(Curso::getNome)
                .collect(Collectors.toSet());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProfessorFromDto(ProfessorRequestDTO dto, @MappingTarget Professor entity);
}
