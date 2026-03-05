package com.Zeroloss2.Zeroloss2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
public class CalendarApiController {

    @GetMapping("/api/today")
    public Map<String, Integer> today() {

        int today = LocalDate.now().getDayOfMonth();

        return Map.of("today", today);
    }
}
