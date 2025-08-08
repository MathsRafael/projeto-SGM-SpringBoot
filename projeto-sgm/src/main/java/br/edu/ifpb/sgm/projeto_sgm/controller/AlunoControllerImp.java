package br.edu.ifpb.sgm.projeto_sgm.controller;

import br.edu.ifpb.sgm.projeto_sgm.dto.AlunoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.AlunoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MinhaInscricaoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.service.AlunoServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoControllerImp {

    @Autowired
    private AlunoServiceImp alunoService;

    @PostMapping
    public ResponseEntity<AlunoResponseDTO> criar(@RequestBody AlunoRequestDTO dto) {

        return alunoService.salvar(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarPorId(@PathVariable Long id) {
        return alunoService.buscarPorId(id);
    }

    @GetMapping("/cadastros")
    public ResponseEntity<List<AlunoResponseDTO>> listarTodos() {
        return alunoService.listarTodos();
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listarTodosCadastrados() {
        return alunoService.listarTodosCadastrados();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> atualizar(@PathVariable Long id, @RequestBody AlunoRequestDTO dto) {

        return alunoService.atualizar(id, dto);
    }

    @PutMapping("/associar/{id}")
    public ResponseEntity<AlunoResponseDTO> associar(@PathVariable Long id, @RequestBody AlunoRequestDTO dto) {
        return alunoService.associar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return alunoService.deletar(id);
    }

    @GetMapping("/minhas-inscricoes")
    public ResponseEntity<List<MinhaInscricaoResponseDTO>> getMinhasInscricoes(Authentication authentication) {
        String matricula = authentication.getName();
        List<MinhaInscricaoResponseDTO> inscricoes = alunoService.findInscricoesByAlunoMatricula(matricula);
        return ResponseEntity.ok(inscricoes);
    }

}
