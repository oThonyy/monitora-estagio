package br.com.fatec.estagios.institucional.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UnidadeFormulario (
    @NotBlank(message = "Informe o nome da unidade.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    String nomeUnidade,

    @Size(max = 10, message = "O CEP deve ter no máximo 10 caracteres.")
    String cep,

    @Size(max = 200, message = "O logradouro deve ter no máximo 200 caracteres.")
    String logradouro,

    @Size(max = 100, message = "O município deve ter no máximo 100 caracteres.")
    String municipio,

    @Size(max = 2, message = "A UF deve ter 2 caracteres.")
    String uf
) {
    public static UnidadeFormulario novo() {
        return new UnidadeFormulario("", "", "", "", "");
    }
}
