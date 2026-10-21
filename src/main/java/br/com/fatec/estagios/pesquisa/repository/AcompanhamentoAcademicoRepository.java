package br.com.fatec.estagios.pesquisa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.fatec.estagios.pesquisa.entity.AcompanhamentoAcademico;

import java.util.List;
import java.util.Optional;

public interface AcompanhamentoAcademicoRepository extends JpaRepository<AcompanhamentoAcademico, Long> {

    boolean existsByAlunoIdAluno(Long idAluno);

    List<AcompanhamentoAcademico> findByAlunoIdAlunoOrderByIdAcompanhamentoDesc(Long idAluno);

    Optional<AcompanhamentoAcademico> findByAlunoIdAlunoAndPeriodoLetivo(Long idAluno, String periodoLetivo);

    boolean existsByAlunoIdAlunoAndPeriodoLetivo(Long idAluno, String periodoLetivo);
    
    @Query("SELECT a FROM AcompanhamentoAcademico a JOIN FETCH a.aluno al JOIN FETCH al.ofertaCurso oc JOIN FETCH oc.unidadeFatec JOIN FETCH oc.curso")
    List<AcompanhamentoAcademico> findAllWithDetails();
}
