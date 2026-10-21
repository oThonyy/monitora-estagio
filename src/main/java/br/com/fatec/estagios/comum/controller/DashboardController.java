package br.com.fatec.estagios.comum.controller;

import br.com.fatec.estagios.comum.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService service;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("modulo", "dashboard");
        model.addAttribute("indicadores", service.indicadores());
        return "dashboard/index";
    }
}
