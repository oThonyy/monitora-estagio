package br.com.fatec.estagios.pesquisa.dto;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record PesquisaFiltro(
    Long aluno,
    Long unidade,
    Long curso,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate inicio,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate fim,
    Boolean experiencia
) {
}
