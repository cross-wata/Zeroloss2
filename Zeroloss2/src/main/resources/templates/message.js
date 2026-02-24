const phrases1 = ["おはよう", "こんにちは", "こんばんは"];
const phrases2 = ["良い", "悪い", "不思議な"];
const phrases3 = ["一日でした。", "日でした。", "夢でした。"];

function getRandomElement(arr) {
    return arr[Math.floor(Math.random() * arr.length)];
}

const message = getRandomElement(phrases1) + "、" + getRandomElement(phrases2) + " " + getRandomElement(phrases3);

console.log(message);
