package br.com.fatec.estagios.estagio.entity;

import br.com.fatec.estagios.aluno.entity.Aluno;
import br.com.fatec.estagios.comum.ModalidadeTrabalho;
import br.com.fatec.estagios.comum.SituacaoContrato;
import br.com.fatec.estagios.empresa.entity.Empresa;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name= "contrato_estagio")
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
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @Column(name = "area_atuacao", length = 100)
    private String areaAtuacaoEstagio;

    @Column(name = "cargo_estagio", length = 100)
    private String cargoEstagio;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim_prevista")
    private LocalDate dataFimPrevista;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao_contrato", nullable = false, length = 30)
    private SituacaoContrato situacaoContrato;

    @Enumerated(EnumType.STRING)
    @Column(name = "modalidade_trabalho", length = 30)
    private ModalidadeTrabalho modalidadeTrabalho;

    @Column(name = "carga_horaria_semanal")
    private Integer cargaHorariaSemanal;

    @Column(name = "valor_bolsa_mensal", precision = 10, scale = 2)
    private BigDecimal valorBolsaMensal;

    @Column(name = "auxilio_transporte")
    private Boolean AuxilioTransporte;

    @Column(name = "flexibilidade_horario")
    private Boolean FlexibilidadeHorario;

    @Column(name = "atividades_desenvolvidas", length = 200)
    private String atividadesDesenvolvidas;
}
