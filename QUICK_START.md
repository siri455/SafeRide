# SafeRide - Quick Start Guide

## 🚀 Get Running in 5 Steps

### Step 1: Install Android Studio (30 minutes)
1. Download from: https://developer.android.com/studio
2. Run installer with default settings
3. Wait for installation to complete

### Step 2: Open Project (2 minutes)
1. Launch Android Studio
2. Click "Open" 
3. Navigate to `SafeRide` folder
4. Click OK
5. **Wait 5-10 minutes** for Gradle to download dependencies (first time only)

### Step 3: Enable Developer Mode on Phone (1 minute)
1. On your Android phone: Settings → About Phone
2. Tap "Build Number" 7 times rapidly
3. You'll see: "You are now a developer!"
4. Go back → Settings → Developer Options
5. Turn ON "USB Debugging"

### Step 4: Connect & Run (2 minutes)
1. Connect phone to computer with USB cable
2. On phone, tap "Allow" when prompted for USB debugging
3. In Android Studio, device name should appear in top toolbar
4. Click green Run button (▶) or press Shift+F10
5. Wait 2-3 minutes for build and install

### Step 5: Setup App (2 minutes)
1. App opens on your phone
2. Tap "Allow" for all permission requests (Camera, Location, SMS, Notifications)
3. Tap "Add Emergency Contact"
4. Enter name and phone number
5. Tap "Save"
6. Done! App is ready to use

---

## ✅ Verify Installation

Test that everything works:

1. **Camera Preview**: You should see yourself in the camera preview box
2. **Add Contact**: Add a test contact - it should appear in the list
3. **Start Monitoring**: Tap the button - it should change to "Stop Monitoring"
4. **Notification**: You should see a persistent "SafeRide Active" notification
5. **Stop Monitoring**: Tap again - notification disappears

If all 5 work → Installation successful! ✅

---

## ⚠️ Common Issues

### "Gradle sync failed"
- Wait and try again (network issue)
- Click "Try Again" in error message
- Or: File → Invalidate Caches → Restart

### "Device not showing in Android Studio"
- Reconnect USB cable
- On phone: Settings → Developer Options → Revoke USB Debugging → Enable again
- Try a different USB cable

### "App keeps crashing"
- Check if phone is Android 7.0 or higher
- Settings → Apps → SafeRide → Permissions → Allow all
- Restart phone and try again

### "Build failed"
- Check internet connection (Gradle needs to download)
- Tools → SDK Manager → Check if Android SDK is installed
- Build → Clean Project → Rebuild Project

---

## 📱 Minimum Requirements

- **Android Phone**: Version 7.0 or higher
- **Computer**: Windows/Mac/Linux with 8GB RAM
- **Storage**: 5GB free space for Android Studio
- **Internet**: Required for first-time setup only

---

## 🎯 What You Have Now (Module 1)

After completing these steps, you have:
- ✅ Working Android app installed on your phone
- ✅ Camera preview functional
- ✅ Emergency contacts management
- ✅ Basic UI and service architecture

**What's NOT working yet:**
- ❌ Drowsiness detection (Module 2)
- ❌ Accident detection (Module 3)
- ❌ Emergency SMS alerts (Module 4)

---

## 📖 Full Documentation

- **Complete Installation Guide**: See `INSTALLATION_GUIDE.md`
- **Project Documentation**: See `README.md`
- **Module 1 Details**: See `MODULE_1_SUMMARY.md`

---

## 🔜 Next Steps

Once Module 1 is working, let me know and I'll provide:
- **Module 2**: Drowsiness Detection (Face & Eye Tracking)
- **Module 3**: Accident Detection (Accelerometer)
- **Module 4**: Emergency Alerts (GPS + SMS)
- **Module 5**: Testing & Optimization

---

**Questions?** Check the troubleshooting sections in the detailed guides!
