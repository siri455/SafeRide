# Step-by-Step Build & Test Guide

## Prerequisites

- [ ] Android Studio installed (version 2022.1 or later)
- [ ] Android phone with USB cable
- [ ] USB Debugging enabled on phone
- [ ] Internet connection (for first-time Gradle download)

## Step 1: Open Project (5 minutes)

1. Launch Android Studio
2. Click "Open"
3. Navigate to: `C:\code\SafeRide`
4. Click "OK"
5. **WAIT** - Gradle will sync (5-10 minutes first time)
6. Watch bottom right corner for "Gradle sync" progress
7. Wait until it says "Gradle sync finished"

**Expected:** No errors in "Build" tab

**If you see errors at this step**, copy the EXACT error message and tell me!

## Step 2: Verify Dependencies (2 minutes)

1. Open `app/build.gradle.kts`
2. Check all dependencies are there
3. Click "Sync Now" if prompted

**Expected:** All dependencies download successfully

## Step 3: Build Project (3 minutes)

1. Click: Build → Make Project (or Ctrl+F9)
2. Watch "Build" tab at bottom
3. Wait for "BUILD SUCCESSFUL"

**If BUILD FAILED:**
- Copy the FULL error message
- Tell me exactly what it says
- I'll fix it immediately

## Step 4: Connect Phone (2 minutes)

1. Connect Android phone via USB/Wifi
2. On phone, unlock screen
3. Tap "Allow USB Debugging" if prompted
4. In Android Studio, check top toolbar
5. Your device name should appear in dropdown

**If device not showing:**
- Try different USB cable
- Check USB Debugging enabled
- Restart Android Studio

## Step 5: Run App (2 minutes)

1. Click green "Run" button (▶) or Shift+F10
2. Select your device
3. Click "OK"
4. Wait 1-2 minutes for install

**Expected:**
- "Installing APK" message
- "App installed" success
- App launches on phone

**If fails:**
- Copy error message
- Tell me what happened
- I'll fix it

## Step 6: Grant Permissions (1 minute)

When app opens:

1. Permission dialog appears
2. Tap "Allow" for Camera
3. Tap "Allow" for Location
4. Tap "Allow" for SMS
5. Tap "Allow" for Notifications (Android 13+)

**Expected:** All permissions granted

## Step 7: Test Basic Features (5 minutes)

### Test 1: Emergency Contact
1. Tap "Add Emergency Contact"
2. Enter name: "Test"
3. Enter number: "1234567890"
4. Tap "Save"
5. **Verify:** Contact appears in list

✅ **PASS** if contact shows up  
❌ **FAIL** - Tell me what happened

### Test 2: Camera Preview
1. Look at camera preview box
2. **Verify:** You can see your face
3. **Verify:** Preview is live (move, see movement)

✅ **PASS** if you see yourself  
❌ **FAIL** - Tell me what you see (black screen? error?)

### Test 3: Start Monitoring
1. Tap "Start Monitoring"
2. **Verify:** Button changes to "Stop Monitoring"
3. **Verify:** Status shows "Active" (green)
4. **Verify:** Notification appears

✅ **PASS** if all three happen  
❌ **FAIL** - Tell me what's different

### Test 4: Drowsiness Detection
1. Ensure monitoring is active
2. Face must be visible in camera
3. Close BOTH eyes for 4-5 seconds
4. **Verify:** Phone vibrates
5. **Verify:** Sound plays
6. **Verify:** Status changes to "Medium Alert" or higher

✅ **PASS** if alert triggers  
❌ **FAIL** - Tell me: Does face show? Eyes detected? Any alert at all?

### Test 5: Accident Detection
1. Ensure monitoring is active
2. Hold phone firmly
3. Shake vigorously 3-4 times (1 second total)
4. **Verify:** Dialog appears "ACCIDENT DETECTED"
5. Tap "I'm OK"

✅ **PASS** if dialog shows  
❌ **FAIL** - Tell me: Any response? Check Logcat for "High impact"

### Test 6: Alert History
1. Tap "View Alert History"
2. **Verify:** New screen opens
3. **Verify:** Statistics show
4. **Verify:** Any accidents/drowsiness logged

✅ **PASS** if screen opens  
❌ **FAIL** - Tell me what happens

### Test 7: Help Screen
1. Tap menu (⋮) at top right
2. Tap "Help"
3. **Verify:** Help screen opens

✅ **PASS** if help shows  
❌ **FAIL** - Tell me error

## Step 8: Check Logcat (If Issues)

If anything fails:

1. In Android Studio, click "Logcat" tab (bottom)
2. Filter dropdown → Select your device
3. Search box → Type "SafeRide" or "AndroidRuntime"
4. Look for RED errors
5. Copy error text
6. Send to me

## Report Template

If you have issues, copy this and fill in:

```
ISSUE REPORT

Step Failed: [e.g., "Step 3 - Build Project"]

Error Message:
[Paste EXACT error here]

What I See:
[Describe what you see]

What Should Happen:
[Describe expected behavior]

Screenshots: [Yes/No]

Logcat Output:
[Paste if available]
```

## Common Solutions

### "Gradle sync failed"
```
Solution:
1. Check internet connection
2. File → Invalidate Caches → Restart
3. Try again
```

### "Build failed - Unresolved reference"
```
Solution:
1. Build → Clean Project
2. Build → Rebuild Project
3. If still fails, tell me WHICH reference
```

### "Installation failed"
```
Solution:
1. Uninstall old version from phone
2. Try installing again
3. Check phone storage (need 100MB free)
```

### "App crashes immediately"
```
Solution:
1. Open Logcat
2. Find "FATAL EXCEPTION"
3. Copy full stack trace
4. Send to me
```

### "Camera black screen"
```
Solution:
1. Check camera permission granted
2. Close other apps using camera
3. Restart app
4. If still black, tell me
```

### "Alerts don't trigger"
```
For Drowsiness:
- Face visible? Look directly at camera
- Good lighting? Not too dark
- Eyes fully closed? Keep closed 4-5 seconds
- Check Logcat for "Drowsiness detected"

For Accident:
- Shake HARD 3-4 times in 1 second
- Hold phone firmly while shaking
- Check Logcat for "High impact detected"
```

## Next Steps

1. **Go through steps 1-7 above**
2. **Mark each test as PASS or FAIL**
3. **For any FAIL, provide details using template**
4. **I'll fix issues immediately!**

---

**Ready to start?**  
Begin with Step 1 and let me know when you hit any errors!
