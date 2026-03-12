package com.Zeroloss2.Zeroloss2.controller;

import com.Zeroloss2.Zeroloss2.dto.PressRequest;
import com.Zeroloss2.Zeroloss2.service.CalendarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CalendarApiController {

    private final CalendarService calendarService;

    public CalendarApiController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/api/status")
    public Map<String, Object> status() {
        return calendarService.status();
    }

    @PostMapping("/api/press")
    public Map<String, Object> press(@RequestBody PressRequest request) {
        return calendarService.press(request.getDay(), request.getMessage());
    }
}