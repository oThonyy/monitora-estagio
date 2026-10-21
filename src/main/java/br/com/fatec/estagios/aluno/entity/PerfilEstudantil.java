package br.com.fatec.estagios.aluno.entity;

import br.com.fatec.estagios.comum.TipoEscola;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name= "perfil_estudantil")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerfilEstudantil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil_estudantil")
    private Long idPerfilEstudantil;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aluno", nullable = false, unique = true)
    private Aluno aluno;

    @Column(name = "qtd_tentativa")
    private Integer quantidadeTentativa;

    @Column(name = "realizou_cursinho")
    private Boolean realizouCursinho;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_escola", length = 20)
    private TipoEscola tipoEscola;

    @Column(name = "faixa_renda_familiar", length = 50)
    private String faixaRendaFamiliar;

    @Column(name = "contribuicao_renda_familiar", length = 100)
    private String contribuicaoRendaFamiliar;

    @Column(name = "ciencia_vestibular", length = 200)
    private String cienciaVestibular;
}
