package br.edu.ifpb.sgm.projeto_sgm.service;

import br.edu.ifpb.sgm.projeto_sgm.dto.AlunoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProfessorRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.*;
import br.edu.ifpb.sgm.projeto_sgm.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static br.edu.ifpb.sgm.projeto_sgm.util.Constants.*;

@Service
public class TestService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private AlunoServiceImp alunoServiceImp;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private ProfessorServiceImp professorServiceImp;

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private ProcessoSeletivoRepository processoSeletivoRepository;

    @Autowired
    private MonitoriaRepository monitoriaRepository;

    @Autowired
    private AtividadeRepository atividadeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @PostConstruct
    private void initRoles() {
        if (roleRepository.findByRole("ROLE_" + ADMIN).isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_" + ADMIN));
        }
        if (roleRepository.findByRole("ROLE_" + COORDENADOR).isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_" + COORDENADOR));
        }
        if (roleRepository.findByRole("ROLE_" + DOCENTE).isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_" + DOCENTE));
        }
        if (roleRepository.findByRole("ROLE_" + DISCENTE).isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_" + DISCENTE));
        }
    }

    private String encriptPassword(String senha) {
        return passwordEncoder.encode(senha);
    }

    @Transactional
    public void insertTestData() {
        Instituicao instituicao = new Instituicao();
        instituicao.setNome("Instituição Teste");
        instituicao.setCnpj("12.345.678/0001-99");
        instituicao.setEmail("contato@instituicaoteste.com");
        instituicaoRepository.save(instituicao);

        Role adminRole = roleRepository.findByRole("ROLE_" + ADMIN).orElseThrow();
        Role coordenadorRole = roleRepository.findByRole("ROLE_" + COORDENADOR).orElseThrow();
        Role docenteRole = roleRepository.findByRole("ROLE_" + DOCENTE).orElseThrow();
        Role discenteRole = roleRepository.findByRole("ROLE_" + DISCENTE).orElseThrow();

        Pessoa admin = new Pessoa();
        admin.setNome("Admin SGM");
        admin.setCpf("000.000.000-00");
        admin.setEmail("admin@sgm.com");
        admin.setEmailAcademico("admin.academico@sgm.com");
        admin.setMatricula("admin");
        admin.setSenha(encriptPassword("admin123"));
        admin.setInstituicao(instituicao);
        admin.setRoles(List.of(adminRole));
        pessoaRepository.save(admin);

        Pessoa pessoaCoordenador = new Pessoa();
        pessoaCoordenador.setNome("Coordenador Teste");
        pessoaCoordenador.setCpf("111.111.111-11");
        pessoaCoordenador.setEmail("coordenador@sgm.com");
        pessoaCoordenador.setEmailAcademico("coordenador.academico@sgm.com");
        pessoaCoordenador.setMatricula("coordenador");
        pessoaCoordenador.setSenha(encriptPassword("coord123"));
        pessoaCoordenador.setInstituicao(instituicao);
        pessoaCoordenador.setRoles(List.of(docenteRole, coordenadorRole));
        pessoaRepository.save(pessoaCoordenador);
        Professor professorCoordenador = new Professor();
        professorCoordenador.setPessoa(pessoaCoordenador);
        professorRepository.save(professorCoordenador);

        Pessoa pessoaProfessor = new Pessoa();
        pessoaProfessor.setNome("Professor Teste");
        pessoaProfessor.setEmail("professor@instituicaoteste.com");
        pessoaProfessor.setEmailAcademico("professorAcademico@instituicaoteste.com");
        pessoaProfessor.setCpf("123.456.789-00");
        pessoaProfessor.setMatricula("12374");
        pessoaProfessor.setSenha(encriptPassword("senha"));
        pessoaProfessor.setInstituicao(instituicao);
        pessoaProfessor.setRoles(List.of(docenteRole));
        pessoaRepository.save(pessoaProfessor);

        Professor professorApenasDocente = new Professor();
        professorApenasDocente.setPessoa(pessoaProfessor);
        professorRepository.save(professorApenasDocente);

        Curso curso = new Curso();
        curso.setNome("Curso Teste");
        curso.setDuracao(4);
        curso.setInstituicao(instituicao);
        curso.setNivel(NivelCurso.GRADUACAO);
        cursoRepository.save(curso);

        Disciplina disciplina = new Disciplina();
        disciplina.setNome("Disciplina Teste");
        disciplina.setCargaHoraria(60);
        disciplina.setCurso(curso);
        disciplina.setProfessor(professorCoordenador);
        disciplinaRepository.save(disciplina);

        AlunoRequestDTO alunoDTO = new AlunoRequestDTO();
        alunoDTO.setNome("Joca Teste");
        alunoDTO.setCpf("222.222.222-22");
        alunoDTO.setEmail("joca@gmail.com");
        alunoDTO.setEmailAcademico("joca.academico@gmail.com");
        alunoDTO.setMatricula("123456");
        alunoDTO.setSenha("senhaAluno");
        alunoDTO.setInstituicaoId(instituicao.getId());
        alunoServiceImp.salvar(alunoDTO);

        ProcessoSeletivo processoSeletivo = new ProcessoSeletivo();
        processoSeletivo.setInstituicao(instituicao);
        processoSeletivo.setNumero("PS001");
        processoSeletivo.setInicio(LocalDate.now());
        processoSeletivo.setFim(LocalDate.now().plusMonths(2));
        processoSeletivoRepository.save(processoSeletivo);

        Monitoria monitoria = new Monitoria();
        monitoria.setDisciplina(disciplina);
        monitoria.setProfessor(professorCoordenador);
        monitoria.setNumeroVaga(10);
        monitoria.setNumeroVagaBolsa(2);
        monitoria.setCargaHoraria(60);
        monitoria.setProcessoSeletivo(processoSeletivo);
        monitoriaRepository.save(monitoria);

        Atividade atividade = new Atividade();
        atividade.setDataHora(LocalDateTime.now());
        atividade.setDescricao("atividade teste");
        atividade.setMonitoria(monitoria);
        atividadeRepository.save(atividade);
    }
}