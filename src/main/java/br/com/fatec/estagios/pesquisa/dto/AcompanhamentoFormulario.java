package br.com.fatec.estagios.pesquisa.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AcompanhamentoFormulario(
    @NotNull(message = "Informe o semestre do aluno no curso.")
    @Min(value = 1, message = "O semestre do curso deve ser pelo menos 1.")
    Integer periodoLetivo,

    @Size(max = 10)
    String periodoOriginal,

    @NotNull(message = "Informe a quantidade de disciplinas cursadas.")
    @Min(value = 0, message = "A quantidade de disciplinas cursadas não pode ser negativa.")
    Integer quantidadeDisciplinaCursada,

    @NotNull(message = "Informe a quantidade de disciplinas trancadas.")
    @Min(value = 0, message = "A quantidade de disciplinas trancadas não pode ser negativa.")
    Integer quantidadeDisciplinaTrancada,

    @NotNull(message = "Informe o total acumulado de trancamentos do curso.")
    @Min(value = 0, message = "O total de trancamentos do curso não pode ser negativo.")
    Integer quantidadeTrancamentoCurso,

    Boolean intencaoTrancarCurso,

    @Size(max = 200, message = "Use até 200 caracteres nas observações de conciliação.")
    String observacaoConciliacao
) {
    public static AcompanhamentoFormulario novo() {
        return new AcompanhamentoFormulario(null, null, 0, 0, 0, null, null);
    }
}
