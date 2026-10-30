package br.com.fatec.estagios.comum;

import lombok.Getter;

@Getter
public enum ModalidadeTrabalho {
    PRESENCIAL("Presencial"),
    REMOTO("Remoto"),
    HIBRIDO("Híbrido");

    private final String descricao;

    ModalidadeTrabalho(String descricao) {
        this.descricao = descricao;
    }
}
