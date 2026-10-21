package br.com.fatec.estagios.institucional.repository;

import br.com.fatec.estagios.institucional.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByNomeCursoContainingIgnoreCase(String nomeCurso);
}
