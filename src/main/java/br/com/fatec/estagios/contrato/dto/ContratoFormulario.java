package br.com.fatec.estagios.contrato.dto;

import br.com.fatec.estagios.comum.ModalidadeTrabalho;
import br.com.fatec.estagios.comum.SituacaoContrato;
import br.com.fatec.estagios.comum.TipoContrato;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratoFormulario(
    @NotNull(message = "Selecione o aluno.")
    Long idAluno,

    Long idEmpresa,

    @NotBlank(message = "Informe a área.")
    @Size(max = 100, message = "A área deve ter no máximo 100 caracteres.")
    String areaAtuacao,

    @NotBlank(message = "Informe o cargo.")
    @Size(max = 100, message = "O cargo deve ter no máximo 100 caracteres.")
    String cargo,

    @NotBlank(message = "Informe a função.")
    @Size(max = 100, message = "A função deve ter no máximo 100 caracteres.")
    String funcao,

    @Size(max = 100, message = "O curso deve ter no máximo 100 caracteres.")
    String curso,

    @Size(max = 50, message = "O período deve ter no máximo 50 caracteres.")
    String periodoCurso,

    @Size(max = 50, message = "O horário deve ter no máximo 50 caracteres.")
    String horarioCurso,

    @NotNull(message = "Selecione o tipo de contrato.")
    TipoContrato tipoContrato,

    @NotNull(message = "Selecione a jornada.")
    ModalidadeTrabalho modalidadeTrabalho,

    Boolean possuiBancoHoras,

    @NotNull(message = "Informe a duração do contrato em meses.")
    @Min(value = 1, message = "A duração do contrato deve ser de no mínimo 1 mês.")
    Integer duracaoMeses,

    @NotNull(message = "Selecione o status do contrato.")
    SituacaoContrato situacaoContrato,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dataInicio,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dataFimPrevista,

    @Min(value = 1, message = "A carga horária semanal deve ser de pelo menos 1 hora.")
    Integer cargaHorariaSemanal,

    @DecimalMin(value = "0.0", inclusive = true, message = "O valor da bolsa não pode ser negativo.")
    BigDecimal valorBolsaMensal,

    Boolean auxilioTransporte,

    Boolean flexibilidadeHorario,

    @Size(max = 200, message = "As atividades desenvolvidas devem ter no máximo 200 caracteres.")
    String atividadesDesenvolvidas
) {
    public ContratoFormulario {
        possuiBancoHoras = Boolean.TRUE.equals(possuiBancoHoras);
        auxilioTransporte = Boolean.TRUE.equals(auxilioTransporte);
        flexibilidadeHorario = Boolean.TRUE.equals(flexibilidadeHorario);
    }

    public static ContratoFormulario novo(Long idAluno) {
        return new ContratoFormulario(
            idAluno, null, null, null, null, null, null, null,
            TipoContrato.ESTAGIO, ModalidadeTrabalho.PRESENCIAL, false, 6,
            SituacaoContrato.ATIVO, null, null, null, null, false, false, null
        );
    }
}
