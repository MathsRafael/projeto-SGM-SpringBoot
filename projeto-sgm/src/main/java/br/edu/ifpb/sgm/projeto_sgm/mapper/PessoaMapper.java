package br.edu.ifpb.sgm.projeto_sgm.mapper;

import br.edu.ifpb.sgm.projeto_sgm.dto.*;
import br.edu.ifpb.sgm.projeto_sgm.model.Pessoa;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = { InstituicaoMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PessoaMapper {

    @Mapping(source = "cpf", target = "cpf")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "matricula", target = "matricula")
    @Mapping(source = "emailAcademico", target = "emailAcademico")
    @Mapping(target = "instituicao", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    Pessoa fromPessoa(AlunoRequestDTO alunoRequestDTO);

    @Mapping(source = "cpf", target = "cpf")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "matricula", target = "matricula")
    @Mapping(source = "emailAcademico", target = "emailAcademico")
    @Mapping(target = "instituicao", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    Pessoa fromPessoa(ProfessorRequestDTO professorRequestDTO);


    @Mapping(target = "authorities", ignore = true)
    Pessoa toEntity(PessoaRequestDTO pessoaRequestDTO);

    @Mapping(source = "instituicao", target = "instituicaoResponseDTO")
    PessoaResponseDTO toResponseDTO(Pessoa pessoa);

    @Mapping(target = "authorities", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePessoaFromPessoa(Pessoa pessoaAtualizada, @MappingTarget Pessoa entity);

    @Mapping(target = "authorities", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePessoaFromDto(PessoaRequestDTO pessoaRequestDTO, @MappingTarget Pessoa entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instituicao", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "aluno", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePessoaFromAlunoRequestDto(AlunoRequestDTO dto, @MappingTarget Pessoa entity);
}
