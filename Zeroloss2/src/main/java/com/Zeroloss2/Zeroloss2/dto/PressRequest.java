package com.Zeroloss2.Zeroloss2.dto;

/*
 * PressRequest
 *
 * 役割：
 * フロントエンド（JavaScript）から送られてくる
 * データを受け取るためのクラス（DTO）
 *
 * DTO = Data Transfer Object
 * → API通信でデータをやり取りするためのオブジェクト
 *
 * このクラスは
 * /api/press のAPIが呼ばれたときに
 * フロント側から送られるデータを受け取るために使用されます。
 *
 * 例（フロントから送信されるJSON）
 *
 * {
 *   "day": 12,
 *   "message": "星々が優しくあなたを見守っています"
 * }
 *
 * SpringBootはこのJSONを
 * 自動的に PressRequest クラスに変換してくれます。
 *
 * 担当：A担当（バックエンド）
 */
public class PressRequest {

    /*
     * 押された日付
     *
     * フロントエンドのカレンダーで
     * ユーザーがクリックした日付を受け取る
     *
     * 例
     * day = 12
     */
    private int day;

    /*
     * 表示メッセージ
     *
     * フロント側で生成されたランダムメッセージを受け取る
     *
     * 例
     * "星々が優しくあなたを見守っています"
     */
    private String message;

    /*
     * day を取得するメソッド
     */
    public int getDay() {
        return day;
    }

    /*
     * day を設定するメソッド
     * SpringBootがJSONから値をセットするときに使われる
     */
    public void setDay(int day) {
        this.day = day;
    }

    /*
     * message を取得するメソッド
     */
    public String getMessage() {
        return message;
    }

    /*
     * message を設定するメソッド
     * JSONデータをこのクラスに変換するときに使用される
     */
    public void setMessage(String message) {
        this.message = message;
    }
}