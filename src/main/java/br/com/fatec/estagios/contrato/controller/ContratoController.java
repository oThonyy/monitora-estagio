package br.com.fatec.estagios.contrato.controller;

import br.com.fatec.estagios.aluno.service.AlunoService;
import br.com.fatec.estagios.comum.DomainException;
import br.com.fatec.estagios.comum.ModalidadeTrabalho;
import br.com.fatec.estagios.comum.SituacaoContrato;
import br.com.fatec.estagios.comum.TipoContrato;
import br.com.fatec.estagios.comum.service.OpcoesFormularioService;
import br.com.fatec.estagios.contrato.dto.ContratoFiltro;
import br.com.fatec.estagios.contrato.dto.ContratoFormulario;
import br.com.fatec.estagios.estagio.service.EstagioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/contratos")
@RequiredArgsConstructor
public class ContratoController {

    private final EstagioService service;
    private final AlunoService alunos;
    private final OpcoesFormularioService opcoes;

    @InitBinder
    public void configurarBinding(WebDataBinder binder) {
        binder.setDeclarativeBinding(true);
    }

    @ModelAttribute
    public void opcoes(Model model) {
        model.addAttribute("modulo", "contratos");
    }

    private void formulario(Model model, Long id) {
        model.addAttribute("id", id);
        model.addAttribute("alunos", opcoes.alunos());
        model.addAttribute("empresas", opcoes.empresas());
        model.addAttribute("tiposContrato", TipoContrato.values());
        model.addAttribute("modalidades", ModalidadeTrabalho.values());
        model.addAttribute("situacoes", List.of(SituacaoContrato.ATIVO, SituacaoContrato.ENCERRADO));
    }

    @GetMapping
    public String listar(@ModelAttribute("filtro") ContratoFiltro filtro,
                         @RequestParam(defaultValue = "0") int pagina,
                         Model model) {
        model.addAttribute("pagina", service.listar(filtro, pagina));
        model.addAttribute("alunos", opcoes.alunos());
        model.addAttribute("empresas", opcoes.empresas());
        model.addAttribute("tiposContrato", TipoContrato.values());
        model.addAttribute("modalidades", ModalidadeTrabalho.values());
        model.addAttribute("situacoes", List.of(SituacaoContrato.ATIVO, SituacaoContrato.ENCERRADO));
        return "estagios/contratos/listagem";
    }

    @GetMapping("/novo")
    public String novo(@RequestParam(required = false) Long aluno, Model model) {
        if (aluno != null) {
            alunos.buscar(aluno);
        }
        model.addAttribute("form", ContratoFormulario.novo(aluno));
        formulario(model, null);
        return "estagios/contratos/formulario";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("form", service.formulario(id));
        formulario(model, id);
        return "estagios/contratos/formulario";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        model.addAttribute("contrato", service.buscar(id));
        return "estagios/contratos/detalhes";
    }

    @PostMapping({"", "/{id}"})
    public String salvar(@PathVariable(required = false) Long id,
                         @Valid @ModelAttribute("form") ContratoFormulario form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes flash) {
        if (!result.hasErrors()) {
            try {
                Long salvo = service.salvar(id, form);
                flash.addFlashAttribute("sucesso", "Contrato salvo com sucesso.");
                return "redirect:/contratos/" + salvo;
            } catch (DomainException ex) {
                result.rejectValue(ex.getCampo(), "regra", ex.getMessage());
            } catch (DataIntegrityViolationException ex) {
                result.reject("conflito", "Não foi possível salvar o contrato. Verifique os dados informados.");
            }
        }
        formulario(model, id);
        return "estagios/contratos/formulario";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes flash) {
        try {
            service.excluir(id);
            flash.addFlashAttribute("sucesso", "Contrato excluído com sucesso.");
            return "redirect:/contratos";
        } catch (DomainException ex) {
            flash.addFlashAttribute("erro", ex.getMessage());
        } catch (DataIntegrityViolationException ex) {
            flash.addFlashAttribute("erro", "O contrato possui registros vinculados e não pode ser excluído.");
        }
        return "redirect:/contratos/" + id;
    }
}
