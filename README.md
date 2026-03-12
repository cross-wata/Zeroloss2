# ZeroLoss

ZeroLossは、毎日1回ボタンを押すことでキャラクターが成長していく
RPG風カレンダーアプリです。

毎日継続して行動することを習慣化することを目的としています。

## アプリ画面

![アプリ画面](images/app.png)

## 主な機能

・31日カレンダーボタン表示  
・今日のボタンのみ押せる制御  
・ボタン押下でランダムメッセージ生成  
・メッセージをDBに保存  
・連続日数によるステージ成長  
・ページ再読み込み後も状態を保持

## 使用技術

・Java  
・Spring Boot  
・HTML  
・CSS  
・JavaScript  
・H2 Database  
・Git / GitHub

## システム構成

Frontend  
HTML / CSS / JavaScript

Backend  
Spring Boot (REST API)

Database  
H2 Database

## API

### 状態取得
GET /api/status

現在の状態を取得

例

{
  "today":12,
  "stage":1,
  "lastMessage":"メッセージ",
  "canPressToday":true
}

### ボタン押下
POST /api/press

送信

{
  "day":12,
  "message":"ランダムメッセージ"
}

## チーム開発

A担当（バックエンド）suzumerion
SpringBoot / API / DB

B担当（フロントロジック） cross-wata
JavaScript / カレンダー処理

C担当（UIデザイン）zero
HTML / CSS / 画面デザイン

## 工夫した点

・APIとフロントエンドを分離した構成にした
・日付によるボタン制御を実装
・連続日数によるキャラクター成長を実装
・ランダムメッセージ生成機能を追加
