package br.com.fatec.estagios.comum;

import lombok.Getter;

@Getter
public enum SituacaoContrato {
    ATIVO("Ativo"),
    ENCERRADO("Encerrado"),
    CONCLUIDO("Concluído"),
    RESCINDIDO("Rescindido"),
    CANCELADO("Cancelado");

    private final String descricao;

    SituacaoContrato(String descricao) {
        this.descricao = descricao;
    }
}
