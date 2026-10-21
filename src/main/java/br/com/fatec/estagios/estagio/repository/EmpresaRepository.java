package br.com.fatec.estagios.estagio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fatec.estagios.estagio.entity.Empresa;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByCnpj(String cnpj);

    boolean existsByCnpj(String cnpj);

    List<Empresa> findByRazaoSocialContainingIgnoreCase(String razaoSocial);
    
}
