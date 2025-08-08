package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.InstituicaoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.InstituicaoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.exception.CursoNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.exception.InstituicaoNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.exception.ProcessoSeletivoNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.mapper.InstituicaoMapper;
import br.edu.ifpb.sgm.projeto_sgm.model.Curso;
import br.edu.ifpb.sgm.projeto_sgm.model.Instituicao;
import br.edu.ifpb.sgm.projeto_sgm.model.ProcessoSeletivo;
import br.edu.ifpb.sgm.projeto_sgm.repository.CursoRepository;
import br.edu.ifpb.sgm.projeto_sgm.repository.InstituicaoRepository;
import br.edu.ifpb.sgm.projeto_sgm.repository.ProcessoSeletivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InstituicaoServiceImp {

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ProcessoSeletivoRepository processoSeletivoRepository;

    @Autowired
    private InstituicaoMapper instituicaoMapper;

    public ResponseEntity<InstituicaoResponseDTO> salvar(InstituicaoRequestDTO dto) {
        Instituicao instituicao = instituicaoMapper.toEntity(dto);
        Instituicao salva = instituicaoRepository.save(instituicao);
        return ResponseEntity.status(HttpStatus.CREATED).body(instituicaoMapper.toResponseDTO(salva));
    }

    public ResponseEntity<InstituicaoResponseDTO> buscarPorId(Long id) {
        Instituicao instituicao = instituicaoRepository.findById(id)
                .orElseThrow(() -> new InstituicaoNotFoundException("Instituição com ID " + id + " não encontrada."));
        return ResponseEntity.ok(instituicaoMapper.toResponseDTO(instituicao));
    }

    public ResponseEntity<List<InstituicaoResponseDTO>> listarTodos() {
        List<Instituicao> lista = instituicaoRepository.findAll();
        List<InstituicaoResponseDTO> dtos = lista.stream()
                .map(instituicaoMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<InstituicaoResponseDTO> atualizar(Long id, InstituicaoRequestDTO dto) {
        Instituicao instituicao = instituicaoRepository.findById(id)
                .orElseThrow(() -> new InstituicaoNotFoundException("Instituição com ID " + id + " não encontrada."));

        instituicaoMapper.updateInstituicaoFromDto(dto, instituicao);

        Instituicao atualizada = instituicaoRepository.save(instituicao);
        return ResponseEntity.ok(instituicaoMapper.toResponseDTO(atualizada));
    }

    public ResponseEntity<Void> deletar(Long id) {
        Instituicao instituicao = instituicaoRepository.findById(id)
                .orElseThrow(() -> new InstituicaoNotFoundException("Instituição com ID " + id + " não encontrada."));
        instituicaoRepository.delete(instituicao);
        return ResponseEntity.noContent().build();
    }

    private List<Curso> buscarCursos(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();

        List<Long> idsNaoEncontrados = ids.stream()
                .filter(id -> cursoRepository.findById(id).isEmpty())
                .toList();

        if (!idsNaoEncontrados.isEmpty()) {
            throw new CursoNotFoundException("IDs de cursos inválidos: " + idsNaoEncontrados);
        }

        return ids.stream()
                .map(id -> cursoRepository.findById(id).get())
                .collect(Collectors.toList());
    }

    private List<ProcessoSeletivo> buscarProcessos(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();

        List<Long> idsNaoEncontrados = ids.stream()
                .filter(id -> processoSeletivoRepository.findById(id).isEmpty())
                .toList();

        if (!idsNaoEncontrados.isEmpty()) {
            throw new ProcessoSeletivoNotFoundException("IDs dos processos seletivos inválidos: " + idsNaoEncontrados);
        }

        return ids.stream()
                .map(id -> processoSeletivoRepository.findById(id).get())
                .collect(Collectors.toList());
    }
}
