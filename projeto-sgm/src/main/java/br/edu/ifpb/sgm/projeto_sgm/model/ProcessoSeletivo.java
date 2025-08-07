package br.edu.ifpb.sgm.projeto_sgm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessoSeletivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dataInicioInscricoes;

    @Column(nullable = false)
    private LocalDate dataFimInscricoes;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProcessoSeletivo status;

    @Column(nullable = false, unique = true)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "instiuicao_id", nullable = false)
    private Instituicao instituicao;

    @OneToMany(mappedBy = "processoSeletivo", cascade = CascadeType.ALL)
    private List<Monitoria> monitorias = new ArrayList<>();

}
