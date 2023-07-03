package com.homework.springboot.featureNote.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
@RequestMapping("/auth")
@RequiredArgsConstructor
@Controller
public class AuthController {
    private final AuthService authService;
    @GetMapping("/profile")
    public ModelAndView get() {

        ModelAndView result = new ModelAndView("auth-page");
        result.addObject("userName", authService.getUsername());
        return result;
    }

    @GetMapping("/superadmin")
    public ModelAndView superAdminOnly() {

        if (!authService.hasAuthority("admin")) {
            return new ModelAndView("forbiden");
        }
        ModelAndView superadmin = new ModelAndView("superadmin");
        return superadmin.addObject("userName", authService.getUsername());


    }
}
