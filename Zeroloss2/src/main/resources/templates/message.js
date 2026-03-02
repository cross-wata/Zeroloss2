const phrases1 = ["ククク…", "愚かな人間よ", "闇に選ばれし者よ", "哀れな勇者よ"];
const phrases2 = ["絶望の", "混沌の", "呪われた", "終焉の"];
const phrases3 = ["一日が始まるのだ。", "運命からは逃れられぬ。", "影が忍び寄る。", "時が来た。"];

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