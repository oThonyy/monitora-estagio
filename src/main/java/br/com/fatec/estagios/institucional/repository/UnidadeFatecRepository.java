package br.com.fatec.estagios.institucional.repository;

import br.com.fatec.estagios.institucional.entity.UnidadeFatec;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UnidadeFatecRepository extends JpaRepository<UnidadeFatec, Long> {
    List<UnidadeFatec> findByNomeUnidadeContainingIgnoreCase(String nomeUnidade);
}
