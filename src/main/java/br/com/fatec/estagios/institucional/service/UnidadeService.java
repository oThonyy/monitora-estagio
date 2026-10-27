
package br.com.fatec.estagios.institucional.service;

import br.com.fatec.estagios.comum.DomainException;
import br.com.fatec.estagios.comum.RegistroNaoEncontradoException;
import br.com.fatec.estagios.institucional.dto.UnidadeFormulario;
import br.com.fatec.estagios.institucional.entity.UnidadeFatec;
import br.com.fatec.estagios.institucional.repository.OfertaCursoRepository;
import br.com.fatec.estagios.institucional.repository.UnidadeFatecRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnidadeService {

    private final UnidadeFatecRepository unidades;
    private final OfertaCursoRepository ofertas;

    public List<UnidadeFatec> listar() {
        return unidades.findAll(
            org.springframework.data.domain.Sort.by("nomeUnidade")
        );
    }

    public UnidadeFatec buscar(Long id) {
        return unidades.findById(id)
            .orElseThrow(RegistroNaoEncontradoException::new);
    }

    public UnidadeFormulario formulario(Long id) {
        UnidadeFatec unidade = buscar(id);

        return new UnidadeFormulario(
            unidade.getNomeUnidade(),
            unidade.getCep(),
            unidade.getLogradouro(),
            unidade.getMunicipio(),
            unidade.getUf()
        );
    }

    @Transactional
    public Long salvar(Long id, @Valid UnidadeFormulario form) {
        UnidadeFatec unidade = id == null
            ? new UnidadeFatec()
            : buscar(id);

        unidade.setNomeUnidade(form.nomeUnidade().strip());
        unidade.setCep(normalizar(form.cep()));
        unidade.setLogradouro(normalizar(form.logradouro()));
        unidade.setMunicipio(normalizar(form.municipio()));
        unidade.setUf(normalizar(form.uf()) == null
            ? null
            : form.uf().strip().toUpperCase());

        return unidades.save(unidade).getIdUnidadeFatec();
    }

    @Transactional
    public void excluir(Long id) {
        UnidadeFatec unidade = buscar(id);

        if (!ofertas.findByUnidadeFatecIdUnidadeFatec(id).isEmpty()) {
            throw new DomainException(
                null,
                "Esta unidade possui ofertas de curso vinculadas e não pode ser excluída."
            );
        }

        unidades.delete(unidade);
        unidades.flush();
    }

    private String normalizar(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.strip();
    }
}
