package br.com.fatec.estagios.aluno.controller;

import br.com.fatec.estagios.aluno.dto.*;
import br.com.fatec.estagios.aluno.service.AlunoService;
import br.com.fatec.estagios.pesquisa.service.AcompanhamentoService;
import br.com.fatec.estagios.comum.DomainException;
import br.com.fatec.estagios.comum.TipoEscola;
import br.com.fatec.estagios.comum.service.OpcoesFormularioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {
    private final AlunoService service;
    private final OpcoesFormularioService opcoes;
    private final AcompanhamentoService acompanhamentos;

    @InitBinder
    public void configurarBinding(WebDataBinder binder) {
        binder.setDeclarativeBinding(true);
    }

    @ModelAttribute
    public void opcoes(Model model) {
        model.addAttribute("modulo", "alunos");
    }

    private void formulario(Model model, Long id) {
        model.addAttribute("id", id);
        model.addAttribute("ofertas", opcoes.ofertas());
        model.addAttribute("tiposEscola", TipoEscola.values());
    }

    @GetMapping
    public String listar(@ModelAttribute("filtro") AlunoFiltro filtro, @RequestParam(defaultValue = "0") int pagina, Model model) {
        model.addAttribute("pagina", service.listar(filtro, pagina));
        model.addAttribute("unidades", opcoes.unidades());
        model.addAttribute("cursos", opcoes.cursos());
        model.addAttribute("ofertas", opcoes.ofertas());
        return "alunos/listagem";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("form", AlunoFormulario.novo());
        formulario(model, null);
        return "alunos/formulario";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, @RequestParam(required = false) String periodo, Model model) {
        model.addAttribute("form", service.formulario(id, periodo));
        formulario(model, id);
        return "alunos/formulario";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        model.addAttribute("aluno", service.buscar(id));
        model.addAttribute("acompanhamentos", acompanhamentos.listar(id));
        return "alunos/detalhes";
    }

    @PostMapping({"", "/{id}"})
    public String salvar(@PathVariable(required = false) Long id,
            @Valid @ModelAttribute("form") AlunoFormulario form, BindingResult result,
            Model model, RedirectAttributes flash) {
        if (!result.hasErrors()) {
            try {
                Long salvo = service.salvar(id, form);
                flash.addFlashAttribute("sucesso", "Aluno salvo com sucesso.");
                return "redirect:/alunos/" + salvo;
            } catch (DomainException ex) {
                result.rejectValue(ex.getCampo(), "regra", ex.getMessage());
            } catch (DataIntegrityViolationException ex) {
                result.reject("conflito", "Não foi possível salvar. Confira o RA, a oferta de curso e o semestre do acompanhamento acadêmico.");
            }
        }
        formulario(model, id);
        return "alunos/formulario";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes flash) {
        try {
            service.excluir(id);
            flash.addFlashAttribute("sucesso", "Aluno excluído com sucesso.");
            return "redirect:/alunos";
        } catch (DomainException ex) {
            flash.addFlashAttribute("erro", ex.getMessage());
        } catch (DataIntegrityViolationException ex) {
            flash.addFlashAttribute("erro", "O aluno possui registros vinculados e não pode ser excluído.");
        }
        return "redirect:/alunos/" + id;
    }
}
