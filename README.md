# TwinMind

TwinMind is a native Android application designed to record meetings, generate transcriptions, and create AI-powered summaries all while running seamlessly in the background.

---

## What It Does

### Background Recording
- Records audio even when the screen is locked  
- Uses a **foreground service** with a persistent notification  
- Displays **live recording duration** in real time  

### Transcription
- Processes recorded audio in chunks  
- Converts speech to text efficiently  
- Stores all transcripts **locally using Room Database**  

### AI Summaries
- Generates structured meeting summaries including:
  - Overview  
  - Action Items  
  - Key Points  
- Edit meeting titles easily  
- Share summaries as plain text  

### Meeting History
- Browse previous meetings  
- View full transcripts and AI summaries  
- Clean and modern UI built using **Jetpack Compose & Material 3**

---

## 📸 Screenshots



## 🛠️ Built With

- **Kotlin**
- **Jetpack Compose (Material 3)**
- **MVVM Architecture**
- **Coroutines & Flow**
- **Room Database** for local storage
- **Foreground Service** for background audio recording
- **MediaRecorder** for audio capture
- **Retrofit + OkHttp** for network communication
- **Accompanist Permissions** for runtime permission handling
- **Navigation Compose** for screen navigation

---

## ⚙️ Setup

```bash
git clone https://github.com/kartkp/TwinMind_Assignment.git
