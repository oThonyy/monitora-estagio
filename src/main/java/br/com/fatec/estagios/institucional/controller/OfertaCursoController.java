package br.com.fatec.estagios.institucional.controller;

import br.com.fatec.estagios.comum.DomainException;
import br.com.fatec.estagios.comum.Turno;
import br.com.fatec.estagios.institucional.dto.OfertaCursoFormulario;
import br.com.fatec.estagios.institucional.service.CursoService;
import br.com.fatec.estagios.institucional.service.OfertaCursoService;
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
@RequestMapping("/cursos/{idCurso}/ofertas")
@RequiredArgsConstructor
public class OfertaCursoController {

    private final OfertaCursoService ofertas;
    private final CursoService cursos;
    private final UnidadeService unidades;

    @InitBinder
    public void configurarBinding(WebDataBinder binder) {
        binder.setDeclarativeBinding(true);
    }

    @ModelAttribute
    public void configurarModulo(Model model) {
        model.addAttribute("modulo", "cursos");
    }

    // Prepara os dados do formulário
    private void prepararFormulario(
            Model model, Long idCurso, Long id) {

        model.addAttribute("curso", cursos.buscar(idCurso));
        model.addAttribute("unidades", unidades.listar());
        model.addAttribute("turnos", Turno.values());
        model.addAttribute("id", id);
    }

    // Abre o formulário de nova oferta
    @GetMapping("/nova")
    public String nova(
            @PathVariable Long idCurso,
            Model model) {

        model.addAttribute(
                "form", OfertaCursoFormulario.novo()
        );

        prepararFormulario(model, idCurso, null);

        return "institucional/cursos/oferta-formulario";
    }

    // Abre o formulário de edição
    @GetMapping("/{id}/editar")
    public String editar(
            @PathVariable Long idCurso,
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "form", ofertas.formulario(id, idCurso)
        );

        prepararFormulario(model, idCurso, id);

        return "institucional/cursos/oferta-formulario";
    }

    // Cadastra ou atualiza uma oferta
    @PostMapping({"", "/{id}"})
    public String salvar(
            @PathVariable Long idCurso,
            @PathVariable(required = false) Long id,
            @Valid @ModelAttribute("form") OfertaCursoFormulario form,
            BindingResult result,
            Model model,
            RedirectAttributes flash) {

        if (!result.hasErrors()) {
            try {
                ofertas.salvar(id, idCurso, form);

                flash.addFlashAttribute(
                        "sucesso", "Oferta salva com sucesso."
                );

                return "redirect:/cursos/" + idCurso + "/ofertas";

            } catch (DomainException ex) {
                result.reject("regra", ex.getMessage());

            } catch (DataIntegrityViolationException ex) {
                result.reject(
                        "conflito",
                        "Oferta duplicada ou com dados inválidos."
                );
            }
        }

        prepararFormulario(model, idCurso, id);

        return "institucional/cursos/oferta-formulario";
    }

    // Exclui uma oferta
    @PostMapping("/{id}/excluir")
    public String excluir(
            @PathVariable Long idCurso,
            @PathVariable Long id,
            RedirectAttributes flash) {

        try {
            ofertas.excluir(id, idCurso);

            flash.addFlashAttribute(
                    "sucesso", "Oferta excluída com sucesso."
            );

        } catch (DataIntegrityViolationException ex) {
            flash.addFlashAttribute(
                    "erro",
                    "A oferta possui registros vinculados."
            );
        }

        return "redirect:/cursos/" + idCurso + "/ofertas";
    }
}