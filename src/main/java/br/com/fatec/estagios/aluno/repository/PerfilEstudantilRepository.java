package br.com.fatec.estagios.aluno.repository;

import br.com.fatec.estagios.aluno.entity.PerfilEstudantil;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PerfilEstudantilRepository extends JpaRepository<PerfilEstudantil, Long> {

    Optional<PerfilEstudantil> findByAlunoIdAluno(Long idAluno);
    
    boolean existsByAlunoIdAluno(Long idAluno);
}
