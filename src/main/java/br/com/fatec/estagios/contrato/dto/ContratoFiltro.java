package br.com.fatec.estagios.contrato.dto;

import br.com.fatec.estagios.comum.ModalidadeTrabalho;
import br.com.fatec.estagios.comum.SituacaoContrato;
import br.com.fatec.estagios.comum.TipoContrato;

public record ContratoFiltro(
    String busca,
    Long aluno,
    Long empresa,
    TipoContrato tipoContrato,
    ModalidadeTrabalho modalidadeTrabalho,
    SituacaoContrato situacaoContrato
) {
}
