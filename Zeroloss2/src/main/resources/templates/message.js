const phrases1 = ["おはよう", "こんにちは", "こんばんは"];
const phrases2 = ["良い", "悪い", "不思議な"];
const phrases3 = ["一日でした。", "日でした。", "夢でした。"];

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