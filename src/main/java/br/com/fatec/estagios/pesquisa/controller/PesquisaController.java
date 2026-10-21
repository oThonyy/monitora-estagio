package br.com.fatec.estagios.pesquisa.controller;

import br.com.fatec.estagios.pesquisa.dto.*;
import br.com.fatec.estagios.pesquisa.service.PesquisaService;
import br.com.fatec.estagios.aluno.service.AlunoService;
import br.com.fatec.estagios.comum.DomainException;
import br.com.fatec.estagios.comum.service.OpcoesFormularioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pesquisas")
@RequiredArgsConstructor
public class PesquisaController {
    private final PesquisaService service;
    private final AlunoService alunos;
    private final OpcoesFormularioService opcoes;

    @InitBinder
    public void configurarBinding(WebDataBinder binder) {
        binder.setDeclarativeBinding(true);
    }

    @ModelAttribute
    public void opcoes(Model model) {
        model.addAttribute("modulo", "pesquisas");
    }

    private void formulario(Model model, Long id) {
        model.addAttribute("id", id);
        model.addAttribute("alunos", opcoes.alunos());
    }

    @GetMapping
    public String listar(@ModelAttribute("filtro") PesquisaFiltro filtro, BindingResult result,
            @RequestParam(defaultValue = "0") int pagina, Model model) {
        if (filtro.inicio() != null && filtro.fim() != null && filtro.inicio().isAfter(filtro.fim())) {
            result.rejectValue("fim", "intervalo", "A data final deve ser igual ou posterior à inicial.");
        }
        model.addAttribute("pagina", result.hasErrors() ? Page.empty() : service.listar(filtro, pagina));
        model.addAttribute("alunos", opcoes.alunos());
        model.addAttribute("unidades", opcoes.unidades());
        model.addAttribute("cursos", opcoes.cursos());
        return "pesquisas/listagem";
    }

    @GetMapping("/nova")
    public String nova(@RequestParam(required = false) Long aluno, Model model) {
        if (aluno != null) {
            alunos.buscar(aluno);
        }
        model.addAttribute("form", PesquisaFormulario.nova(aluno));
        formulario(model, null);
        return "pesquisas/formulario";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("form", service.formulario(id));
        formulario(model, id);
        return "pesquisas/formulario";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        model.addAttribute("pesquisa", service.buscar(id));
        return "pesquisas/detalhes";
    }

    @PostMapping({"", "/{id}"})
    public String salvar(@PathVariable(required = false) Long id,
            @Valid @ModelAttribute("form") PesquisaFormulario form, BindingResult result,
            Model model, RedirectAttributes flash) {
        if (!result.hasErrors()) {
            try {
                Long salvo = service.salvar(id, form);
                flash.addFlashAttribute("sucesso", "Pesquisa salva com sucesso.");
                return "redirect:/pesquisas/" + salvo;
            } catch (DomainException ex) {
                result.rejectValue(ex.getCampo(), "regra", ex.getMessage());
            } catch (DataIntegrityViolationException ex) {
                result.reject("conflito", "Não foi possível salvar. Verifique se o aluno ainda existe.");
            }
        }
        formulario(model, id);
        return "pesquisas/formulario";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes flash) {
        service.excluir(id);
        flash.addFlashAttribute("sucesso", "Pesquisa excluída com sucesso.");
        return "redirect:/pesquisas";
    }
}
