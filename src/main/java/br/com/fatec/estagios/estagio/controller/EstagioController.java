package br.com.fatec.estagios.estagio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EstagioController {
    @GetMapping("/empresas")
    public String empresas(Model model) {
        model.addAttribute("modulo", "empresas");
        return "estagios/empresas/listagem";
    }
}
