# SafeRide - Installation Guide

## Prerequisites

### On Your Computer:
1. **Android Studio** (Latest version recommended)
   - Download from: https://developer.android.com/studio
   - Install with default settings

2. **Java Development Kit (JDK)**
   - Android Studio includes JDK, but you can also install separately
   - Version 8 or higher required

### On Your Android Phone:
1. **Android Version**: Android 7.0 (API 24) or higher
2. **Enable Developer Options**:
   - Go to Settings → About Phone
   - Tap "Build Number" 7 times
   - You'll see "You are now a developer!"

3. **Enable USB Debugging**:
   - Go to Settings → Developer Options
   - Turn ON "USB Debugging"

---

## Installation Steps

### Method 1: Install via Android Studio (Recommended)

#### Step 1: Open Project in Android Studio
1. Launch Android Studio
2. Click "Open an Existing Project"
3. Navigate to the `SafeRide` folder
4. Click "OK"

#### Step 2: Let Gradle Sync
- Android Studio will automatically download dependencies
- Wait for "Gradle Build Finished" message (may take 5-10 minutes first time)
- If prompted to update Gradle/plugins, click "Update"

#### Step 3: Connect Your Phone
1. Connect your Android phone to computer via USB cable
2. On your phone, allow USB debugging when prompted
3. In Android Studio, you should see your device name in the device dropdown (top toolbar)

#### Step 4: Build and Install
1. Click the green "Run" button (▶) in Android Studio toolbar
2. OR press Shift + F10
3. Select your connected device
4. App will build and install automatically

---

### Method 2: Install via APK File

#### Step 1: Build APK
1. Open project in Android Studio
2. Go to: Build → Build Bundle(s) / APK(s) → Build APK(s)
3. Wait for build to complete
4. Click "locate" in the notification to find the APK file
5. APK location: `SafeRide/app/build/outputs/apk/debug/app-debug.apk`

#### Step 2: Transfer APK to Phone
**Option A - USB Cable:**
1. Connect phone to computer
2. Copy `app-debug.apk` to phone's Download folder

**Option B - Cloud/Email:**
1. Upload APK to Google Drive/Dropbox
2. Download on your phone

**Option C - Direct Transfer:**
1. Use apps like ShareIt, Send Anywhere, etc.

#### Step 3: Install APK on Phone
1. On your phone, go to Settings → Security
2. Enable "Install from Unknown Sources" or "Install Unknown Apps"
3. Open File Manager
4. Navigate to Download folder
5. Tap on `app-debug.apk`
6. Tap "Install"
7. Tap "Open" when installation completes

---

## First Time Setup

### Step 1: Grant Permissions
When you first open SafeRide, it will ask for permissions:
- ✅ **Camera** - Required for drowsiness detection
- ✅ **Location** - Required to send GPS coordinates during emergencies
- ✅ **SMS** - Required to send emergency alerts
- ✅ **Notifications** - Required for alerts

**Tap "Allow" for all permissions**

### Step 2: Add Emergency Contacts
1. On the main screen, tap "Add Emergency Contact"
2. Enter contact name (e.g., "Mom", "Dad", "Friend")
3. Enter phone number with country code (e.g., +919876543210)
4. Tap "Save"
5. Add at least 1-2 emergency contacts

### Step 3: Mount Your Phone
For best results:
- **Option 1**: Use a phone holder on bike handlebar (camera faces you)
- **Option 2**: Attach to helmet with camera facing your face
- **Option 3**: Place in chest pocket (if camera can see your face)

**Important**: Front camera must clearly see your face!

### Step 4: Start Monitoring
1. Before starting your ride, tap "Start Monitoring"
2. The camera preview should show your face
3. Status will change to "Active"
4. You'll see a persistent notification showing SafeRide is running

---

## How to Use

### During Your Ride:
1. Keep the app running in foreground or background
2. If you feel drowsy, the app will detect it and:
   - Sound a loud buzzer/alarm
   - Vibrate your phone
   - Alert you to take a break

3. If an accident is detected (sudden impact):
   - App automatically sends SMS to all emergency contacts
   - SMS includes: "Emergency! Accident detected at [GPS Location Link]"
   - Your contacts can click the link to see your exact location

### To Stop Monitoring:
1. Open the app
2. Tap "Stop Monitoring"

---

## Troubleshooting

### Camera Not Working
- Go to Settings → Apps → SafeRide → Permissions
- Ensure Camera permission is allowed
- Try restarting the app

### Location Not Accurate
- Enable High Accuracy mode in phone's Location settings
- Ensure you're outdoors with clear sky view for GPS

### SMS Not Sending
- Check if you have SMS balance/plan
- Ensure SMS permission is granted
- Some phones require SMS app to be set as default

### App Keeps Stopping
- Clear app cache: Settings → Apps → SafeRide → Storage → Clear Cache
- Ensure you have Android 7.0 or higher
- Restart your phone

### Battery Drain
- This is normal as app uses camera, GPS, and sensors continuously
- Recommended: Keep phone charging while riding (use power bank or bike USB charger)

---

## Important Notes

⚠️ **Safety First**:
- Do NOT interact with the phone while riding
- Set up everything BEFORE starting your journey
- Pull over safely if you need to check the app

⚠️ **Accuracy**:
- Drowsiness detection works best in good lighting
- Wear clear glasses (not sunglasses) for better face detection
- Keep your face visible to the camera

⚠️ **Battery**:
- App uses significant battery due to continuous monitoring
- Always ride with a charger/power bank

⚠️ **Privacy**:
- Camera feed is processed locally on your phone
- No video/photos are saved or uploaded anywhere
- Your location is only sent during emergency alerts

---

## Next Steps

After successful installation, you're ready to use SafeRide!

**Coming in Next Modules**:
1. Module 2: Drowsiness Detection Implementation
2. Module 3: Accident Detection Implementation
3. Module 4: Emergency Alert System
4. Module 5: Testing & Optimization

---

## Support

If you face any issues:
1. Check this guide's Troubleshooting section
2. Ensure all system requirements are met
3. Try reinstalling the app

---

**Current Status**: ✅ Basic App Structure Complete
**Next Module**: Drowsiness Detection with Face/Eye Tracking
