const phrases1 = [
    "ほしぼしが,",
    "つきのひかりが,",
    "もりのかぜが,",
    "せいれいたちが,",
    "あさひのかがやき,",
    "だいちのこどうが,",
    "てんのしゅくふくが,",
    "きぼうのともしびが,",
    "にじのきらめきが,",
    "きぼうのひかりが,"
];
const phrases2 = [
    "やさしく,",
    "しずかに,",
    "しんぴてきに,",
    "そっと,",
    "しゅくふくとともに,",
    "ひかりにつつまれ,",
    "きぼうをのせて,",
    "いのりをこめて,",
    "ぬくもりをたたえて,",
    "かがやきをまとい,"
];
const phrases3 = [
    "あなたをみまもっています。",
    "みちびいてくれるでしょう。",
    "あたらしいいちにちがはじまります。",
    "みらいへのとびらがひらきます。",
    "ちからをあたえてくれます。",
    "きせきをもたらします。",
    "やさしいひかりでつつみます。",
    "きぼうのみちをてらします。",
    "あなたのせなかをおします。",
    "こううんをはこんでくれます。"
];

function getRandomElement(arr) {
    return arr[Math.floor(Math.random() * arr.length)];
}

function createMessage() {
    return getRandomElement(phrases1) + "、" +
           getRandomElement(phrases2) + " " +
           getRandomElement(phrases3);
}

//API取得
let today;
let stage;
let lastMessage;
let canPressToday;

window.addEventListener("load", async () => {
//API呼び出し
    const response = await fetch("/api/status");
    const data = await response.json();

    today = data.today;
    stage = data.stage;
    lastMessage = data.lastMessage;
    canPressToday = data.canPressToday;

    // 前回メッセージ表示
    if(lastMessage){
        document.getElementById("message").textContent = lastMessage;
    }
    //画像表示
        document.getElementById("stageImage").src =
            `/images/stage${stage}.png`;

        // カレンダーボタン生成
        createCalendar();
    });

function createCalendar(){

    const calendar = document.getElementById("calendar");
    calendar.innerHTML = "";

    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth();

    // 月の日数取得
    const lastDate = new Date(year, month + 1, 0).getDate();

    // 1日の曜日取得
    const firstDay = new Date(year, month, 1).getDay();

    //　曜日表示
    const weekDays = ["日", "月", "火", "水", "木", "金", "土"];

    for(let i = 0; i < 7; i++){
    const dayLabel = document.createElement("div");
    dayLabel.textContent = weekDays[i];
    dayLabel.className = "weekday";

    calendar.appendChild(dayLabel);
}
    // 空白（曜日調整）ボタンを曜日ごとに並べる
    for(let i = 0; i < firstDay; i++){
        const empty = document.createElement("div");
        calendar.appendChild(empty);
    }

    // 日付ボタン生成
    for(let i = 1; i <= lastDate; i++){

        const button = document.createElement("button");
        button.textContent = i;
        button.className = "day";

        // 今日以外押せない
        if(i !== today || !canPressToday){
            button.disabled = true;
        }
        button.addEventListener("click", () => pressDay(i,button));

        calendar.appendChild(button);
    }
}

async function pressDay(day,button){

    const message = createMessage();

    const response = await fetch("/api/press",{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        body:JSON.stringify({
            day:day,
            message:message
        })
    });

    const data = await response.json();

    if(data.ok){

        // メッセージ更新
        document.getElementById("message").textContent = data.message;

        // ステージ画像更新
        document.getElementById("stageImage").src =
            `/images/stage${data.stage}.png`;

        // 今日のボタン無効化
        button.disabled = true;
    }
}
