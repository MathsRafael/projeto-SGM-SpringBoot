package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.AlunoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.AlunoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.exception.DisciplinaNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.exception.AlunoNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.exception.InstituicaoNotFoundException;
import br.edu.ifpb.sgm.projeto_sgm.mapper.AlunoMapper;
import br.edu.ifpb.sgm.projeto_sgm.mapper.PessoaMapper;
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

import static br.edu.ifpb.sgm.projeto_sgm.util.Constants.DISCENTE;

@Service
@Transactional
public class AlunoServiceImp {

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Autowired
    private AlunoMapper alunoMapper;

    @Autowired
    private PessoaMapper pessoaMapper;


    public ResponseEntity<AlunoResponseDTO> salvar(AlunoRequestDTO alunoRequestDTO){
        String senhaCriptografada = passwordEncoder.encode(alunoRequestDTO.getSenha());
        alunoRequestDTO.setSenha(senhaCriptografada);
        Pessoa pessoa = pessoaMapper.fromPessoa(alunoRequestDTO);
        pessoa.setInstituicao(buscarInstituicao(alunoRequestDTO.getInstituicaoId()));

        Role alunoRole = roleRepository.findByRole("ROLE_" + DISCENTE)
                .orElseThrow(() -> new RuntimeException("ERRO CRÍTICO: Role DISCENTE não encontrada no banco!"));
        pessoa.setRoles(List.of(alunoRole));
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);
        Aluno aluno = new Aluno();
        aluno.setDisciplinasPagas(buscarDisciplinas(alunoRequestDTO.getDisciplinasPagasId()));
        aluno.setDisciplinaMonitoria(buscarDisciplinas(alunoRequestDTO.getDisciplinasMonitoriaId()));
        aluno.setPessoa(pessoaSalva);

        Aluno salvo = alunoRepository.save(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoMapper.toResponseDTO(salvo));
    }

    public ResponseEntity<AlunoResponseDTO> buscarPorId(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(AlunoNotFoundException::new);
        return ResponseEntity.ok(alunoMapper.toResponseDTO(aluno));
    }

    public ResponseEntity<List<AlunoResponseDTO>> listarTodos() {
        List<Aluno> alunos = alunoRepository.findAll();
        List<AlunoResponseDTO> dtos = alunos.stream()
                .map(alunoMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<List<AlunoResponseDTO>> listarTodosCadastrados() {
        List<Aluno> alunos = alunoRepository.findByCadastradoTrue();
        List<AlunoResponseDTO> dtos = alunos.stream()
                .map(alunoMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<AlunoResponseDTO> atualizar(Long id, AlunoRequestDTO dto) {

        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(AlunoNotFoundException::new);

        pessoaMapper.updatePessoaFromAlunoRequestDto(dto, aluno.getPessoa());
        alunoMapper.updateAlunoFromDto(dto, aluno);

        if (dto.getInstituicaoId() != null) {
            aluno.getPessoa().setInstituicao(buscarInstituicao(dto.getInstituicaoId()));
        }

        if (dto.getDisciplinasPagasId() != null) {
            aluno.setDisciplinasPagas(buscarDisciplinas(dto.getDisciplinasPagasId()));
        }
        if (dto.getDisciplinasMonitoriaId() != null) {
            aluno.setDisciplinaMonitoria(buscarDisciplinas(dto.getDisciplinasMonitoriaId()));
        }

        Aluno atualizado = alunoRepository.save(aluno);
        return ResponseEntity.ok(alunoMapper.toResponseDTO(atualizado));
    }

    public ResponseEntity<Void> deletar(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(AlunoNotFoundException::new);
        aluno.setCadastrado(false);
        aluno.setDisciplinaMonitoria(null);

        alunoRepository.save(aluno);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<AlunoResponseDTO> associar(Long id, AlunoRequestDTO alunoRequestDTO){
        Pessoa pessoa = pessoaRepository.findById(id).orElseThrow();

        Aluno aluno = new Aluno();

        aluno.setDisciplinasPagas(buscarDisciplinas(alunoRequestDTO.getDisciplinasPagasId()));
        aluno.setDisciplinaMonitoria(buscarDisciplinas(alunoRequestDTO.getDisciplinasMonitoriaId()));
        aluno.setPessoa(pessoa);

        Aluno salvo = alunoRepository.save(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoMapper.toResponseDTO(salvo));
    }

    private Set<Disciplina> buscarDisciplinas(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptySet();

        // Encontra todos os IDs inexistentes
        Set<Long> idsNaoEncontrados = ids.stream()
                .filter(id -> disciplinaRepository.findById(id).isEmpty())
                .collect(Collectors.toSet());

        if (!idsNaoEncontrados.isEmpty()) {
            throw new DisciplinaNotFoundException("IDs de disciplinas inválidos: " + idsNaoEncontrados);
        }

        // Busca segura, já que todos existem
        return ids.stream()
                .map(id -> disciplinaRepository.findById(id).get())
                .collect(Collectors.toSet());

    }

    private Instituicao buscarInstituicao(Long id) {
        return instituicaoRepository.findById(id)
                .orElseThrow(() -> new InstituicaoNotFoundException("Instituição com ID " + id + " não encontrada."));
    }
}
