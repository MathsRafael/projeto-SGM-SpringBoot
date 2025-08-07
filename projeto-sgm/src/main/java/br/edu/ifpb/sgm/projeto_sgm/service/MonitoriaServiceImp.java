package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.exception.*;
import br.edu.ifpb.sgm.projeto_sgm.mapper.MonitoriaMapper;
import br.edu.ifpb.sgm.projeto_sgm.model.*;
import br.edu.ifpb.sgm.projeto_sgm.repository.*;
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
public class MonitoriaServiceImp {

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProcessoSeletivoRepository processoSeletivoRepository;

    @Autowired
    private MonitoriaInscricoesRepository monitoriaInscricoesRepository;


    @Autowired
    private MonitoriaMapper monitoriaMapper;

    public ResponseEntity<MonitoriaResponseDTO> salvar(MonitoriaRequestDTO dto) {
        Disciplina disciplina = disciplinaRepository.findById(dto.getDisciplinaId())
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina não encontrada"));

        Professor professor = disciplina.getProfessor();
        if (professor == null) {
            throw new RuntimeException("A disciplina selecionada não possui um professor responsável.");
        }

        ProcessoSeletivo processoSeletivo = processoSeletivoRepository.findById(dto.getProcessoSeletivoId())
                .orElseThrow(() -> new ProcessoSeletivoNotFoundException("Processo Seletivo não encontrado"));

        Monitoria novaMonitoria = monitoriaMapper.toEntity(dto);
        novaMonitoria.setDisciplina(disciplina);
        novaMonitoria.setProfessor(professor);
        novaMonitoria.setProcessoSeletivo(processoSeletivo);

        Monitoria monitoriaSalva = monitoriaRepository.save(novaMonitoria);
        return ResponseEntity.ok(monitoriaMapper.toResponseDTO(monitoriaSalva));
    }

    public ResponseEntity<MonitoriaResponseDTO> buscarPorId(Long id) {
        Monitoria monitoria = monitoriaRepository.findById(id)
                .orElseThrow(() -> new MonitoriaNotFoundException("Monitoria com ID " + id + " não encontrada."));
        return ResponseEntity.ok(monitoriaMapper.toResponseDTO(monitoria));
    }

    public ResponseEntity<List<MonitoriaResponseDTO>> listarTodos() {
        List<Monitoria> monitorias = monitoriaRepository.findAll();
        List<MonitoriaResponseDTO> dtos = monitorias.stream()
                .map(monitoriaMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<MonitoriaResponseDTO> atualizar(Long id, MonitoriaRequestDTO dto) {
        Monitoria monitoria = monitoriaRepository.findById(id)
                .orElseThrow(() -> new MonitoriaNotFoundException("Monitoria não encontrada"));

        Disciplina disciplina = disciplinaRepository.findById(dto.getDisciplinaId())
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina não encontrada"));

        Professor professor = disciplina.getProfessor();
        if (professor == null) {
            throw new RuntimeException("A disciplina selecionada não possui um professor responsável.");
        }

        ProcessoSeletivo processoSeletivo = processoSeletivoRepository.findById(dto.getProcessoSeletivoId())
                .orElseThrow(() -> new ProcessoSeletivoNotFoundException("Processo Seletivo não encontrado"));

        monitoria.setDisciplina(disciplina);
        monitoria.setProfessor(professor);
        monitoria.setProcessoSeletivo(processoSeletivo);
        monitoria.setNumeroVaga(dto.getNumeroVaga());
        monitoria.setCargaHoraria(dto.getCargaHoraria());

        Monitoria atualizada = monitoriaRepository.save(monitoria);
        return ResponseEntity.ok(monitoriaMapper.toResponseDTO(atualizada));
    }

    public ResponseEntity<Void> deletar(Long id) {
        Monitoria monitoria = monitoriaRepository.findById(id)
                .orElseThrow(() -> new MonitoriaNotFoundException("Monitoria com ID " + id + " não encontrada."));
        monitoriaRepository.delete(monitoria);
        return ResponseEntity.noContent().build();
    }

    // Métodos auxiliares

    private Disciplina buscarDisciplina(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina com ID " + id + " não encontrada."));
    }

    private Professor buscarProfessor(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor com ID " + id + " não encontrado."));
    }


    private ProcessoSeletivo buscarProcesso(Long id) {
        return processoSeletivoRepository.findById(id)
                .orElseThrow(() -> new ProcessoSeletivoNotFoundException("Processo seletivo com ID " + id + " não encontrado."));
    }

    private List<MonitoriaInscritos> buscarInscritosMonitoria(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();

        List<Long> idsNaoEncontrados = ids.stream()
                .filter(id -> monitoriaInscricoesRepository.findById(id).isEmpty())
                .toList();

        if (!idsNaoEncontrados.isEmpty()) {
            throw new MonitoriaNotFoundException("IDs de disciplinas inválidos: " + idsNaoEncontrados);
        }

        return ids.stream()
                .map(id -> monitoriaInscricoesRepository.findById(id).get())
                .collect(Collectors.toList());
    }
}
