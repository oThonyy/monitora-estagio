package br.com.fatec.estagios.comum.service;

import org.springframework.data.domain.Sort;
import br.com.fatec.estagios.aluno.entity.Aluno;
import br.com.fatec.estagios.aluno.repository.AlunoRepository;
import br.com.fatec.estagios.institucional.entity.*;
import br.com.fatec.estagios.institucional.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OpcoesFormularioService {
    private final AlunoRepository alunos;
    private final OfertaCursoRepository ofertas;
    private final UnidadeFatecRepository unidades;
    private final CursoRepository cursos;

    public List<Aluno> alunos() {
        return alunos.findAllWithOferta().stream().sorted(Comparator.comparing(Aluno::getNome)).toList();
    }

    public List<OfertaCurso> ofertas() {
        return ofertas.findAllWithDetails().stream().sorted(Comparator.comparing(o ->
            o.getUnidadeFatec().getNomeUnidade() + o.getCurso().getNomeCurso() + o.getTurno())).toList();
    }

    public List<UnidadeFatec> unidades() {
        return unidades.findAll(Sort.by("nomeUnidade"));
    }

    public List<Curso> cursos() {
        return cursos.findAll(Sort.by("nomeCurso"));
    }
}
