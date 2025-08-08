package br.edu.ifpb.sgm.projeto_sgm.controller;

import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.MonitoriaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.service.MonitoriaServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitorias")
public class MonitoriaControllerImp {

    @Autowired
    private MonitoriaServiceImp monitoriaService;

    @PostMapping
    public ResponseEntity<MonitoriaResponseDTO> criar(@RequestBody MonitoriaRequestDTO dto) {
        return monitoriaService.salvar(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonitoriaResponseDTO> buscarPorId(@PathVariable Long id) {
        return monitoriaService.buscarPorId(id);
    }

    @GetMapping
    public ResponseEntity<List<MonitoriaResponseDTO>> listarTodos() {
        return monitoriaService.listarTodos();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MonitoriaResponseDTO> atualizar(@PathVariable Long id,
                                                          @RequestBody MonitoriaRequestDTO dto) {
        return monitoriaService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return monitoriaService.deletar(id);
    }

    @PostMapping("/{monitoriaId}/inscrever-se")
    public ResponseEntity<Void> inscreverAluno(@PathVariable Long monitoriaId, Authentication authentication) {
        String alunoMatricula = authentication.getName();
        monitoriaService.inscreverAluno(monitoriaId, alunoMatricula);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
