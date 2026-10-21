package br.com.fatec.estagios.aluno.entity;

import br.com.fatec.estagios.institucional.entity.OfertaCurso;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "aluno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aluno")
    private Long idAluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_oferta_curso", nullable = false)
    private OfertaCurso ofertaCurso;

    @Column(name = "ra", length = 20, unique = true)
    private String ra;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "genero", length = 30)
    private String genero;

    @Column(name = "etnia", length = 30)
    private String etnia;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "endereco", length = 500)
    private String endereco;

    @Column(name = "pcd", nullable = false)
    private Boolean pcd = false;

    @OneToOne(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private PerfilEstudantil perfilEstudantil;
}
