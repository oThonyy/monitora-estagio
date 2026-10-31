
package br.com.fatec.estagios.institucional.service;

import br.com.fatec.estagios.comum.*;
import br.com.fatec.estagios.institucional.dto.OfertaCursoFormulario;
import br.com.fatec.estagios.institucional.entity.OfertaCurso;
import br.com.fatec.estagios.institucional.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OfertaCursoService {

    private final OfertaCursoRepository ofertas;
    private final CursoRepository cursos;
    private final UnidadeFatecRepository unidades;

    // Lista as ofertas de um curso
    public List<OfertaCurso> listar(Long idCurso) {
        return ofertas.findByCursoWithDetails(idCurso);
    }

    // Busca uma oferta pelo ID
    public OfertaCurso buscar(Long id) {
        return ofertas.findById(id)
                .orElseThrow(RegistroNaoEncontradoException::new);
    }

    // Preenche o formulário de edição
    public OfertaCursoFormulario formulario(Long id, Long idCurso) {
        OfertaCurso oferta = buscarDoCurso(id, idCurso);

        return new OfertaCursoFormulario(
                oferta.getUnidadeFatec().getIdUnidadeFatec(),
                oferta.getTurno(),
                oferta.getDuracao()
        );
    }

    // Confirma se a oferta pertence ao curso informado
    public OfertaCurso buscarDoCurso(Long id, Long idCurso) {
        OfertaCurso oferta = buscar(id);

        if (!oferta.getCurso().getIdCurso().equals(idCurso)) {
            throw new RegistroNaoEncontradoException();
        }

        return oferta;
    }

    // Cadastra ou atualiza uma oferta
    @Transactional
    public void salvar(Long id, Long idCurso, OfertaCursoFormulario form) {

        OfertaCurso oferta = id == null
                ? new OfertaCurso()
                : buscarDoCurso(id, idCurso);

        // Verifica se já existe a mesma oferta
        boolean duplicada =
                ofertas.existsByUnidadeFatecIdUnidadeFatecAndCursoIdCursoAndTurno(
                        form.idUnidadeFatec(),
                        idCurso,
                        form.turno()
                );

        if (duplicada && (
                id == null
                        || !oferta.getUnidadeFatec().getIdUnidadeFatec()
                        .equals(form.idUnidadeFatec())
                        || oferta.getTurno() != form.turno()
        )) {
            throw new DomainException(
                    null,
                    "Já existe uma oferta deste curso nessa unidade e turno."
            );
        }

        // Define o curso, unidade, turno e duração
        oferta.setCurso(
                cursos.findById(idCurso)
                        .orElseThrow(RegistroNaoEncontradoException::new)
        );

        oferta.setUnidadeFatec(
                unidades.findById(form.idUnidadeFatec())
                        .orElseThrow(RegistroNaoEncontradoException::new)
        );

        oferta.setTurno(form.turno());
        oferta.setDuracao(form.duracao());

        ofertas.saveAndFlush(oferta);
    }

    // Exclui uma oferta do curso
    @Transactional
    public void excluir(Long id, Long idCurso) {
        OfertaCurso oferta = buscarDoCurso(id, idCurso);

        ofertas.delete(oferta);
        ofertas.flush();
    }
}
