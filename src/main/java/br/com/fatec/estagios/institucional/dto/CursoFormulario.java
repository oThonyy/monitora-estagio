package br.com.fatec.estagios.institucional.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CursoFormulario(
        @NotBlank(message = "Informe o nome do curso.")
        @Size(max = 100)
        String nomeCurso
) {
    public static CursoFormulario novo() {
        return new CursoFormulario("");
    }
}
