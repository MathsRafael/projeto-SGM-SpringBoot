package br.edu.ifpb.sgm.projeto_sgm.mapper;

import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Monitoria;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Mapper(
        componentModel = "spring",
        uses = {DisciplinaMapper.class, ProfessorMapper.class, ProcessoSeletivoMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class MonitoriaMapper {

    public abstract Monitoria toEntity(MonitoriaRequestDTO monitoriaRequestDTO);

    @Mapping(source = "professor", target = "professorResponseDTO")
    //@Mapping(source = "processoSeletivo", target = "processoSeletivoResponseDTO")
    @Mapping(source = "disciplina", target = "disciplinaResponseDTO")
    @Mapping(source = "inscricoes", target = "monitoriaInscritosResponseDTO")
    public abstract MonitoriaResponseDTO toResponseDTO( Monitoria monitoria);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateMonitoriaFromDto( MonitoriaRequestDTO dto, @MappingTarget  Monitoria entity);
}
