package br.edu.ifpb.sgm.projeto_sgm.mapper;

import br.edu.ifpb.sgm.projeto_sgm.dto.AlunoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.AlunoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Aluno;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = {DisciplinaMapper.class, InstituicaoMapper.class, PessoaMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AlunoMapper {

    @Mapping(target = "pessoa", ignore = true)
    Aluno toEntity(AlunoRequestDTO alunoRequestDTO);


    @Mapping(source = "disciplinasPagas", target = "disciplinasPagasResponseDTO")
    @Mapping(source = "disciplinaMonitoria", target = "disciplinasMonitoriaResponseDTO")
    @Mapping(source = "pessoa.id", target = "id")
    @Mapping(source = "pessoa.cpf", target = "cpf")
    @Mapping(source = "pessoa.nome", target = "nome")
    @Mapping(source = "pessoa.email", target = "email")
    @Mapping(source = "pessoa.matricula", target = "matricula")
    @Mapping(source = "cre", target = "cre")
    @Mapping(source = "pessoa.emailAcademico", target = "emailAcademico")
    @Mapping(source = "pessoa.instituicao", target = "instituicaoResponseDTO")
    AlunoResponseDTO toResponseDTO(Aluno aluno);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAlunoFromDto(AlunoRequestDTO dto, @MappingTarget Aluno entity);
}
