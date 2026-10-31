package br.com.fatec.estagios.empresa.service;

import br.com.fatec.estagios.comum.DomainException;
import br.com.fatec.estagios.comum.RegistroNaoEncontradoException;
import br.com.fatec.estagios.empresa.dto.EmpresaFormulario;
import br.com.fatec.estagios.estagio.entity.Empresa;
import br.com.fatec.estagios.estagio.repository.ContratoEstagioRepository;
import br.com.fatec.estagios.estagio.repository.EmpresaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

@Service
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmpresaService {

    private final EmpresaRepository empresas;
    private final ContratoEstagioRepository contratos;

    public List<Empresa> listar() {
        return empresas.findAll(Sort.by(Sort.Direction.ASC, "razaoSocial"));
    }

    public Empresa buscar(Long id) {
        return empresas.findById(id)
            .orElseThrow(RegistroNaoEncontradoException::new);
    }

    public EmpresaFormulario formulario(Long id) {
        Empresa empresa = buscar(id);

        return new EmpresaFormulario(
            empresa.getCnpj(),
            empresa.getRazaoSocial(),
            empresa.getNomeFantasia(),
            empresa.getSegmentoMercado(),
            empresa.getAreaAtuacaoEmpresa(),
            empresa.getEmail(),
            empresa.getCepEmpresa(),
            empresa.getEndereco(),
            empresa.getLinkedin()
        );
    }

    @Transactional
    public Long salvar(Long id, @Valid EmpresaFormulario form) {
        Empresa empresa = id == null ? new Empresa() : buscar(id);
        String cnpj = normalizarCnpj(form.cnpj());

        if (cnpj != null) {
            empresas.findByCnpj(cnpj)
                .filter(cadastrada -> !Objects.equals(cadastrada.getIdEmpresa(), id))
                .ifPresent(cadastrada -> {
                    throw new DomainException("cnpj", "Já existe uma empresa cadastrada com este CNPJ.");
                });
        }

        empresa.setCnpj(cnpj);
        empresa.setRazaoSocial(form.razaoSocial().strip());
        empresa.setNomeFantasia(normalizar(form.nomeFantasia()));
        empresa.setSegmentoMercado(normalizar(form.segmentoMercado()));
        empresa.setAreaAtuacaoEmpresa(normalizar(form.areaAtuacaoEmpresa()));
        empresa.setEmail(normalizar(form.email()));
        empresa.setCepEmpresa(normalizar(form.cepEmpresa()));
        empresa.setEndereco(normalizar(form.endereco()));
        empresa.setLinkedin(normalizar(form.linkedin()));

        return empresas.save(empresa).getIdEmpresa();
    }

    @Transactional
    public void excluir(Long id) {
        Empresa empresa = buscar(id);

        if (contratos.existsByEmpresaIdEmpresa(id)) {
            throw new DomainException(
                null,
                "Esta empresa possui contratos de estágio vinculados e não pode ser excluída."
            );
        }

        empresas.delete(empresa);
        empresas.flush();
    }

    private String normalizarCnpj(String valor) {
        String cnpj = normalizar(valor);
        if (cnpj == null) {
            return null;
        }

        // Padroniza CNPJs informados com ou sem pontuação para evitar duplicidades de formato.
        String numeros = cnpj.replaceAll("\\D", "");
        if (numeros.length() == 14) {
            return numeros.substring(0, 2) + "." + numeros.substring(2, 5) + "."
                + numeros.substring(5, 8) + "/" + numeros.substring(8, 12) + "-"
                + numeros.substring(12, 14);
        }
        return cnpj;
    }

    private String normalizar(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.strip();
    }
}
