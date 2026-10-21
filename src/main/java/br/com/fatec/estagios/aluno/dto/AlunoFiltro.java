package br.com.fatec.estagios.aluno.dto;


public record AlunoFiltro(
    String busca,
    Long unidade,
    Long curso,
    Long oferta
) {
}
