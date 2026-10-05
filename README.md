# SafeRide: Intelligent Rider Safety System

An AI-powered Android application that enhances two-wheeler safety through real-time drowsiness detection and automatic accident alerts.

## 📋 Project Overview

**Problem**: Road accidents involving two-wheelers are increasing due to rider fatigue, drowsiness, and delayed emergency response.

**Solution**: SafeRide detects rider drowsiness, identifies accidents in real-time, and automatically sends emergency alerts with GPS location.

## ✨ Features

### 1. Drowsiness Detection Module
- Real-time face and eye tracking using front camera
- Detects eye closure patterns indicating sleepiness
- Visual and audio alerts to warn the rider
- Works with TensorFlow Lite for on-device ML processing

### 2. Accident Detection Module
- Uses phone's built-in accelerometer sensor
- Detects sudden impacts and collisions
- Distinguishes between normal bumps and actual accidents

### 3. Emergency Alert Module
- Automatically sends SMS to emergency contacts
- Includes real-time GPS location with Google Maps link
- Multiple contact support
- Instant notification delivery

## 🛠️ Technology Stack

| Component | Technology |
|-----------|------------|
| **Language** | Kotlin |
| **UI Framework** | Android XML Layouts + Material Design |
| **Camera** | CameraX API |
| **ML Framework** | TensorFlow Lite |
| **Sensors** | Android SensorManager (Accelerometer) |
| **Location** | Google Play Services Location API |
| **Messaging** | Android SmsManager |
| **Permissions** | PermissionX Library |
| **Storage** | SharedPreferences + Gson |

## 📱 System Requirements

### Minimum Requirements:
- **Android Version**: 7.0 (API Level 24) or higher
- **RAM**: 2GB minimum (4GB recommended)
- **Camera**: Front-facing camera required
- **Sensors**: Accelerometer sensor required
- **Storage**: 50MB free space

### Recommended Setup:
- Android 10 or higher
- 4GB RAM or more
- Good quality front camera
- Stable GPS reception

## 📦 Project Structure

```
SafeRide/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/saferide/app/
│   │   │   │   ├── MainActivity.kt                 # Main activity
│   │   │   │   ├── models/
│   │   │   │   │   └── EmergencyContact.kt        # Data model
│   │   │   │   ├── services/
│   │   │   │   │   └── SafetyMonitoringService.kt # Background service
│   │   │   │   ├── utils/
│   │   │   │   │   └── PreferencesManager.kt      # Settings storage
│   │   │   │   ├── detectors/                     # (Coming in Module 2)
│   │   │   │   └── sensors/                       # (Coming in Module 3)
│   │   │   ├── res/
│   │   │   │   ├── layout/                        # UI layouts
│   │   │   │   ├── values/                        # Strings, colors, themes
│   │   │   │   └── drawable/                      # Icons and images
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── INSTALLATION_GUIDE.md
└── README.md
```

## 🚀 Installation

See [INSTALLATION_GUIDE.md](INSTALLATION_GUIDE.md) for detailed installation instructions.

### Quick Start:
1. Install Android Studio
2. Open this project in Android Studio
3. Connect your Android phone with USB/Wifi debugging enabled
4. Click Run (▶) button
5. Grant all permissions when app opens
6. Add emergency contacts
7. Start monitoring before riding

## 📖 Usage

### Before Riding:
1. Open SafeRide app
2. Add emergency contacts (at least one required)
3. Mount phone securely (camera facing your face)
4. Tap "Start Monitoring"

### During Ride:
- App runs in background with persistent notification
- Drowsiness detection monitors your eyes continuously
- Accident detection monitors for sudden impacts
- Alerts you immediately if drowsiness detected
- Auto-sends emergency SMS if accident detected

### After Ride:
- Tap "Stop Monitoring" when you reach destination
- Review any alerts received during the journey

## 🔐 Permissions Required

| Permission | Purpose |
|------------|---------|
| Camera | Face and eye tracking for drowsiness detection |
| Location (Fine & Coarse) | GPS coordinates for emergency alerts |
| SMS | Sending emergency messages to contacts |
| Notifications | Displaying alerts and status updates |

**Privacy**: All processing happens on-device. No data is uploaded to any server.

## 📊 Current Status

### ✅ Completed (Module 1):
- Basic app structure and UI
- Emergency contacts management
- Permissions handling
- Camera preview integration
- Foreground service setup
- Settings storage system

### 🔄 Coming Next:

#### Module 2: Drowsiness Detection
- TensorFlow Lite model integration
- Face detection using ML Kit
- Eye aspect ratio (EAR) calculation
- Drowsiness alert system
- Buzzer/vibration alerts

#### Module 3: Accident Detection
- Accelerometer data processing
- Impact detection algorithm
- Accident threshold calibration
- Fall detection

#### Module 4: Emergency Alert System
- GPS location fetching
- SMS formatting and sending
- Google Maps link generation
- Alert confirmation system

#### Module 5: Testing & Optimization
- Battery optimization
- Performance tuning
- Real-world testing
- Bug fixes

## 🎯 Advantages

✅ **Low Cost**: Uses only your smartphone - no external hardware needed  
✅ **Easy to Use**: Simple interface, minimal setup  
✅ **Real-time Monitoring**: Continuous safety monitoring while riding  
✅ **Privacy Focused**: All processing on-device, no cloud dependency  
✅ **Offline Capable**: Works without internet (except for SMS)  
✅ **Lightweight**: Minimal storage and resource usage  

## 🔮 Future Enhancements

- Helmet detection before allowing ride start
- Pothole detection and warning
- Speed monitoring and overspeed alerts
- Ride statistics and safety score
- Cloud backup of emergency contacts
- Voice assistant integration
- Multiple language support
- Smartwatch companion app

## ⚠️ Limitations & Disclaimers

- **Not a replacement** for safe riding practices
- Requires phone to be mounted with clear face visibility
- Drowsiness detection accuracy depends on lighting conditions
- Battery intensive - requires charging during long rides
- SMS delivery depends on network coverage
- Accident detection may have false positives/negatives

**This app assists safety but does NOT guarantee accident prevention. Always ride responsibly.**

## 🤝 Contributing

This is a graduate student project. Suggestions and improvements are welcome!

### To Contribute:
1. Test the app and report bugs
2. Suggest feature improvements
3. Help with ML model optimization
4. Contribute to documentation

## 📄 License

This project is created for educational purposes as part of a graduate program.

## 👨‍🎓 Author

Graduate Student Project  
Course: [Your Course Name]  
Institution: [Your University]

## 📞 Support

For issues or questions:
1. Check INSTALLATION_GUIDE.md
2. Review this README
3. Check the troubleshooting section

---

**Version**: 1.0.0 (Module 1 Complete)  
**Last Updated**: June 2026  
**Status**: Active Development 🚧
