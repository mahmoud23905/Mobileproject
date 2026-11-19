# NeuroGuard — Physiological Stress & Health Monitoring System (Phase 1)

**NeuroGuard** is a native Android application developed in **Kotlin** using modern Android architecture components and classical XML UI layouts to monitor real-time physiological stress indicators—specifically **Heart Rate (BPM)** and **Galvanic Skin Response (GSR)**. The system detects acute stress episodes, triggers interactive guided breathing exercises, logs historical readings locally via **Room Persistence Library**, and provides rapid emergency SOS escalation.

---

## 📱 Core Features (Phase 1)

- **Role-Based Entry (`MainActivity`):** Provides quick access to both the Patient Portal and Caregiver Portal alongside a global emergency SOS trigger.
- **Real-Time Patient Dashboard (`PatientActivity`):** Displays live Heart Rate and GSR metrics with dynamic color-coded status feedback (Normal vs. High Stress) and configuration-change state preservation (`onSaveInstanceState`).
- **Interactive Guided Breathing (`BreathingActivity`):** Automatically prompted during high-stress events, utilizing property-based scaling animations (`ObjectAnimator`) to guide patients through 4-second inhale/exhale cycles.
- **Caregiver Monitoring & History (`CaregiverActivity` & `ReadingAdapter`):** Displays timestamped physiological records reactively using Kotlin `Flow` and `RecyclerView`, complete with live alert banners, caregiver clinical notes (`updateNote`), and record management.
- **Local Room Database Persistence (`AppDatabase`, `ReadingDao`, `ReadingRepository`, `Reading`):** Replaces raw SQLite boilerplate with a type-safe Room database compiled via **KSP (Kotlin Symbol Processing)** for fast, asynchronous CRUD operations using Kotlin Coroutines.
- **Stochastic Sensor Simulation (`MockSensorEngine`):** Generates realistic normal (68–85 BPM, 220–400 GSR) and stressed (110–140 BPM, 750–950 GSR) physiological streams for end-to-end validation prior to hardware deployment.
- **Emergency SOS & System Intents:** Seamless inter-screen navigation via `Explicit Intents` and one-tap emergency calling via `Implicit Intents` (`Intent.ACTION_DIAL` / `Intent.ACTION_CALL`).

---

## 🏗️ System Architecture & Project Structure

```text
Mobileproject/
├── docs/
│   └── NeuroGuard_Phase1_Abstract.pdf   # ACM Two-Column 2-Page Abstract
├── gradle/
│   └── libs.versions.toml               # Version catalog (AGP, Room, KSP)
└── app/src/main/
    ├── java/com/example/neuroguard/
    │   ├── MainActivity.kt              # Entry point, role navigation & SOS
    │   ├── PatientActivity.kt           # Patient dashboard & dynamic stress UI
    │   ├── BreathingActivity.kt         # Interactive guided breathing module
    │   ├── CaregiverActivity.kt         # Caregiver dashboard, alerts & notes
    │   ├── ReadingAdapter.kt            # RecyclerView adapter for reading logs
    │   ├── MockSensorEngine.kt          # Simulated Heart Rate & GSR generator
    │   ├── Reading.kt                   # Room @Entity data model
    │   ├── ReadingDao.kt                # Room @Dao SQL queries & Flow streams
    │   ├── AppDatabase.kt               # Room @Database singleton instance
    │   └── ReadingRepo.kt               # Repository layer abstracting data access
    ├── res/
    │   ├── layout/                      # Classical XML UI layouts
    │   └── drawable/                    # Custom shapes & dynamic UI drawables
    └── AndroidManifest.xml              # Activity declarations & permissions
```

---

## 🚀 Setup & Build Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/mahmoud23905/Mobileproject.git
   ```
2. Open the project in **Android Studio** and allow Gradle & KSP to sync dependencies.
3. Run the app on an Android Emulator or physical device (API 24+).
4. Before pushing updates to GitHub, clean temporary build artifacts:
   ```bash
   ./gradlew clean
   ```

---

## 👥 Team 57 — Faculty of Computer and Information Sciences, Ain Shams University

- **Antonios Botros Qorayshi** (`2023170109`)
- **Antoniuos Fayez Salwans** (`2023170110`)
- **Fares Mohamed Soliman** (`2023170418`)
- **Mahmoud Ibrahim Mohamed El-Sayed** (`2023170545`)
- **Mohamed Ashraf Mohamed Mahmoud El-Rify** (`2022170354`)