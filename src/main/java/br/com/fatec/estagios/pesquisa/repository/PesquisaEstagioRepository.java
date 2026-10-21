package br.com.fatec.estagios.pesquisa.repository;

import br.com.fatec.estagios.pesquisa.entity.PesquisaEstagio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PesquisaEstagioRepository extends JpaRepository<PesquisaEstagio, Long>, JpaSpecificationExecutor<PesquisaEstagio> {
    
    List<PesquisaEstagio> findByAlunoIdAluno(Long idAluno);

    @Query("SELECT p FROM PesquisaEstagio p JOIN FETCH p.aluno a JOIN FETCH a.ofertaCurso oc JOIN FETCH oc.unidadeFatec JOIN FETCH oc.curso")
    List<PesquisaEstagio> findAllWithDetails();

    @Query("SELECT p FROM PesquisaEstagio p JOIN FETCH p.aluno a JOIN FETCH a.ofertaCurso oc JOIN FETCH oc.unidadeFatec JOIN FETCH oc.curso WHERE p.dataColeta BETWEEN :inicio AND :fim")
    List<PesquisaEstagio> findByDataColetaBetween(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    boolean existsByAlunoIdAluno(Long idAluno);

    @Query("SELECT p FROM PesquisaEstagio p JOIN FETCH p.aluno a JOIN FETCH a.ofertaCurso oc JOIN FETCH oc.unidadeFatec JOIN FETCH oc.curso WHERE p.idPesquisaEstagio = :id")
    Optional<PesquisaEstagio> findByIdWithDetails(@Param("id") Long id);

    @EntityGraph(attributePaths = {"aluno.ofertaCurso.curso", "aluno.ofertaCurso.unidadeFatec"})
    Page<PesquisaEstagio> findAll(Specification<PesquisaEstagio> specification, Pageable pageable);
}
