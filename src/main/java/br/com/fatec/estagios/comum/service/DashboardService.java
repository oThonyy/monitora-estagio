package br.com.fatec.estagios.comum.service;

import br.com.fatec.estagios.aluno.repository.AlunoRepository;
import br.com.fatec.estagios.comum.SituacaoContrato;
import br.com.fatec.estagios.estagio.repository.*;
import br.com.fatec.estagios.institucional.repository.*;
import br.com.fatec.estagios.pesquisa.repository.PesquisaEstagioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {
    private final AlunoRepository alunos;
    private final CursoRepository cursos;
    private final EmpresaRepository empresas;
    private final ContratoEstagioRepository contratos;
    private final PesquisaEstagioRepository pesquisas;

    public Map<String, Long> indicadores() {
        Map<String, Long> indicadores = new LinkedHashMap<>();
        indicadores.put("Alunos cadastrados", alunos.count());
        indicadores.put("Pesquisas registradas", pesquisas.count());
        indicadores.put("Cursos", cursos.count());
        indicadores.put("Empresas", empresas.count());
        indicadores.put("Contratos ativos", contratos.countBySituacaoContrato(SituacaoContrato.ATIVO));
        return indicadores;
    }
}
