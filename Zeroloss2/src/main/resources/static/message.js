const phrases1 = [
    "星々が",
    "月の光が",
    "森の風が",
    "精霊たちが",
    "朝日の輝きが",
    "大地の鼓動が",
    "天の祝福が",
    "希望の灯火が",
    "虹のきらめきが",
    "蒼穹の光が"
];
const phrases2 = [
    "優しく",
    "静かに",
    "神秘的に",
    "そっと",
    "祝福とともに",
    "光に包まれ",
    "希望を乗せて",
    "祈りを込めて",
    "温もりをたたえて",
    "輝きをまとい"
];
const phrases3 = [
    "あなたを見守っています。",
    "導いてくれるでしょう。",
    "新しい一日が始まります。",
    "未来への扉を開きます。",
    "力を与えてくれます。",
    "奇跡をもたらします。",
    "優しい光で包み込みます。",
    "希望の道を照らします。",
    "あなたの背中を押します。",
    "幸運を運んでくれます。"
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

    for(let i=1;i<=31;i++){

        const button = document.createElement("button");
        button.textContent = i;
        button.className = "day";

        // 今日以外押せない
        if(i !== today || !canPressToday){
            button.disabled = true;
        }

        // ボタン押した時
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