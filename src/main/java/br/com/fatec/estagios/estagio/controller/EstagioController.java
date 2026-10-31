package br.com.fatec.estagios.estagio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EstagioController {

    @GetMapping("/contratos")
    public String contratos(Model model) {
        model.addAttribute("modulo", "contratos");
        return "estagios/contratos/listagem";
    }
}
