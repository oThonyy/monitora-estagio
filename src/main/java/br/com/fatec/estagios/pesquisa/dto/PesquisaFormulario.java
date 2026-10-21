package br.com.fatec.estagios.pesquisa.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public record PesquisaFormulario(
    @NotNull(message = "Selecione o aluno.")
    Long idAluno,
    @NotNull(message = "Informe a data da coleta.")
    @PastOrPresent(message = "A coleta não pode estar no futuro.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dataColeta,
    @NotNull(message = "Informe a quantidade de estágios realizados.")
    @Min(value = 0, message = "A quantidade não pode ser negativa.")
    Integer quantidadeEstagioRealizado,
    @NotNull(message = "Informe o tempo de procura em meses, inclusive zero.")
    @Min(value = 0, message = "O tempo de procura não pode ser negativo.")
    Integer tempoProcurandoEstagio,
    @Size(max = 200, message = "Use até 200 caracteres.")
    String observacoes
) {
    public static PesquisaFormulario nova(Long idAluno) {
        return new PesquisaFormulario(idAluno, null, 0, 0, null);
    }
}
