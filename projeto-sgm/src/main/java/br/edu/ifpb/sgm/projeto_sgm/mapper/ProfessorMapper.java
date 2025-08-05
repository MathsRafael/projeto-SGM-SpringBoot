package br.edu.ifpb.sgm.projeto_sgm.mapper;

import br.edu.ifpb.sgm.projeto_sgm.dto.ProfessorRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProfessorResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Professor;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Mapper(componentModel = "spring",
        uses = {InstituicaoMapper.class, PessoaMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProfessorMapper {

    @Mapping(target = "pessoa", ignore = true)
    Professor toEntity(ProfessorRequestDTO professorRequestDTO);

    //@Mapping(source = "disciplinas", target = "disciplinasResponseDTO")
    @Mapping(source = "cursos", target = "cursosResponseDTO")
    @Mapping(source = "pessoa.id", target = "id")
    @Mapping(source = "pessoa.cpf", target = "cpf")
    @Mapping(source = "pessoa.nome", target = "nome")
    @Mapping(source = "pessoa.email", target = "email")
    @Mapping(source = "pessoa.matricula", target = "matricula")
    @Mapping(source = "pessoa.emailAcademico", target = "emailAcademico")
    @Mapping(source = "pessoa.instituicao", target = "instituicaoResponseDTO")
    ProfessorResponseDTO toResponseDTO(Professor professor);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProfessorFromDto(ProfessorRequestDTO dto, @MappingTarget Professor entity);
}
