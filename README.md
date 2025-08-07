# ATSlyzer (Frontend)

  ATSlyzer is an Android app that analyzes your resume against a specific job role using AI. Upload your resume PDF, enter the job title, and get an ATS score, keyword matching, grammar feedback, and improvement suggestions — all in one sleek Compose UI.

  ## 🚀 Features

  - 📄 Upload PDF resumes directly from your phone
  - 💼 Enter job roles for customized analysis
  - 🧠 View ATS score, keyword matches, grammar issues, and improvement tips
  - ✨ Stylish Jetpack Compose UI with light/dark mode
  - 📱 Glassmorphism effects + smooth animations
  - 📊 Result cards with progress indicators
  - ✅ Retrofit + ViewModel + MVVM architecture

  ## 🛠️ Tech Stack

  - Jetpack Compose
  - Kotlin
  - MVVM
  - Retrofit
  - KOIN
  - Material 3
  - Android Navigation

  ## 📦 Project Structure

  **ui**          → All Compose UI screens & components  
  **viewmodel**   → ViewModels for API calls  
  **network**     → Retrofit setup & API interface  

  ## 🔌 API Integration

  The app connects to a Spring Boot backend that analyzes resumes.
  - Endpoint: https://atslyzer.onrender.com/resume/upload
  - Request: Multipart with PDF + job role
  - Response: ATS Score, keywords, grammar, tips

  ## 🔧 Setup

  1. Clone the repo  
  2. Open in Android Studio  
  3. Run on emulator or device

  ## 🖼️ Screenshots

<img width="205" height="456" alt="image" src="https://github.com/user-attachments/assets/8f9c3092-49df-4dd1-94d4-ae5fcb6dd26f" />
<img width="203" height="446" alt="image" src="https://github.com/user-attachments/assets/d5a37c65-d946-45d9-bfd7-0066916dae33" />



  ---
  Made with ❤️ by [@ishan739](https://github.com/ishan739)
