package br.com.fatec.estagios.pesquisa.service;

import br.com.fatec.estagios.aluno.entity.Aluno;
import br.com.fatec.estagios.comum.DomainException;
import br.com.fatec.estagios.comum.RegistroNaoEncontradoException;
import br.com.fatec.estagios.pesquisa.dto.AcompanhamentoFormulario;
import br.com.fatec.estagios.pesquisa.entity.AcompanhamentoAcademico;
import br.com.fatec.estagios.pesquisa.repository.AcompanhamentoAcademicoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.Comparator;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AcompanhamentoService {
    private final AcompanhamentoAcademicoRepository acompanhamentos;

    public List<AcompanhamentoAcademico> listar(Long idAluno) {
        return acompanhamentos.findByAlunoIdAlunoOrderByIdAcompanhamentoDesc(idAluno).stream()
            .sorted(Comparator.comparingInt((AcompanhamentoAcademico item) -> semestre(item.getPeriodoLetivo()))
                .reversed().thenComparing(AcompanhamentoAcademico::getIdAcompanhamento, Comparator.reverseOrder()))
            .toList();
    }

    private int semestre(String periodo) {
        if (periodo == null || !periodo.matches("[1-9][0-9]*")) {
            return 0;
        }
        try {
            return Integer.parseInt(periodo);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public AcompanhamentoFormulario novoFormulario() {
        return AcompanhamentoFormulario.novo();
    }

    public AcompanhamentoFormulario formulario(Long idAluno, String periodo) {
        var historico = listar(idAluno);
        AcompanhamentoAcademico acompanhamento;
        if (periodo != null) {
            acompanhamento = historico.stream().filter(item -> periodo.equals(item.getPeriodoLetivo()))
                .findFirst().orElseThrow(RegistroNaoEncontradoException::new);
        } else if (!historico.isEmpty()) {
            acompanhamento = historico.getFirst();
        } else {
            return novoFormulario();
        }
        int numeroSemestre = semestre(acompanhamento.getPeriodoLetivo());
        return new AcompanhamentoFormulario(numeroSemestre > 0 ? numeroSemestre : null,
            numeroSemestre > 0 ? null : acompanhamento.getPeriodoLetivo(),
            acompanhamento.getQuantidadeDisciplinaCursada(), acompanhamento.getQuantidadeDisciplinaTrancada(),
            acompanhamento.getQuantidadeTrancamentoCurso() == null ? 0 : acompanhamento.getQuantidadeTrancamentoCurso(),
            acompanhamento.getIntencaoTrancarCurso(), acompanhamento.getObservacaoConciliacao());
    }

    @Transactional
    public void salvar(Aluno aluno, @Valid AcompanhamentoFormulario form) {
        Integer duracao = aluno.getOfertaCurso().getDuracao();
        if (form.periodoLetivo() > duracao) {
            throw new DomainException("acompanhamento.periodoLetivo",
                "O semestre informado deve estar entre 1 e " + duracao + ", conforme a duração do curso.");
        }
        var historico = listar(aluno.getIdAluno());
        for (var anterior : historico) {
            int numeroSemestre = semestre(anterior.getPeriodoLetivo());
            Integer total = anterior.getQuantidadeTrancamentoCurso();
            if (numeroSemestre == 0 || total == null) {
                continue;
            }
            if (numeroSemestre < form.periodoLetivo() && total > form.quantidadeTrancamentoCurso()) {
                throw new DomainException("acompanhamento.quantidadeTrancamentoCurso",
                    "O total acumulado não pode ser menor que o declarado no " + numeroSemestre + "º semestre.");
            }
            if (numeroSemestre > form.periodoLetivo() && total < form.quantidadeTrancamentoCurso()) {
                throw new DomainException("acompanhamento.quantidadeTrancamentoCurso",
                    "O total acumulado não pode superar o declarado no " + numeroSemestre + "º semestre posterior.");
            }
        }
        String periodo = form.periodoLetivo().toString();
        var existente = acompanhamentos.findByAlunoIdAlunoAndPeriodoLetivo(aluno.getIdAluno(), periodo);
        AcompanhamentoAcademico acompanhamento;
        if (form.periodoOriginal() != null && !form.periodoOriginal().isBlank()) {
            acompanhamento = acompanhamentos.findByAlunoIdAlunoAndPeriodoLetivo(aluno.getIdAluno(), form.periodoOriginal())
                .orElseThrow(RegistroNaoEncontradoException::new);
            if (semestre(acompanhamento.getPeriodoLetivo()) != 0) {
                throw new DomainException("acompanhamento.periodoLetivo", "Selecione o acompanhamento que deseja editar.");
            }
            if (existente.isPresent()) {
                throw new DomainException("acompanhamento.periodoLetivo", "Já existe um acompanhamento para este semestre. Informe o semestre correspondente a este registro anterior.");
            }
        } else {
            acompanhamento = existente.orElseGet(AcompanhamentoAcademico::new);
        }
        acompanhamento.setQuantidadeDisciplinaCursada(form.quantidadeDisciplinaCursada());
        acompanhamento.setQuantidadeDisciplinaTrancada(form.quantidadeDisciplinaTrancada());
        acompanhamento.setQuantidadeTrancamentoCurso(form.quantidadeTrancamentoCurso());
        acompanhamento.setIntencaoTrancarCurso(form.intencaoTrancarCurso());
        acompanhamento.setObservacaoConciliacao(form.observacaoConciliacao());
        acompanhamento.setPeriodoLetivo(periodo);
        acompanhamento.setAluno(aluno);
        acompanhamentos.saveAndFlush(acompanhamento);
    }
}
