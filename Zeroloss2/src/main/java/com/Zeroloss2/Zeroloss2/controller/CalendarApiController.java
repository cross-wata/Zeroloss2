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
    // Serviceクラスを使うための宣言
    // 実際の処理（状態確認・ボタン押下処理）はServiceに任せる
    private final CalendarService calendarService;
    // コンストラクタでCalendarServiceを受け取る
    // Springが自動でServiceを注入してくれる
    public CalendarApiController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }
    // GET /api/status にアクセスされたときの処理
    // 現在のアプリ状態（例：日付やメッセージの状態）を取得する
    @GetMapping("/api/status")
    public Map<String, Object> status() {
        return calendarService.status();
    }
    // POST /api/press にアクセスされたときの処理
    // フロント側から送られてきた day と message を受け取り、
    // Serviceに渡してボタン押下時の処理を実行する
    @PostMapping("/api/press")
    public Map<String, Object> press(@RequestBody PressRequest request) {
        return calendarService.press(request.getDay(), request.getMessage());
    }
}