package br.com.fatec.estagios.pesquisa.entity;

import br.com.fatec.estagios.aluno.entity.Aluno;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name= "pesquisa_estagio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PesquisaEstagio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pesquisa_estagio")
    private Long idPesquisaEstagio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno aluno;

    @Column(name = "data_coleta")
    private LocalDate dataColeta;

    @Column(name = "qtd_estagio_realizado")
    private int quantidadeEstagioRealizado;

    // Quantidade em Meses

    @Column(name = "tempo_procurando_estagio")
    private int tempoProcurandoEstagio;

    @Column(name = "observacoes", length = 200)
    private String observacoes;
}
