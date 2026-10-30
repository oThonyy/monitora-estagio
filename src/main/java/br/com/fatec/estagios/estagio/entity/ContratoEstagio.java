package br.com.fatec.estagios.estagio.entity;

import br.com.fatec.estagios.aluno.entity.Aluno;
import br.com.fatec.estagios.comum.ModalidadeTrabalho;
import br.com.fatec.estagios.comum.SituacaoContrato;
import br.com.fatec.estagios.comum.TipoContrato;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contrato_estagio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContratoEstagio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contrato_estagio")
    private Long idContratoEstagio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

    @Column(name = "area_atuacao", length = 100)
    private String areaAtuacaoEstagio;

    @Column(name = "cargo_estagio", length = 100)
    private String cargoEstagio;

    @Column(name = "funcao", length = 100)
    private String funcao;

    @Column(name = "curso", length = 100)
    private String curso;

    @Column(name = "periodo_curso", length = 50)
    private String periodoCurso;

    @Column(name = "horario_curso", length = 50)
    private String horarioCurso;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contrato", length = 30)
    private TipoContrato tipoContrato;

    @Enumerated(EnumType.STRING)
    @Column(name = "modalidade_trabalho", length = 30)
    private ModalidadeTrabalho modalidadeTrabalho;

    @Column(name = "possui_banco_horas")
    private Boolean possuiBancoHoras = false;

    @Column(name = "duracao_meses")
    private Integer duracaoMeses;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao_contrato", nullable = false, length = 30)
    private SituacaoContrato situacaoContrato = SituacaoContrato.ATIVO;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim_prevista")
    private LocalDate dataFimPrevista;

    @Column(name = "carga_horaria_semanal")
    private Integer cargaHorariaSemanal;

    @Column(name = "valor_bolsa_mensal", precision = 10, scale = 2)
    private BigDecimal valorBolsaMensal;

    @Column(name = "auxilio_transporte")
    private Boolean auxilioTransporte = false;

    @Column(name = "flexibilidade_horario")
    private Boolean flexibilidadeHorario = false;

    @Column(name = "atividades_desenvolvidas", length = 200)
    private String atividadesDesenvolvidas;
}
