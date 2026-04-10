function createStreakText(streak) {
    return "【 " + streak + "日れんぞくログイン達成 】";
}

function createPhrases1(streak){
    return "【 " + streak + "日目 】";
}

const phrases2 = [
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

const phrases3 = [
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

const phrases4 = [
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

function createMessage(streak) {
    return createPhrases1(streak) + " " +   // ← +1削除
        getRandomElement(phrases2) + " " +
        getRandomElement(phrases3) + " " +
        getRandomElement(phrases4);
}

// APIデータ
let today;
let stage;
let lastMessage;
let canPressToday;
let streakCount;

window.addEventListener("load", async () => {
    try {
        const response = await fetch("/api/status");
        const data = await response.json();
        console.log(data);

        today = Number(data.today); // ★ 型対策
        stage = data.stage;
        lastMessage = data.lastMessage;
        canPressToday = data.canPressToday;
        streakCount = data.streakCount;

        if(lastMessage){
            document.getElementById("message").textContent = lastMessage;
        }

        // ★ 「画面を開いた瞬間」 の更新　suzume
        document.getElementById("stageImage").src =
            `/images/stage${data.stage}.png`;
            streakCount = data.streakCount;

// 背景画像切り替え
        const bgImage = document.getElementById("bgImage");

        const now = new Date();
        const month = now.getMonth() + 1; // 1〜12にする

        let bgSrc = "../images/image2.png";

        if(month >= 3 && month <= 5){
            bgSrc = "../images/image2.png";
        }else if(month >= 6 && month <= 8){
            bgSrc = "../images/image3.png";
        }else if(month >= 9 && month <= 11){
            bgSrc = "../images/image4.png";
        }else{
            bgSrc = "../images/image5.png";
        }

        if(bgImage){
            bgImage.src = bgSrc;
        }

        createCalendar();

    } catch (e) {
        console.error("APIエラー:", e);
    }
});

function createCalendar(){

    const calendar = document.getElementById("calendar");
    calendar.innerHTML = "";

    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth();

    const lastDate = new Date(year, month + 1, 0).getDate();
    const firstDay = new Date(year, month, 1).getDay();

    // 月表示
    const monthElement = document.getElementById("month");
    if(monthElement){
        monthElement.textContent = (month + 1) + "月";
    }

    // 曜日表示
    const weekDays = ["日", "月", "火", "水", "木", "金", "土"];

    const week = document.getElementById("week");
    if(!week) return;
    week.innerHTML = ""; // ★ リセット

    for(let i = 0; i < 7; i++){
        const dayLabel = document.createElement("div");
        dayLabel.textContent = weekDays[i];
        dayLabel.className = "week-day";
        week.appendChild(dayLabel);
    }

    // 空白
    for(let i = 0; i < firstDay; i++){
        const empty = document.createElement("div");
        calendar.appendChild(empty);
    }

    // 日付ボタン
    for(let i = 1; i <= lastDate; i++){

        const button = document.createElement("button");
        button.textContent = i;
        button.className = "day";

        if(i !== today || !canPressToday){
            button.disabled = true;
        }

        button.addEventListener("click", () => pressDay(i, button));

        calendar.appendChild(button);
    }
}

async function pressDay(day, button){

    button.disabled = true; // 先に無効化

    const message =
        getRandomElement(phrases2) + " " +
        getRandomElement(phrases3) + " " +
        getRandomElement(phrases4);

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
        document.getElementById("message").textContent = data.message;

        document.getElementById("stageImage").src =
            `/images/stage${data.stage}.png`;
    }
}
//BGM
document.addEventListener("click", () => {
    const bgm = document.getElementById("bgm");
    bgm.volume = 0.2;
    bgm.play();
}, { once: true });