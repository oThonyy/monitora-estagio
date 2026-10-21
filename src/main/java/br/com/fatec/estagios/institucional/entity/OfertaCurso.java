package br.com.fatec.estagios.institucional.entity;

import br.com.fatec.estagios.comum.Turno;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "oferta_curso", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_unidade_fatec", "id_curso", "turno"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfertaCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oferta_curso")
    private Long idOfertaCurso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unidade_fatec", nullable = false)
    private UnidadeFatec unidadeFatec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

    @Enumerated(EnumType.STRING)
    @Column(name = "turno", nullable = false, length = 20)
    private Turno turno;

    @Column(name = "duracao", nullable = false)
    private Integer duracao;
}
