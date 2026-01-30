document.addEventListener("DOMContentLoaded", () => {

  const chatContainer = document.getElementById("chatContainer");
  const chatForm = document.getElementById("chatForm");
  const userInput = document.getElementById("userMessage");
  const newChatBtn = document.getElementById("newChatBtn");

  /* USER ID */
  const USER_ID = localStorage.getItem("userId");

  if (!USER_ID) {
    alert("User not logged in");
    window.location.href = "login.html";
    return;
  }

  /* BACKEND URL*/
  const SEND_MSG_URL = `/chat/${USER_ID}`;

  /* UI HELPER  */
  function addMessage(text, sender) {
    const msgDiv = document.createElement("div");
    msgDiv.className = `message ${sender}`;
    msgDiv.innerText = text;
    chatContainer.appendChild(msgDiv);
    chatContainer.scrollTop = chatContainer.scrollHeight;
  }

  /*  DEFAULT BOT MESSAGE */
  addMessage("Hi 👋 I’m MindCare. How are you feeling today?", "bot");

  /* NEW CHAT */
  newChatBtn.addEventListener("click", () => {
    chatContainer.innerHTML = "";
    addMessage("Hi 👋 I’m MindCare. How are you feeling today?", "bot");
  });

  /* SEND MESSAGE  */
  chatForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const message = userInput.value.trim();
    if (!message) return;

    addMessage(message, "user");
    userInput.value = "";

    try {
      const response = await fetch(SEND_MSG_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ message })
      });

      if (!response.ok) {
        throw new Error("Server error");
      }

      const data = await response.json();
      console.log("CHAT API RESPONSE:", data);

      addMessage(
        data.botReply ?? "I’m here with you 💙 Can you tell me a bit more?",
        "bot"
      );

    } catch (error) {
      console.error(error);
      addMessage("Sorry, something went wrong  Please try again.", "bot");
    }
  });

});
