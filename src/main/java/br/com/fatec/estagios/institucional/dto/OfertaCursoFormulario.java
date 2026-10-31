package br.com.fatec.estagios.institucional.dto;

import br.com.fatec.estagios.comum.Turno;
import jakarta.validation.constraints.*;

public record OfertaCursoFormulario(
        @NotNull(message = "Selecione a unidade FATEC.")
        Long idUnidadeFatec,

        @NotNull(message = "Selecione o turno.")
        Turno turno,

        @NotNull(message = "Informe a duração.")
        @Min(value = 1)
        Integer duracao
) {
    public static OfertaCursoFormulario novo() {
        return new OfertaCursoFormulario(null, null, null);
    }
}