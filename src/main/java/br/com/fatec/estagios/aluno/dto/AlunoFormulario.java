package br.com.fatec.estagios.aluno.dto;

import br.com.fatec.estagios.comum.TipoEscola;
import br.com.fatec.estagios.pesquisa.dto.AcompanhamentoFormulario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public record AlunoFormulario(
    @NotBlank(message = "Informe o nome do aluno.")
    @Size(max = 200)
    String nome,
    @Size(max = 20)
    String ra,
    @NotNull(message = "Selecione o curso.")
    Long idOfertaCurso,
    @Size(max = 30)
    String genero,
    @Size(max = 30)
    String etnia,
    @Past(message = "A data de nascimento deve estar no passado.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dataNascimento,
    @Size(max = 200)
    String endereco,
    Boolean pcd,
    Boolean incluirPerfil,
    @Min(value = 0, message = "Informe zero ou mais tentativas.")
    Integer quantidadeTentativa,
    Boolean realizouCursinho,
    TipoEscola tipoEscola,
    @Size(max = 50)
    String faixaRendaFamiliar,
    @Size(max = 100)
    String contribuicaoRendaFamiliar,
    @Size(max = 200)
    String cienciaVestibular,

    @Valid
    @NotNull(message = "Preencha o acompanhamento acadêmico.")
    AcompanhamentoFormulario acompanhamento
) {
    public AlunoFormulario {
        pcd = Boolean.TRUE.equals(pcd);
        incluirPerfil = Boolean.TRUE.equals(incluirPerfil);
        if (acompanhamento == null) {
            acompanhamento = AcompanhamentoFormulario.novo();
        }
    }

    public static AlunoFormulario novo() {
        return new AlunoFormulario(null, null, null, null, null, null, null, false, false,
            null, null, null, null, null, null, AcompanhamentoFormulario.novo());
    }
}
