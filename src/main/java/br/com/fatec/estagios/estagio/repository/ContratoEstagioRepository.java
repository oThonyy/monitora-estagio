package br.com.fatec.estagios.estagio.repository;

import br.com.fatec.estagios.comum.SituacaoContrato;
import br.com.fatec.estagios.estagio.entity.ContratoEstagio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ContratoEstagioRepository extends JpaRepository<ContratoEstagio, Long> {

    List<ContratoEstagio> findByAlunoIdAluno(Long idAluno);

    List<ContratoEstagio> findByEmpresaIdEmpresa(Long idEmpresa);

    List<ContratoEstagio> findBySituacaoContrato(SituacaoContrato situacao);

    long countBySituacaoContrato(SituacaoContrato situacao);

    @Query("SELECT c FROM ContratoEstagio c JOIN FETCH c.aluno a JOIN FETCH a.ofertaCurso oc JOIN FETCH oc.unidadeFatec JOIN FETCH oc.curso JOIN FETCH c.empresa")
    List<ContratoEstagio> findAllWithDetails();

    boolean existsByEmpresaIdEmpresa(Long idEmpresa);
    
    boolean existsByAlunoIdAluno(Long idAluno);
}
