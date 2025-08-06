package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.AlunoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.AlunoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProfessorRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProfessorResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.exception.AlunoNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.exception.DisciplinaNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.exception.InstituicaoNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.exception.ProfessorNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.mapper.PessoaMapper;
import br.edu.ifpb.sgm.projeto_sgm.mapper.ProfessorMapper;
import br.edu.ifpb.sgm.projeto_sgm.model.*;
import br.edu.ifpb.sgm.projeto_sgm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static br.edu.ifpb.sgm.projeto_sgm.util.Constants.COORDENADOR;

@Service
@Transactional
public class ProfessorServiceImp {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ProfessorMapper professorMapper;

    @Autowired
    private PessoaMapper pessoaMapper;

    public ResponseEntity<ProfessorResponseDTO> salvar(ProfessorRequestDTO dto) {
        Pessoa pessoa = pessoaMapper.fromPessoa(dto);
        pessoa.setInstituicao(buscarInstituicao(dto.getInstituicaoId()));
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        Professor professor = new Professor();
        professor.setDisciplinas(buscarDisciplinas(dto.getDisciplinasId()));
        professor.setCursos(buscarCursos(dto.getCursosId()));
        professor.setPessoa(pessoaSalva);

        Professor salvo = professorRepository.save(professor);

        return ResponseEntity.status(HttpStatus.CREATED).body(professorMapper.toResponseDTO(salvo));
    }

    public ResponseEntity<ProfessorResponseDTO> buscarPorId(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor com ID " + id + " não encontrado."));
        return ResponseEntity.ok(professorMapper.toResponseDTO(professor));
    }

    public ResponseEntity<List<ProfessorResponseDTO>> listarTodos() {
        List<Professor> professores = professorRepository.findAll();
        List<ProfessorResponseDTO> dtos = professores.stream()
                .map(professorMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<List<ProfessorResponseDTO>> listarTodosCadastrados() {
        List<Professor> professores = professorRepository.findByCadastradoTrue();
        List<ProfessorResponseDTO> dtos = professores.stream()
                .map(professorMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<ProfessorResponseDTO> atualizar(Long id, ProfessorRequestDTO dto) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(AlunoNotFoundException::new);

        Pessoa pesssoaAtualizada = pessoaMapper.fromPessoa(dto);

        if (dto.getInstituicaoId() != null) {
            pesssoaAtualizada.setInstituicao(buscarInstituicao(dto.getInstituicaoId()));
        }

        pessoaMapper.updatePessoaFromPessoa(pesssoaAtualizada, pessoa);

        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor com ID " + id + " não encontrado."));

        professorMapper.updateProfessorFromDto(dto, professor);

        if (dto.getDisciplinasId() != null) {
            professor.setDisciplinas(buscarDisciplinas(dto.getDisciplinasId()));
        }

        if (dto.getCursosId() != null) {
            professor.setCursos(buscarCursos(dto.getCursosId()));
        }

        Pessoa pessoaSalva = pessoaRepository.save(pessoa);
        professor.setPessoa(pessoaSalva);

        Professor atualizado = professorRepository.save(professor);
        return ResponseEntity.ok(professorMapper.toResponseDTO(atualizado));
    }

    public ResponseEntity<Void> deletar(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(ProfessorNotFoundException::new);
        professor.setCadastrado(false);
        professor.setCursos(null);

        professorRepository.save(professor);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<ProfessorResponseDTO> associar(Long id, ProfessorRequestDTO professorRequestDTO){
        Pessoa pessoa = pessoaRepository.findById(id).orElseThrow();

        Professor professor = new Professor();


        professor.setDisciplinas(buscarDisciplinas(professorRequestDTO.getDisciplinasId()));
        professor.setCursos(buscarCursos(professorRequestDTO.getCursosId()));

        professor.setPessoa(pessoa);

        Professor salvo = professorRepository.save(professor);
        return ResponseEntity.status(HttpStatus.CREATED).body(professorMapper.toResponseDTO(salvo));
    }

    // Auxiliar
    private List<Disciplina> buscarDisciplinas(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();

        List<Long> idsNaoEncontrados = ids.stream()
                .filter(id -> disciplinaRepository.findById(id).isEmpty())
                .toList();

        if (!idsNaoEncontrados.isEmpty()) {
            throw new DisciplinaNotFoundException("IDs de disciplinas inválidos: " + idsNaoEncontrados);
        }

        return ids.stream()
                .map(id -> disciplinaRepository.findById(id).get())
                .collect(Collectors.toList());
    }

    private Set<Curso> buscarCursos(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptySet();

        List<Long> idsNaoEncontrados = ids.stream()
                .filter(id -> cursoRepository.findById(id).isEmpty())
                .toList();


        if (!idsNaoEncontrados.isEmpty()) {
            throw new DisciplinaNotFoundException("IDs dos Cursos inválidos: " + idsNaoEncontrados);
        }

        return ids.stream()
                .map(id -> cursoRepository.findById(id).get())
                .collect(Collectors.toSet());
    }

    private Instituicao buscarInstituicao(Long id) {
        return instituicaoRepository.findById(id)
                .orElseThrow(() -> new InstituicaoNotFoundException("Instituição com ID " + id + " não encontrada."));
    }

    public ResponseEntity<List<ProfessorResponseDTO>> listarTodosCoordenadores() {
        List<Professor> coordenadores = professorRepository.findProfessoresByRoleName("ROLE_" + COORDENADOR);
        List<ProfessorResponseDTO> dtos = coordenadores.stream()
                .map(professorMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }
}
