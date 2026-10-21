package br.com.fatec.estagios.institucional.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "unidade_fatec")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeFatec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unidade_fatec")
    private Long idUnidadeFatec;

    @Column(name = "nome_unidade", nullable = false)
    private String nomeUnidade;

    @Column(name = "cep", length = 10)
    private String cep;

    @Column(name = "logradouro")
    private String logradouro;

    @Column(name = "municipio")
    private String municipio;

    @Column(name = "uf", length = 2)
    private String uf;

    @OneToMany(mappedBy = "unidadeFatec")
    private List<OfertaCurso> ofertasCursos = new ArrayList<>();
}
