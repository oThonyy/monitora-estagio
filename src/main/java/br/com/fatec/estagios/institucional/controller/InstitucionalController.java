package br.com.fatec.estagios.institucional.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InstitucionalController {
    @GetMapping("/unidades")
    public String unidades(Model model) {
        model.addAttribute("modulo", "unidades");
        return "institucional/unidades/listagem";
    }

    @GetMapping("/cursos")
    public String cursos(Model model) {
        model.addAttribute("modulo", "cursos");
        return "institucional/cursos/listagem";
    }
}
