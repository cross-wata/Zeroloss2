package com.Zeroloss2.Zeroloss2.controller;

import com.Zeroloss2.Zeroloss2.service.CalendarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CalendarApiController {

    private final CalendarService service;

    public CalendarApiController(CalendarService service) {
        this.service = service;
    }

    @GetMapping("/api/today")
    public Map<String, Integer> today() {
        return Map.of("today", service.getToday());
    }

    @GetMapping("/api/status")
    public Map<String, Object> status() {
        return service.getStatus();
    }

    @PostMapping("/api/press")
    public Map<String, Object> press(@RequestParam int day) {
        return service.press(day);
    }
}