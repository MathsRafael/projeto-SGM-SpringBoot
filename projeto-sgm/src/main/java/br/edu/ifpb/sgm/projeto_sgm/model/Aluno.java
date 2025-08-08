package br.edu.ifpb.sgm.projeto_sgm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "aluno")
public class Aluno {

    @Id
    protected Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "id")
    private Pessoa pessoa;

    @ManyToMany
    @JoinTable(
        name = "aluno_disciplinas_pagas",
        joinColumns = @JoinColumn(name = "aluno_id"),
        inverseJoinColumns = @JoinColumn(name = "disciplina_id")
    )
    private Set<Disciplina> disciplinasPagas = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "monitor_disciplinas",
            joinColumns = @JoinColumn(name = "aluno_id"),
            inverseJoinColumns = @JoinColumn(name = "disciplina_id")
    )
    private Set<Disciplina> disciplinaMonitoria = new HashSet<>();

    @Column
    private Boolean cadastrado = true;


    @Column(name = "cre")
    private Double cre;
}
