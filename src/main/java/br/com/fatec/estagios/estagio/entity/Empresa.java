package br.com.fatec.estagios.estagio.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name= "empresa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Long idEmpresa;

    @Column(name = "cnpj", length = 18, unique = true)
    private String cnpj;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    @Column(name = "nome_fantasia")
    private String nomeFantasia;

    @Column(name = "segmento_mercado", length = 100)
    private String segmentoMercado;

    @Column(name = "area_atuacao", length = 100)
    private String areaAtuacaoEmpresa;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "cep_empresa", length = 10)
    private String cepEmpresa;

    @Column(name = "endereco", length = 200)
    private String endereco;

    @Column(name = "linkedin", length = 100)
    private String linkedin;
}