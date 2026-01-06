package com.adsentinel.presentation.web;

import com.adsentinel.application.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebDashboardController {

    private final DashboardService dashboardService;

    public WebDashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String index(Model model) {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("gmailStats", dashboardService.getGmailStats());
        model.addAttribute("adsStats", dashboardService.getAdsStats());
        return "dashboard";
    }
}
