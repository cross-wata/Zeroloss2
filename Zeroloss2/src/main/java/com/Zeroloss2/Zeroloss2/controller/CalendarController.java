package com.Zeroloss2.Zeroloss2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalendarController {

    @GetMapping("/")
    public String index() {
        return "index"; // templates/index.html を表示
    }
}