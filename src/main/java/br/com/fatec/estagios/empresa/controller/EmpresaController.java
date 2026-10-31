package br.com.fatec.estagios.empresa.controller;

import br.com.fatec.estagios.comum.DomainException;
import br.com.fatec.estagios.empresa.dto.EmpresaFormulario;
import br.com.fatec.estagios.empresa.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresas;

    @InitBinder
    public void configurarBinding(WebDataBinder binder) {
        binder.setDeclarativeBinding(true);
    }

    @ModelAttribute
    public void configurarModulo(Model model) {
        model.addAttribute("modulo", "empresas");
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("empresas", empresas.listar());
        return "estagios/empresas/listagem";
    }

    @GetMapping("/novo")
    public String nova(Model model) {
        model.addAttribute("form", EmpresaFormulario.novo());
        model.addAttribute("id", null);
        return "estagios/empresas/formulario";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        model.addAttribute("empresa", empresas.buscar(id));
        return "estagios/empresas/detalhes";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("form", empresas.formulario(id));
        model.addAttribute("id", id);
        return "estagios/empresas/formulario";
    }

    @PostMapping({"", "/{id}"})
    public String salvar(
            @PathVariable(required = false) Long id,
            @Valid @ModelAttribute("form") EmpresaFormulario form,
            BindingResult result,
            Model model,
            RedirectAttributes flash) {

        if (!result.hasErrors()) {
            try {
                Long salvo = empresas.salvar(id, form);
                flash.addFlashAttribute("sucesso", "Empresa salva com sucesso.");
                return "redirect:/empresas/" + salvo;
            } catch (DomainException ex) {
                if (ex.getCampo() == null) {
                    result.reject("regra", ex.getMessage());
                } else {
                    result.rejectValue(ex.getCampo(), "regra", ex.getMessage());
                }
            } catch (DataIntegrityViolationException ex) {
                result.reject("conflito", "Não foi possível salvar a empresa. Confira o CNPJ e os dados informados.");
            }
        }

        model.addAttribute("id", id);
        return "estagios/empresas/formulario";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes flash) {
        try {
            empresas.excluir(id);
            flash.addFlashAttribute("sucesso", "Empresa excluída com sucesso.");
            return "redirect:/empresas";
        } catch (DomainException ex) {
            flash.addFlashAttribute("erro", ex.getMessage());
        } catch (DataIntegrityViolationException ex) {
            flash.addFlashAttribute("erro", "A empresa possui registros vinculados e não pode ser excluída.");
        }
        return "redirect:/empresas/" + id;
    }
}
