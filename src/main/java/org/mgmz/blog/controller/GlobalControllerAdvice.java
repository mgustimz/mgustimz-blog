package org.mgmz.blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addGlobalAttributes(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURL().toString());
    }
}