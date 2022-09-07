package com.telekom.datacorona.server.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestControllerPzi {
    @RequestMapping("/testmap")
    public String mapPage() {
        return "mainpzi";
    }

}
