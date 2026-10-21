package br.com.fatec.estagios.pesquisa.service;

import br.com.fatec.estagios.aluno.repository.AlunoRepository;
import br.com.fatec.estagios.comum.*;
import br.com.fatec.estagios.pesquisa.dto.*;
import br.com.fatec.estagios.pesquisa.entity.PesquisaEstagio;
import br.com.fatec.estagios.pesquisa.repository.PesquisaEstagioRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.ArrayList;

@Service
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PesquisaService {
    private final PesquisaEstagioRepository pesquisas;
    private final AlunoRepository alunos;

    public Page<PesquisaEstagio> listar(PesquisaFiltro filtro, int pagina) {
        return pesquisas.findAll((root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (filtro.aluno() != null) {
                predicates.add(cb.equal(root.get("aluno").get("idAluno"), filtro.aluno()));
            }
            if (filtro.unidade() != null) {
                predicates.add(cb.equal(root.get("aluno").get("ofertaCurso").get("unidadeFatec").get("idUnidadeFatec"), filtro.unidade()));
            }
            if (filtro.curso() != null) {
                predicates.add(cb.equal(root.get("aluno").get("ofertaCurso").get("curso").get("idCurso"), filtro.curso()));
            }
            if (filtro.inicio() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataColeta"), filtro.inicio()));
            }
            if (filtro.fim() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataColeta"), filtro.fim()));
            }
            if (filtro.experiencia() != null) {
                predicates.add(filtro.experiencia() ? cb.greaterThan(root.get("quantidadeEstagioRealizado"), 0)
                    : cb.equal(root.get("quantidadeEstagioRealizado"), 0));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        }, PageRequest.of(Math.max(0, pagina), 15, Sort.by(Sort.Direction.DESC, "dataColeta", "idPesquisaEstagio")));
    }

    public PesquisaEstagio buscar(Long id) {
        return pesquisas.findByIdWithDetails(id).orElseThrow(RegistroNaoEncontradoException::new);
    }

    public PesquisaFormulario formulario(Long id) {
        var pesquisa = buscar(id);
        return new PesquisaFormulario(pesquisa.getAluno().getIdAluno(), pesquisa.getDataColeta(),
            pesquisa.getQuantidadeEstagioRealizado(), pesquisa.getTempoProcurandoEstagio(), pesquisa.getObservacoes());
    }

    @Transactional
    public Long salvar(Long id, @Valid PesquisaFormulario form) {
        var pesquisa = id == null ? new PesquisaEstagio() : buscar(id);
        var aluno = alunos.findById(form.idAluno()).orElseThrow(() ->
            new DomainException("idAluno", "O aluno selecionado não existe."));
        if (aluno.getDataNascimento() != null && form.dataColeta().isBefore(aluno.getDataNascimento())) {
            throw new DomainException("Referente a Data_Coleta", "A coleta não pode anteceder o nascimento do aluno.");
        }
        pesquisa.setDataColeta(form.dataColeta());
        pesquisa.setQuantidadeEstagioRealizado(form.quantidadeEstagioRealizado());
        pesquisa.setTempoProcurandoEstagio(form.tempoProcurandoEstagio());
        pesquisa.setObservacoes(form.observacoes());
        pesquisa.setAluno(aluno);
        return pesquisas.saveAndFlush(pesquisa).getIdPesquisaEstagio();
    }

    @Transactional
    public void excluir(Long id) {
        pesquisas.delete(buscar(id));
        pesquisas.flush();
    }
}
