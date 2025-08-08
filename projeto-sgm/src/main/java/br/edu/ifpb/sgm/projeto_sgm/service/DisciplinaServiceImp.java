package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.DisciplinaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.DisciplinaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.exception.CursoNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.exception.DisciplinaNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.exception.ProfessorNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.mapper.DisciplinaMapper;
import br.edu.ifpb.sgm.projeto_sgm.model.Curso;
import br.edu.ifpb.sgm.projeto_sgm.model.Disciplina;
import br.edu.ifpb.sgm.projeto_sgm.model.Professor;
import br.edu.ifpb.sgm.projeto_sgm.repository.CursoRepository;
import br.edu.ifpb.sgm.projeto_sgm.repository.DisciplinaRepository;
import br.edu.ifpb.sgm.projeto_sgm.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DisciplinaServiceImp {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private DisciplinaMapper disciplinaMapper;

    public ResponseEntity<DisciplinaResponseDTO> salvar(DisciplinaRequestDTO dto) {
        Disciplina disciplina = disciplinaMapper.toEntity(dto);
        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        disciplina.setCurso(curso);
        Professor professor = professorRepository.findById(dto.getProfessorId())
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        disciplina.setProfessor(professor);
        Disciplina salva = disciplinaRepository.save(disciplina);
        return ResponseEntity.status(HttpStatus.CREATED).body(disciplinaMapper.toResponseDTO(salva));
    }

    public ResponseEntity<DisciplinaResponseDTO> buscarPorId(Long id) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina com ID " + id + " não encontrada."));
        return ResponseEntity.ok(disciplinaMapper.toResponseDTO(disciplina));
    }

    public ResponseEntity<List<DisciplinaResponseDTO>> listarTodos() {
        List<Disciplina> disciplinas = disciplinaRepository.findAll();
        List<DisciplinaResponseDTO> dtos = disciplinas.stream()
                .map(disciplinaMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<DisciplinaResponseDTO> atualizar(Long id, DisciplinaRequestDTO dto) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina com ID " + id + " não encontrada."));

        disciplinaMapper.updateDisciplinaFromDto(dto, disciplina);

        if (dto.getCursoId() != null) {
            disciplina.setCurso(buscarCurso(dto.getCursoId()));
        }

        Disciplina atualizada = disciplinaRepository.save(disciplina);
        return ResponseEntity.ok(disciplinaMapper.toResponseDTO(atualizada));
    }

    public ResponseEntity<Void> deletar(Long id) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina com ID " + id + " não encontrada."));
        disciplinaRepository.delete(disciplina);
        return ResponseEntity.noContent().build();
    }

    private Curso buscarCurso(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new CursoNotFoundException("Curso com ID " + id + " não encontrado."));
    }

    private Professor buscarProfessor(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor com ID " + id + " não encontrado."));
    }
}
