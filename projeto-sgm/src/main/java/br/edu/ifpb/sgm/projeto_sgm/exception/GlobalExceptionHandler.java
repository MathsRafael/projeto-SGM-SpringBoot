package br.edu.ifpb.sgm.projeto_sgm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DisciplinaNotFoundException.class)
    public ResponseEntity<String> handleDisciplinaNotFound(DisciplinaNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MonitoriaNotFoundException.class)
    public ResponseEntity<String> handleMonitoriaNotFound(MonitoriaNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CursoNotFoundException.class)
    public ResponseEntity<String> handleCursoNotFound(CursoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InstituicaoNotFoundException.class)
    public ResponseEntity<String> handleInstituicaoNotFound(InstituicaoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CoordenadorNotFoundException.class)
    public ResponseEntity<String> handleCoordenadorNotFound(CoordenadorNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NivelNotFoundException.class)
    public ResponseEntity<String> handleNivelNotFound(NivelNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ProfessorNotFoundException.class)
    public ResponseEntity<String> handleProfessorNotFound(ProfessorNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ProcessoSeletivoNotFoundException.class)
    public ResponseEntity<String> handleProcessoSeletivoNotFound(ProcessoSeletivoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AlunoNotFoundException.class)
    public ResponseEntity<String> handleAlunoNotFound(AlunoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(PessoaNotFoundException.class)
    public ResponseEntity<String> handlePessoaNotFound(PessoaNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MonitorNotFoundException.class)
    public ResponseEntity<String> handleMonitorNotFound(MonitorNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno: " +
                ex.getMessage());
    }
}
