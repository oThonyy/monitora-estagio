package br.com.fatec.estagios.estagio.service;

import br.com.fatec.estagios.aluno.repository.AlunoRepository;
import br.com.fatec.estagios.comum.DomainException;
import br.com.fatec.estagios.comum.RegistroNaoEncontradoException;
import br.com.fatec.estagios.contrato.dto.ContratoFiltro;
import br.com.fatec.estagios.contrato.dto.ContratoFormulario;
import br.com.fatec.estagios.estagio.entity.ContratoEstagio;
import br.com.fatec.estagios.estagio.entity.Empresa;
import br.com.fatec.estagios.estagio.repository.ContratoEstagioRepository;
import br.com.fatec.estagios.estagio.repository.EmpresaRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstagioService {

    private final ContratoEstagioRepository contratos;
    private final AlunoRepository alunos;
    private final EmpresaRepository empresas;

    public Page<ContratoEstagio> listar(ContratoFiltro filtro, int pagina) {
        return contratos.findAll((root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (filtro != null) {
                if (StringUtils.hasText(filtro.busca())) {
                    String termo = "%" + filtro.busca().trim().toLowerCase() + "%";
                    var alunoJoin = root.join("aluno");
                    predicates.add(cb.or(
                        cb.like(cb.lower(alunoJoin.get("nome")), termo),
                        cb.like(cb.lower(root.get("cargoEstagio")), termo),
                        cb.like(cb.lower(root.get("funcao")), termo),
                        cb.like(cb.lower(root.get("areaAtuacaoEstagio")), termo),
                        cb.like(cb.lower(root.get("curso")), termo)
                    ));
                }
                if (filtro.aluno() != null) {
                    predicates.add(cb.equal(root.get("aluno").get("idAluno"), filtro.aluno()));
                }
                if (filtro.empresa() != null) {
                    predicates.add(cb.equal(root.get("empresa").get("idEmpresa"), filtro.empresa()));
                }
                if (filtro.tipoContrato() != null) {
                    predicates.add(cb.equal(root.get("tipoContrato"), filtro.tipoContrato()));
                }
                if (filtro.modalidadeTrabalho() != null) {
                    predicates.add(cb.equal(root.get("modalidadeTrabalho"), filtro.modalidadeTrabalho()));
                }
                if (filtro.situacaoContrato() != null) {
                    predicates.add(cb.equal(root.get("situacaoContrato"), filtro.situacaoContrato()));
                }
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        }, PageRequest.of(Math.max(0, pagina), 15, Sort.by(Sort.Direction.DESC, "idContratoEstagio")));
    }

    public ContratoEstagio buscar(Long id) {
        return contratos.findByIdWithDetails(id).orElseThrow(RegistroNaoEncontradoException::new);
    }

    public ContratoFormulario formulario(Long id) {
        var c = buscar(id);
        return new ContratoFormulario(
            c.getAluno().getIdAluno(),
            c.getEmpresa() != null ? c.getEmpresa().getIdEmpresa() : null,
            c.getAreaAtuacaoEstagio(),
            c.getCargoEstagio(),
            c.getFuncao(),
            c.getCurso(),
            c.getPeriodoCurso(),
            c.getHorarioCurso(),
            c.getTipoContrato(),
            c.getModalidadeTrabalho(),
            c.getPossuiBancoHoras(),
            c.getDuracaoMeses(),
            c.getSituacaoContrato(),
            c.getDataInicio(),
            c.getDataFimPrevista(),
            c.getCargaHorariaSemanal(),
            c.getValorBolsaMensal(),
            c.getAuxilioTransporte(),
            c.getFlexibilidadeHorario(),
            c.getAtividadesDesenvolvidas()
        );
    }

    @Transactional
    public Long salvar(Long id, @Valid ContratoFormulario form) {
        var contrato = id == null ? new ContratoEstagio() : buscar(id);

        var aluno = alunos.findById(form.idAluno()).orElseThrow(() ->
            new DomainException("idAluno", "O aluno selecionado não existe."));

        if (form.idEmpresa() != null) {
            var empresa = empresas.findById(form.idEmpresa()).orElseThrow(() ->
                new DomainException("idEmpresa", "A empresa selecionada não existe."));
            contrato.setEmpresa(empresa);
        } else {
            contrato.setEmpresa(null);
        }

        if (form.dataInicio() != null && form.dataFimPrevista() != null && form.dataInicio().isAfter(form.dataFimPrevista())) {
            throw new DomainException("dataFimPrevista", "A data final deve ser igual ou posterior à data inicial.");
        }

        String curso = StringUtils.hasText(form.curso()) ? form.curso()
            : (aluno.getOfertaCurso() != null && aluno.getOfertaCurso().getCurso() != null ? aluno.getOfertaCurso().getCurso().getNomeCurso() : null);

        String horario = StringUtils.hasText(form.horarioCurso()) ? form.horarioCurso()
            : (aluno.getOfertaCurso() != null && aluno.getOfertaCurso().getTurno() != null ? aluno.getOfertaCurso().getTurno().name() : null);

        contrato.setAluno(aluno);
        contrato.setAreaAtuacaoEstagio(form.areaAtuacao());
        contrato.setCargoEstagio(form.cargo());
        contrato.setFuncao(form.funcao());
        contrato.setCurso(curso);
        contrato.setPeriodoCurso(form.periodoCurso());
        contrato.setHorarioCurso(horario);
        contrato.setTipoContrato(form.tipoContrato());
        contrato.setModalidadeTrabalho(form.modalidadeTrabalho());
        contrato.setPossuiBancoHoras(Boolean.TRUE.equals(form.possuiBancoHoras()));
        contrato.setDuracaoMeses(form.duracaoMeses());
        contrato.setSituacaoContrato(form.situacaoContrato());
        contrato.setDataInicio(form.dataInicio());
        contrato.setDataFimPrevista(form.dataFimPrevista());
        contrato.setCargaHorariaSemanal(form.cargaHorariaSemanal());
        contrato.setValorBolsaMensal(form.valorBolsaMensal());
        contrato.setAuxilioTransporte(Boolean.TRUE.equals(form.auxilioTransporte()));
        contrato.setFlexibilidadeHorario(Boolean.TRUE.equals(form.flexibilidadeHorario()));
        contrato.setAtividadesDesenvolvidas(form.atividadesDesenvolvidas());

        return contratos.saveAndFlush(contrato).getIdContratoEstagio();
    }

    @Transactional
    public void excluir(Long id) {
        contratos.delete(buscar(id));
        contratos.flush();
    }

    public List<ContratoEstagio> listarPorAluno(Long idAluno) {
        return contratos.findByAlunoIdAluno(idAluno);
    }
}
