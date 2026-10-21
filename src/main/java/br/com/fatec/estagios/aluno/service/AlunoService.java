package br.com.fatec.estagios.aluno.service;

import br.com.fatec.estagios.aluno.dto.*;
import br.com.fatec.estagios.aluno.entity.*;
import br.com.fatec.estagios.aluno.repository.AlunoRepository;
import br.com.fatec.estagios.comum.*;
import br.com.fatec.estagios.estagio.repository.ContratoEstagioRepository;
import br.com.fatec.estagios.institucional.repository.OfertaCursoRepository;
import br.com.fatec.estagios.pesquisa.repository.*;
import br.com.fatec.estagios.pesquisa.service.AcompanhamentoService;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

@Service
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlunoService {
    private final AlunoRepository alunos;
    private final OfertaCursoRepository ofertas;
    private final PesquisaEstagioRepository pesquisas;
    private final AcompanhamentoAcademicoRepository acompanhamentos;
    private final ContratoEstagioRepository contratos;
    private final AcompanhamentoService acompanhamentoService;

    public Page<Aluno> listar(AlunoFiltro filtro, int pagina) {
        return alunos.findAll((root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (filtro.busca() != null && !filtro.busca().isBlank()) {
                String termo = "%" + filtro.busca().strip().toLowerCase(Locale.ROOT)
                    .replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%";
                predicates.add(cb.or(cb.like(cb.lower(root.get("nome")), termo, '\\'),
                    cb.like(cb.lower(root.get("ra")), termo, '\\')));
            }
            if (filtro.unidade() != null) {
                predicates.add(cb.equal(root.get("ofertaCurso").get("unidadeFatec").get("idUnidadeFatec"), filtro.unidade()));
            }
            if (filtro.curso() != null) {
                predicates.add(cb.equal(root.get("ofertaCurso").get("curso").get("idCurso"), filtro.curso()));
            }
            if (filtro.oferta() != null) {
                predicates.add(cb.equal(root.get("ofertaCurso").get("idOfertaCurso"), filtro.oferta()));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        }, PageRequest.of(Math.max(0, pagina), 15, Sort.by("nome").and(Sort.by("idAluno"))));
    }

    public Aluno buscar(Long id) {
        return alunos.findByIdWithDetails(id).orElseThrow(RegistroNaoEncontradoException::new);
    }

    public AlunoFormulario formulario(Long id) {
        return formulario(id, null);
    }

    public AlunoFormulario formulario(Long id, String periodo) {
        Aluno aluno = buscar(id);
        var perfil = aluno.getPerfilEstudantil();
        return new AlunoFormulario(aluno.getNome(), aluno.getRa(), aluno.getOfertaCurso().getIdOfertaCurso(),
            aluno.getGenero(), aluno.getEtnia(), aluno.getDataNascimento(), aluno.getEndereco(),
            Boolean.TRUE.equals(aluno.getPcd()), perfil != null,
            perfil == null ? null : perfil.getQuantidadeTentativa(),
            perfil == null ? null : perfil.getRealizouCursinho(),
            perfil == null ? null : perfil.getTipoEscola(),
            perfil == null ? null : perfil.getFaixaRendaFamiliar(),
            perfil == null ? null : perfil.getContribuicaoRendaFamiliar(),
            perfil == null ? null : perfil.getCienciaVestibular(),
            acompanhamentoService.formulario(id, periodo));
    }

    @Transactional
    public Long salvar(Long id, @Valid AlunoFormulario form) {
        Aluno aluno = id == null ? new Aluno() : buscar(id);
        String ra = form.ra() == null || form.ra().isBlank() ? null : form.ra().strip();
        if (ra != null && alunos.findByRa(ra).filter(outro -> !Objects.equals(outro.getIdAluno(), id)).isPresent()) {
            throw new DomainException("ra", "Este RA já pertence a outro aluno.");
        }
        var oferta = ofertas.findById(form.idOfertaCurso()).orElseThrow(() ->
            new DomainException("idOfertaCurso", "A oferta de curso selecionada não existe."));
        aluno.setGenero(form.genero());
        aluno.setEtnia(form.etnia());
        aluno.setDataNascimento(form.dataNascimento());
        aluno.setEndereco(form.endereco());
        aluno.setPcd(form.pcd());
        aluno.setNome(form.nome().strip());
        aluno.setRa(ra);
        aluno.setOfertaCurso(oferta);
        if (form.incluirPerfil()) {
            PerfilEstudantil perfil = aluno.getPerfilEstudantil();
            if (perfil == null) {
                perfil = new PerfilEstudantil();
            }
            perfil.setQuantidadeTentativa(form.quantidadeTentativa());
            perfil.setRealizouCursinho(form.realizouCursinho());
            perfil.setTipoEscola(form.tipoEscola());
            perfil.setFaixaRendaFamiliar(form.faixaRendaFamiliar());
            perfil.setContribuicaoRendaFamiliar(form.contribuicaoRendaFamiliar());
            perfil.setCienciaVestibular(form.cienciaVestibular());
            perfil.setAluno(aluno);
            aluno.setPerfilEstudantil(perfil);
        } else {
            aluno.setPerfilEstudantil(null);
        }
        alunos.saveAndFlush(aluno);
        acompanhamentoService.salvar(aluno, form.acompanhamento());
        return aluno.getIdAluno();
    }

    @Transactional
    public void excluir(Long id) {
        Aluno aluno = buscar(id);
        if (pesquisas.existsByAlunoIdAluno(id) || acompanhamentos.existsByAlunoIdAluno(id)
                || contratos.existsByAlunoIdAluno(id)) {
            throw new DomainException(null, "O aluno possui pesquisas, acompanhamentos ou contratos vinculados e não pode ser excluído.");
        }
        alunos.delete(aluno);
        alunos.flush();
    }
}
