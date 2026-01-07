# 🤖 SmartChatbot (Java Swing + Gemini API)

SmartChatbot is a **Java Swing–based desktop chatbot application** that integrates **Google Gemini API** for AI responses and **FreeTTS** for text‑to‑speech. It provides a clean GUI, real‑time chat interaction, and basic voice output support.

---

## ✨ Features

- 🖥️ Java Swing GUI (User‑friendly interface)
- 💬 AI‑powered responses using **Gemini 1.5 Flash API**
- 🔊 Text‑to‑Speech using **FreeTTS (Kevin16 voice)**
- 🎤 Microphone button (currently simulated input)
- ⚡ Multithreaded response handling (UI stays responsive)

---

## 🛠️ Technologies Used

- **Java (JDK 8+)**
- **Java Swing & AWT** – GUI
- **Google Gemini API** – AI responses
- **FreeTTS** – Text to Speech
- **org.json** – JSON parsing
- **HTTPURLConnection** – API communication

---

## 📂 Project Structure

```
SmartChatbot/
│── SmartChatbot.java
│── lib/
│   ├── freetts.jar
│   ├── json.jar
│── README.md
```

---

## ⚙️ Setup & Installation

### 1️⃣ Prerequisites

- Java JDK **8 or above**
- Internet connection
- Google Gemini API key

---

### 2️⃣ Required Libraries

Download and add these JAR files to your project classpath:

- **FreeTTS**  
  https://sourceforge.net/projects/freetts/

- **JSON Library (org.json)**  
  https://mvnrepository.com/artifact/org.json/json

---

### 3️⃣ Add Libraries to Project

**In IDE (NetBeans / IntelliJ / Eclipse):**

- Right click project → *Properties*
- Add JARs to **Libraries / Build Path**

---

### 4️⃣ API Key Configuration

Open `SmartChatbot.java` and replace the API key:

```java
public static String getApiKey() {
    return "YOUR_GEMINI_API_KEY";
}
```

⚠️ **Important:** Never expose your API key in public repositories.

---

## ▶️ How to Run

```bash
javac SmartChatbot.java
java SmartChatbot
```

Or simply run via your IDE.

---

## 🧠 How It Works

1. User types a message in the text field
2. Message is sent to **Gemini API** via HTTP POST
3. JSON response is parsed
4. Bot reply is displayed in chat area
5. FreeTTS speaks the response

---

## 🎤 Voice Input

- Current microphone button uses **simulated input**
- Can be extended with:
  - Java Sound API
  - Google Speech‑to‑Text

---

## 🚀 Future Improvements

- 🎙️ Real microphone input
- 🧠 Conversation memory
- 🌐 Offline fallback responses
- 🎨 Dark mode UI
- 🔐 Secure API key handling (env variables)

---

## 🧪 Sample Output

```
You: Hello
Bot: Hi! How can I help you today?
```

---

## 👨‍💻 Author

**Muhammad Daniyal**  
BSCS Student | Java & AI Enthusiast

---

## 📜 License

This project is for **educational purposes only**.
You are free to modify and use it for learning.

---

⭐ If you like this project, don’t forget to star the repo!

