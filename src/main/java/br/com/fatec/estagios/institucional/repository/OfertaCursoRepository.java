package br.com.fatec.estagios.institucional.repository;

import br.com.fatec.estagios.comum.Turno;
import br.com.fatec.estagios.institucional.entity.OfertaCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OfertaCursoRepository extends JpaRepository<OfertaCurso, Long> {

    List<OfertaCurso> findByUnidadeFatecIdUnidadeFatec(Long idUnidadeFatec);

    List<OfertaCurso> findByCursoIdCurso(Long idCurso);

    boolean existsByUnidadeFatecIdUnidadeFatecAndCursoIdCursoAndTurno(Long idUnidadeFatec, Long idCurso, Turno turno);

    @Query("SELECT o FROM OfertaCurso o JOIN FETCH o.unidadeFatec JOIN FETCH o.curso")

    List<OfertaCurso> findAllWithDetails();

    @Query("SELECT o FROM OfertaCurso o JOIN FETCH o.unidadeFatec JOIN FETCH o.curso WHERE o.unidadeFatec.idUnidadeFatec = :idUnidade")
    
    List<OfertaCurso> findByUnidadeWithDetails(@Param("idUnidade") Long idUnidade);
}
