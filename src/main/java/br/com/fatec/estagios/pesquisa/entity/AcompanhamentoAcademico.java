package br.com.fatec.estagios.pesquisa.entity;

import br.com.fatec.estagios.aluno.entity.Aluno;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "acompanhamento_academico", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_aluno", "periodo_letivo"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcompanhamentoAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_acompanhamento")
    private Long idAcompanhamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno aluno;

    @Column(name = "periodo_letivo", nullable = false, length = 10)
    private String periodoLetivo;

    @Column(name = "qtd_disciplina_cursada", nullable = false)
    private Integer quantidadeDisciplinaCursada = 0;

    @Column(name = "qtd_disciplina_trancada", nullable = false)
    private Integer quantidadeDisciplinaTrancada = 0;

    @Column(name = "qtd_trancamento_curso")
    private Integer quantidadeTrancamentoCurso = 0;

    @Column(name = "intencao_trancar_curso")
    private Boolean intencaoTrancarCurso;

    @Column(name = "observacao_conciliacao", length = 1000)
    private String observacaoConciliacao;
}
