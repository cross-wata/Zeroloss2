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

const calendarDiv = document.getElementById("calendar");

for (let i = 1; i <= 31; i++) {
    const button = document.createElement("button");
    button.textContent = i + "日";

    button.addEventListener("click", function () {
        const message = createMessage();
        document.getElementById("message").textContent = message;
    });

    calendarDiv.appendChild(button);
}