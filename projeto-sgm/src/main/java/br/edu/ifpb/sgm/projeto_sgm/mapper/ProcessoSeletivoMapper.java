package br.edu.ifpb.sgm.projeto_sgm.mapper;

import br.edu.ifpb.sgm.projeto_sgm.dto.ProcessoSeletivoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProcessoSeletivoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.ProcessoSeletivo;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Mapper(componentModel = "spring",
        uses = {InstituicaoMapper.class, MonitoriaMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProcessoSeletivoMapper {

    @Mapping(source = "instituicao", target = "instituicaoResponseDTO")
    ProcessoSeletivoResponseDTO toResponseDTO(ProcessoSeletivo processoSeletivo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instituicao", ignore = true)
    @Mapping(target = "monitorias", ignore = true)
    @Mapping(target = "status", ignore = true)
    ProcessoSeletivo toEntity(ProcessoSeletivoRequestDTO requestDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProcessoSeletivoFromDto(ProcessoSeletivoRequestDTO dto, @MappingTarget ProcessoSeletivo entity);
}
