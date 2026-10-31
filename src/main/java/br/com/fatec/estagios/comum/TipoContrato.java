package br.com.fatec.estagios.comum;

import lombok.Getter;

@Getter
public enum TipoContrato {
    ESTAGIO("Estágio"),
    CLT("CLT"),
    PJ("PJ");

    private final String descricao;

    TipoContrato(String descricao) {
        this.descricao = descricao;
    }
}
