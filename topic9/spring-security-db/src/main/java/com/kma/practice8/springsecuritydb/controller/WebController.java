package com.kma.practice8.springsecuritydb.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PreAuthorize("hasAuthority('VIEW_ADMIN') || hasAuthority('VIEW_CATALOG')")
    @GetMapping("/admin")
    public String admin() {
        return "admin_root";
    }

    @PreAuthorize("hasAuthority('VIEW_ADMIN')")
    @GetMapping("/admin/subpage")
    public String adminSubpage() {
        return "admin_sub";
    }

    @GetMapping("/catalog")
    public String catalog() {
        return "catalog";
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @GetMapping("/other")
    public String other() {
        return "other";
    }

    @PreAuthorize("hasAuthority('VIEW_ADMIN') || authentication.principal.companyId == #companyId")
    @GetMapping("/company/{companyId}/edit")
    public String editCompany(@PathVariable("companyId") int companyId) {
        return "other";
    }

}
