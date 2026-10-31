package br.com.fatec.estagios.institucional.controller;

import br.com.fatec.estagios.comum.DomainException;
import br.com.fatec.estagios.institucional.dto.CursoFormulario;
import br.com.fatec.estagios.institucional.service.CursoService;
import br.com.fatec.estagios.institucional.service.OfertaCursoService;

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
@RequestMapping("/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursos;
    private final OfertaCursoService ofertas;

    @InitBinder
    public void configurarBinding(WebDataBinder binder) {
        binder.setDeclarativeBinding(true);
    }

    @ModelAttribute
    public void configurarModulo(Model model) {
        model.addAttribute("modulo", "cursos");
    }

    // Lista todos os cursos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("cursos", cursos.listar());
        return "institucional/cursos/listagem";
    }

    // Abre o formulário de cadastro
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("form", CursoFormulario.novo());
        model.addAttribute("id", null);
        return "institucional/cursos/formulario";
    }

    // Abre o formulário de edição
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("form", cursos.formulario(id));
        model.addAttribute("id", id);
        return "institucional/cursos/formulario";
    }

    // Cadastra ou atualiza um curso
    @PostMapping({"", "/{id}"})
    public String salvar(
            @PathVariable(required = false) Long id,
            @Valid @ModelAttribute("form") CursoFormulario form,
            BindingResult result,
            Model model,
            RedirectAttributes flash) {

        if (!result.hasErrors()) {
            try {
                Long salvo = cursos.salvar(id, form);

                flash.addFlashAttribute(
                        "sucesso", "Curso salvo com sucesso."
                );

                return "redirect:/cursos/" + salvo + "/ofertas";

            } catch (DataIntegrityViolationException ex) {
                result.reject(
                        "conflito",
                        "Não foi possível salvar o curso."
                );
            }
        }

        model.addAttribute("id", id);
        return "institucional/cursos/formulario";
    }

    // Mostra as ofertas de um curso
    @GetMapping("/{id}/ofertas")
    public String listarOfertas(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("curso", cursos.buscar(id));
        model.addAttribute("ofertas", ofertas.listar(id));

        return "institucional/cursos/ofertas";
    }

    // Exclui um curso
    @PostMapping("/{id}/excluir")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes flash) {

        try {
            cursos.excluir(id);

            flash.addFlashAttribute(
                    "sucesso", "Curso excluído com sucesso."
            );

        } catch (DomainException ex) {
            flash.addFlashAttribute("erro", ex.getMessage());

        } catch (DataIntegrityViolationException ex) {
            flash.addFlashAttribute(
                    "erro", "O curso possui registros vinculados."
            );
        }

        return "redirect:/cursos";
    }
}