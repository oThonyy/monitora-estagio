
package br.com.fatec.estagios.institucional.service;

import br.com.fatec.estagios.comum.*;
import br.com.fatec.estagios.institucional.dto.CursoFormulario;
import br.com.fatec.estagios.institucional.entity.Curso;
import br.com.fatec.estagios.institucional.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CursoService {

    private final CursoRepository cursos;
    private final OfertaCursoRepository ofertas;

    // Lista todos os cursos em ordem alfabética
    public List<Curso> listar() {
        return cursos.findAll(Sort.by("nomeCurso"));
    }

    // Busca um curso pelo ID
    public Curso buscar(Long id) {
        return cursos.findById(id)
                .orElseThrow(RegistroNaoEncontradoException::new);
    }

    // Preenche o formulário ao editar um curso
    public CursoFormulario formulario(Long id) {
        return new CursoFormulario(buscar(id).getNomeCurso());
    }

    // Cadastra ou atualiza um curso
    @Transactional
    public Long salvar(Long id, CursoFormulario form) {
        Curso curso = id == null ? new Curso() : buscar(id);

        curso.setNomeCurso(form.nomeCurso().strip());

        return cursos.save(curso).getIdCurso();
    }

    // Exclui um curso, desde que não tenha ofertas
    @Transactional
    public void excluir(Long id) {
        Curso curso = buscar(id);

        if (!ofertas.findByCursoIdCurso(id).isEmpty()) {
            throw new DomainException(
                    null,
                    "Exclua as ofertas do curso antes de excluí-lo."
            );
        }

        cursos.delete(curso);
        cursos.flush();
    }
}
