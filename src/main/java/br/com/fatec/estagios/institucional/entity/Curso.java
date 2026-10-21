package br.com.fatec.estagios.institucional.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "curso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Long idCurso;

    @Column(name = "nome_curso", nullable = false)
    private String nomeCurso;

    @OneToMany(mappedBy = "curso")
    private List<OfertaCurso> ofertasCursos = new ArrayList<>();
}
