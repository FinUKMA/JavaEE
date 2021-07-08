package com.kma.practice8.springsecuritycustom.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @PreAuthorize("hasAuthority('VIEW_ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "admin_string";
    }

    @PreAuthorize("hasAuthority('VIEW_CATALOG')")
    @GetMapping("/catalog")
    public ResponseEntity<String> catalog() {
        return ResponseEntity.ok("catalog");
    }

    @GetMapping("/other")
    public ResponseEntity<String> other() {
        return ResponseEntity.ok("other");
    }

    @PreAuthorize("hasAuthority('VIEW_ADMIN') || authentication.principal.companyAlias == #cAlias")
    @GetMapping("/company/{cAlias}")
    public ResponseEntity<String> companyDetail(@PathVariable("cAlias") final String cAlias) {
        return ResponseEntity.ok("asd");
    }

}
