package com.telekom.datacorona.server.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

@Controller
@RequestMapping("")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class GraphController {

    @RequestMapping
    public String graphsSlovakiaVaccinations() {
        return "main";
    }

}
