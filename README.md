# NeuroGuard — Physiological Stress & Health Monitoring System (Phase 1)

**NeuroGuard** is a native Android application developed in **Kotlin** using modern Android SDK components and **Room Persistence** to monitor real-time physiological stress indicators—specifically **Heart Rate (BPM)** and **Galvanic Skin Response (GSR)**. The system detects acute stress episodes, triggers interactive guided breathing exercises, logs historical readings locally via Room (SQLite), and provides rapid emergency SOS escalation.

---

## Core Features (Phase 1)

- **Real-Time Patient Dashboard (`PatientActivity`):** Displays live Heart Rate and GSR metrics with dynamic color-coded status cards (Green for Normal, Red for High Stress) and configuration-change state preservation (`onSaveInstanceState`).
- **Interactive Guided Breathing (`BreathingActivity`):** Automatically prompted during high-stress events, utilizing property-based scaling animations (`ObjectAnimator`) to guide patients through 4-second inhale/exhale cycles.
- **Caregiver & Historical Logs (`CaregiverActivity` & `HistoryActivity`):** Displays timestamped physiological records retrieved reactively via Kotlin `Flow` and `ViewModel` (`Readingviewmodel`) from local Room storage.
- **Stochastic Sensor Simulation (`MockSensorEngine`):** Generates realistic normal (68–85 BPM, 220–400 GSR) and stressed (110–140 BPM, 750–950 GSR) physiological streams for end-to-end validation prior to hardware deployment.
- **Emergency SOS & System Intents:** Seamless inter-screen navigation via `Explicit Intents` and one-tap emergency calling via `Implicit Intents` (`Intent.ACTION_DIAL`).

---

## System Architecture & Project Structure

```text
Mobileproject/
├── docs/
│   └── NeuroGuard_Phase1_Abstract.pdf   # ACM Two-Column 2-Page Abstract
└── app/src/main/
    ├── java/com/example/neuroguard/
    │   ├── MainActivity.kt              # Entry point & role navigation
    │   ├── PatientActivity.kt           # Patient dashboard & dynamic stress UI
    │   ├── BreathingActivity.kt         # Interactive guided breathing module
    │   ├── CaregiverActivity.kt         # Caregiver monitoring interface
    │   ├── HistoryActivity.kt           # Historical physiological readings log
    │   ├── MockSensorEngine.kt          # Simulated Heart Rate & GSR generator
    │   ├── AppDatabase.kt               # Room Database singleton instance (KSP)
    │   ├── Reading.kt                   # Room @Entity data model
    │   ├── ReadingDao.kt                # Room @Dao queries & Flow streams
    │   ├── ReadingRepo.kt               # Repository abstraction layer
    │   ├── Readingviewmodel.kt          # ViewModel for UI state & readings
    │   └── VitalDatabaseHelper.kt       # Legacy SQLite helper
    ├── res/
    │   ├── layout/                      # Classical XML UI layouts
    │   └── drawable/                    # Custom shapes & dynamic UI drawables
    └── AndroidManifest.xml              # Activity declarations & intent filters
Team Members (Team 57)
Antonios Botros Qorayshi (Tonibotros)

Antoniuos Fayez Salwans (tonyfayez1)

Mahmoud Ibrahim Mohamed El-Sayed (mahmoud23905)

Mohamed Ashraf Mohamed Mahmoud El-Rify (elrify)

Fares Mohamed Soliman (Fares-soliman7)