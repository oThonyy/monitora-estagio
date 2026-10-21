package br.com.fatec.estagios.aluno.repository;

import br.com.fatec.estagios.aluno.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long>, JpaSpecificationExecutor<Aluno> {

    @EntityGraph(attributePaths = {"ofertaCurso.curso", "ofertaCurso.unidadeFatec"})
    Page<Aluno> findAll(Specification<Aluno> specification, Pageable pageable);

    Optional<Aluno> findByRa(String ra);

    boolean existsByRa(String ra);

    List<Aluno> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT a FROM Aluno a JOIN FETCH a.ofertaCurso oc JOIN FETCH oc.unidadeFatec JOIN FETCH oc.curso")
    List<Aluno> findAllWithOferta();

    @Query("SELECT a FROM Aluno a JOIN FETCH a.ofertaCurso oc JOIN FETCH oc.unidadeFatec JOIN FETCH oc.curso WHERE a.ofertaCurso.idOfertaCurso = :idOferta")
    List<Aluno> findByOfertaCursoWithDetails(@Param("idOferta") Long idOferta);

    @Query("SELECT a FROM Aluno a JOIN FETCH a.ofertaCurso oc JOIN FETCH oc.unidadeFatec JOIN FETCH oc.curso WHERE oc.unidadeFatec.idUnidadeFatec = :idUnidade")
    List<Aluno> findByUnidadeWithDetails(@Param("idUnidade") Long idUnidade);

    @Query("SELECT a FROM Aluno a JOIN FETCH a.ofertaCurso oc JOIN FETCH oc.unidadeFatec JOIN FETCH oc.curso WHERE oc.curso.idCurso = :idCurso")
    List<Aluno> findByCursoWithDetails(@Param("idCurso") Long idCurso);
    
    @Query("SELECT a FROM Aluno a JOIN FETCH a.ofertaCurso oc JOIN FETCH oc.unidadeFatec JOIN FETCH oc.curso LEFT JOIN FETCH a.perfilEstudantil WHERE a.idAluno = :id")
    Optional<Aluno> findByIdWithDetails(@Param("id") Long id);
}
