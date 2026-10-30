
package br.com.fatec.estagios.institucional.controller;

import br.com.fatec.estagios.comum.DomainException;
import br.com.fatec.estagios.institucional.dto.UnidadeFormulario;
import br.com.fatec.estagios.institucional.service.UnidadeService;
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
@RequiredArgsConstructor
public class InstitucionalController {

    private final UnidadeService unidades;

    @InitBinder
    public void configurarBinding(WebDataBinder binder) {
        binder.setDeclarativeBinding(true);
    }

    @ModelAttribute
    public void configurarModulo(Model model) {
        model.addAttribute("modulo", "unidades");
    }

    @GetMapping("/unidades")
    public String unidades(Model model) {
        model.addAttribute("unidades", unidades.listar());
        return "institucional/unidades/listagem";
    }

    @GetMapping("/unidades/novo")
    public String novaUnidade(Model model) {
        model.addAttribute("form", UnidadeFormulario.novo());
        model.addAttribute("id", null);
        return "institucional/unidades/formulario";
    }

    @GetMapping("/unidades/{id}/editar")
    public String editarUnidade(@PathVariable Long id, Model model) {
        model.addAttribute("form", unidades.formulario(id));
        model.addAttribute("id", id);
        return "institucional/unidades/formulario";
    }

    @PostMapping({"/unidades", "/unidades/{id}"})
    public String salvarUnidade(
            @PathVariable(required = false) Long id,
            @Valid @ModelAttribute("form") UnidadeFormulario form,
            BindingResult result,
            Model model,
            RedirectAttributes flash) {

        if (result.hasErrors()) {
            model.addAttribute("id", id);
            return "institucional/unidades/formulario";
        }

        try {
            Long salvo = unidades.salvar(id, form);
            flash.addFlashAttribute("sucesso", "Unidade FATEC salva com sucesso.");
            return "redirect:/unidades";
        } catch (DataIntegrityViolationException ex) {
            result.reject(
                "conflito",
                "Não foi possível salvar a unidade. Confira os dados informados."
            );
            model.addAttribute("id", id);
            return "institucional/unidades/formulario";
        }
    }

    @PostMapping("/unidades/{id}/excluir")
    public String excluirUnidade(
            @PathVariable Long id,
            RedirectAttributes flash) {

        try {
            unidades.excluir(id);
            flash.addFlashAttribute("sucesso", "Unidade FATEC excluída com sucesso.");
        } catch (DomainException ex) {
            flash.addFlashAttribute("erro", ex.getMessage());
        } catch (DataIntegrityViolationException ex) {
            flash.addFlashAttribute(
                "erro",
                "A unidade possui registros vinculados e não pode ser excluída."
            );
        }

        return "redirect:/unidades";
    }

}
