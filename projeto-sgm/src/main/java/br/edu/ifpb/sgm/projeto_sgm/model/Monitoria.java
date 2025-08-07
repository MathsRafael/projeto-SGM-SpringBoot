package br.edu.ifpb.sgm.projeto_sgm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Monitoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "disciplina_id")
    private Disciplina disciplina;

    @Column(nullable = false)
    private int numeroVaga;

    @Column(nullable = false)
    private int cargaHoraria;

    @ManyToOne(optional = false)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @OneToMany(mappedBy = "monitoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonitoriaInscritos> inscricoes = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "processoSeletivo_id")
    private ProcessoSeletivo processoSeletivo;

}
