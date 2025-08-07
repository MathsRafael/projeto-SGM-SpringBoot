package br.edu.ifpb.sgm.projeto_sgm.controller;

import br.edu.ifpb.sgm.projeto_sgm.dto.ProcessoSeletivoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProcessoSeletivoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.service.ProcessoSeletivoServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/processos-seletivos")
public class ProcessoSeletivoControllerImp {

    @Autowired
    private ProcessoSeletivoServiceImp processoSeletivoService;

    @PostMapping
    public ResponseEntity<ProcessoSeletivoResponseDTO> criar(@RequestBody ProcessoSeletivoRequestDTO dto) {
        return processoSeletivoService.salvar(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcessoSeletivoResponseDTO> buscarPorId(@PathVariable Long id) {
        return processoSeletivoService.buscarPorId(id);
    }

    @GetMapping
    public ResponseEntity<List<ProcessoSeletivoResponseDTO>> listarTodos() {
        return processoSeletivoService.listarTodos();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcessoSeletivoResponseDTO> atualizar(@PathVariable Long id,
                                                                 @RequestBody ProcessoSeletivoRequestDTO dto) {
        return processoSeletivoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return processoSeletivoService.deletar(id);
    }

    @PutMapping("/{id}/fechar")
    public ResponseEntity<ProcessoSeletivoResponseDTO> fecharEdital(@PathVariable Long id) {
        return ResponseEntity.ok(processoSeletivoService.fechar(id));
    }
}
