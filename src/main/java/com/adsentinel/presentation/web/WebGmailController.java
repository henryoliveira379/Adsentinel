package com.adsentinel.presentation.web;

import com.adsentinel.application.dto.CreateGmailAccountCommand;
import com.adsentinel.application.service.GmailService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gmail")
public class WebGmailController {

    private final GmailService gmailService;

    public WebGmailController(GmailService gmailService) {
        this.gmailService = gmailService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("accounts", gmailService.findAll());
        model.addAttribute("command", new CreateGmailAccountCommand());
        return "gmail-list";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("command") CreateGmailAccountCommand command, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("accounts", gmailService.findAll());
            return "gmail-list";
        }
        
        // Force operator for web context (in a real app, this would come from SecurityContext)
        command.setOperatorName("OPERADOR_WEB");
        
        try {
            gmailService.createAccount(command);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("accounts", gmailService.findAll());
            return "gmail-list";
        }
        
        return "redirect:/gmail";
    }
}
