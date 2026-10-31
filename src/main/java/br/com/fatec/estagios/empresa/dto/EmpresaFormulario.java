package br.com.fatec.estagios.empresa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmpresaFormulario(
    @Size(max = 18, message = "O CNPJ deve ter no máximo 18 caracteres.")
    String cnpj,

    @NotBlank(message = "Informe a razão social.")
    @Size(max = 200, message = "A razão social deve ter no máximo 200 caracteres.")
    String razaoSocial,

    @Size(max = 200, message = "O nome fantasia deve ter no máximo 200 caracteres.")
    String nomeFantasia,

    @Size(max = 100, message = "O segmento de mercado deve ter no máximo 100 caracteres.")
    String segmentoMercado,

    @Size(max = 100, message = "A área de atuação deve ter no máximo 100 caracteres.")
    String areaAtuacaoEmpresa,

    @Email(message = "Informe um e-mail válido.")
    @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres.")
    String email,

    @Size(max = 10, message = "O CEP deve ter no máximo 10 caracteres.")
    String cepEmpresa,

    @Size(max = 200, message = "O endereço deve ter no máximo 200 caracteres.")
    String endereco,

    @Size(max = 100, message = "O LinkedIn deve ter no máximo 100 caracteres.")
    String linkedin
) {
    public static EmpresaFormulario novo() {
        return new EmpresaFormulario("", "", "", "", "", "", "", "", "");
    }
}

