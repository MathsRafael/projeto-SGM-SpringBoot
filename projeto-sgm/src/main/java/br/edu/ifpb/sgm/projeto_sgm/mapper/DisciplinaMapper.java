package br.edu.ifpb.sgm.projeto_sgm.mapper;

import br.edu.ifpb.sgm.projeto_sgm.dto.DisciplinaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.DisciplinaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Disciplina;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Mapper(componentModel = "spring",
        uses = {CursoMapper.class, ProfessorMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DisciplinaMapper {

    Disciplina toEntity(DisciplinaRequestDTO disciplinaRequestDTO);

    @Mapping(source = "curso", target = "cursoResponseDTO")
    @Mapping(source = "professor", target = "professorResponseDTO")
    DisciplinaResponseDTO toResponseDTO(Disciplina disciplina);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDisciplinaFromDto(DisciplinaRequestDTO dto, @MappingTarget Disciplina entity);


}
