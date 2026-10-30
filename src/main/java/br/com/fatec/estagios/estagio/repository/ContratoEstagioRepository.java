package br.com.fatec.estagios.estagio.repository;

import br.com.fatec.estagios.comum.SituacaoContrato;
import br.com.fatec.estagios.estagio.entity.ContratoEstagio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ContratoEstagioRepository extends JpaRepository<ContratoEstagio, Long>, JpaSpecificationExecutor<ContratoEstagio> {

    List<ContratoEstagio> findByAlunoIdAluno(Long idAluno);

    List<ContratoEstagio> findByEmpresaIdEmpresa(Long idEmpresa);

    List<ContratoEstagio> findBySituacaoContrato(SituacaoContrato situacao);

    long countBySituacaoContrato(SituacaoContrato situacao);

    @Query("SELECT c FROM ContratoEstagio c JOIN FETCH c.aluno a JOIN FETCH a.ofertaCurso oc JOIN FETCH oc.unidadeFatec JOIN FETCH oc.curso LEFT JOIN FETCH c.empresa")
    List<ContratoEstagio> findAllWithDetails();

    boolean existsByEmpresaIdEmpresa(Long idEmpresa);
    
    boolean existsByAlunoIdAluno(Long idAluno);

    @Query("SELECT c FROM ContratoEstagio c JOIN FETCH c.aluno a JOIN FETCH a.ofertaCurso oc JOIN FETCH oc.unidadeFatec JOIN FETCH oc.curso LEFT JOIN FETCH c.empresa WHERE c.idContratoEstagio = :id")
    Optional<ContratoEstagio> findByIdWithDetails(@Param("id") Long id);

    @EntityGraph(attributePaths = {"aluno.ofertaCurso.curso", "aluno.ofertaCurso.unidadeFatec", "empresa"})
    Page<ContratoEstagio> findAll(Specification<ContratoEstagio> specification, Pageable pageable);
}
