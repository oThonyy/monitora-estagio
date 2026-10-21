package br.com.fatec.estagios.comum;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {
    private final String campo;

    public DomainException(String campo, String mensagem) {
        super(mensagem);
        this.campo = campo;
    }
}
