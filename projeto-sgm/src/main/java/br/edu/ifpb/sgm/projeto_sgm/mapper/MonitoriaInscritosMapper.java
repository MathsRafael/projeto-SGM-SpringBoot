package br.edu.ifpb.sgm.projeto_sgm.mapper;

import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaInscritosResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.MonitoriaInscritos;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Mapper(
        componentModel = "spring",
        uses = {AlunoMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class MonitoriaInscritosMapper {

    @Mapping(source = "aluno", target = "alunoResponseDTO")
    public abstract MonitoriaInscritosResponseDTO toResponseDTO(MonitoriaInscritos entity);
}
