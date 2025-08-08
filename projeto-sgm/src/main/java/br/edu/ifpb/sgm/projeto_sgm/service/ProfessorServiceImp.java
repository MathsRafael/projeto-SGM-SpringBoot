package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.*;
import br.edu.ifpb.sgm.projeto_sgm.exception.*;
import br.edu.ifpb.sgm.projeto_sgm.mapper.MonitoriaMapper;
import br.edu.ifpb.sgm.projeto_sgm.mapper.PessoaMapper;
import br.edu.ifpb.sgm.projeto_sgm.mapper.ProfessorMapper;
import br.edu.ifpb.sgm.projeto_sgm.model.*;
import br.edu.ifpb.sgm.projeto_sgm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static br.edu.ifpb.sgm.projeto_sgm.util.Constants.COORDENADOR;
import static br.edu.ifpb.sgm.projeto_sgm.util.Constants.DOCENTE;

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

    @Autowired
    private MonitoriaMapper monitoriaMapper;

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public ResponseEntity<ProfessorResponseDTO> salvar(ProfessorRequestDTO dto) {
        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());

        Pessoa pessoa = pessoaMapper.fromPessoa(dto);
        pessoa.setSenha(senhaCriptografada);

        Role docenteRole = roleRepository.findByRole("ROLE_" + DOCENTE)
                .orElseThrow(() -> new RuntimeException("ERRO CRÍTICO: Role DOCENTE não encontrada no banco!"));

        pessoa.setRoles(List.of(docenteRole));

        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        Professor professor = new Professor();
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

        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor com ID " + id + " não encontrado."));

        pessoaMapper.updatePessoaFromProfessorRequestDto(dto, professor.getPessoa());
        professorMapper.updateProfessorFromDto(dto, professor);

        if (dto.getInstituicaoId() != null) {
            professor.getPessoa().setInstituicao(buscarInstituicao(dto.getInstituicaoId()));
        }
        if (dto.getDisciplinasId() != null) {
            professor.setDisciplinas(buscarDisciplinas(dto.getDisciplinasId()));
        }
        if (dto.getCursosId() != null) {
            professor.setCursos(buscarCursos(dto.getCursosId()));
        }
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

    public ResponseEntity<ProfessorResponseDTO> designarCoordenador(Long professorId, Long cursoId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor com ID " + professorId + " não encontrado."));

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new CursoNotFoundException("Curso com ID " + cursoId + " não encontrado."));

        Role coordenadorRole = roleRepository.findByRole("ROLE_" + COORDENADOR)
                .orElseThrow(() -> new RuntimeException("Role de Coordenador não encontrada no banco."));

        Pessoa pessoa = professor.getPessoa();
        if (!pessoa.getRoles().contains(coordenadorRole)) {
            pessoa.getRoles().add(coordenadorRole);
        }
        professor.getCursos().add(curso);

        Professor professorAtualizado = professorRepository.save(professor);

        return ResponseEntity.ok(professorMapper.toResponseDTO(professorAtualizado));
    }

    public ResponseEntity<Void> removerCargoCoordenador(Long professorId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor com ID " + professorId + " não encontrado."));
        Pessoa pessoa = professor.getPessoa();
        pessoa.getRoles().removeIf(role -> role.getRole().equals("ROLE_" + COORDENADOR));
        professor.getCursos().clear();
        professorRepository.save(professor);

        return ResponseEntity.noContent().build();
    }

    public List<MonitoriaResponseDTO> findMonitoriasByProfessor(String matricula) {
        List<Monitoria> monitorias = monitoriaRepository.findByProfessor_Pessoa_Matricula(matricula);
        return monitorias.stream()
                .map(monitoriaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
