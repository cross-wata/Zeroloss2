package com.Zeroloss2.Zeroloss2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * CalendarController
 *
 * 役割：
 * 画面表示用のController
 *
 * Spring Bootでは
 * Controller がブラウザからのリクエストを受け取り
 * どのHTMLを表示するかを決めます。
 *
 * このControllerは
 * 「トップページ("/")にアクセスされたとき」
 * カレンダー画面（index.html）を表示する役割です。
 *
 * 担当：A担当（バックエンド）
 */

@Controller
public class CalendarController {

    /*
     * "/" にアクセスされたときに実行されるメソッド
     *
     * 例：
     * http://localhost:8080/
     *
     * return "index"
     * → templates/index.html を表示する
     *
     * Spring Bootでは
     * templatesフォルダのHTMLを
     * 文字列で指定して表示します。
     */

    @GetMapping("/")
    public String index() {

        // index.html を表示

        return "index"; // resources/templates/index.html
    }
}