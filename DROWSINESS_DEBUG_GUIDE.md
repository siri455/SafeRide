# Drowsiness Detection - Debug Guide

## Issue: No alerts when eyes closed for 3+ seconds

### Possible Causes & Solutions:

## Cause 1: Camera Stops When App Goes to Background ✅ MOST LIKELY

**Problem:**
- When you press Home button, app goes to background
- Android may pause camera to save battery
- No frames = no face detection = no alerts

**Solution: Keep App in FOREGROUND**

### TEST 1: Keep App Visible
1. Start monitoring
2. **DO NOT press Home button**
3. **Keep app on screen**
4. Close eyes for 3 seconds
5. Alert should trigger

**If this works:** Camera needs to stay active in background

---

## Cause 2: Face Not Detected

**Check in Logcat:**
```
Look for: "Face detection failed" or "NO_FACE"
```

**Solution:**
- Ensure good lighting
- Face must be clearly visible
- Look directly at camera

### TEST 2: Face Detection
1. Start monitoring
2. Check if status shows "Active" (green)
3. If shows "No Face Detected" (yellow):
   - Improve lighting
   - Position face in center
   - Move closer to camera

---

## Cause 3: Detection Working But Alerts Not Showing

**Check in Logcat:**
```
Filter: "DrowsinessDetector"
Look for: "Average EAR" values
Look for: "Drowsiness detected"
```

### TEST 3: Check Logs
1. Connect phone to computer
2. Open Android Studio → Logcat
3. Filter: "DrowsinessDetector"
4. Start monitoring
5. Close eyes for 5 seconds
6. Check if you see:
   - "Average EAR: 0.XX" (should be <0.25 when eyes closed)
   - "Drowsiness detected! Level: XXX"

**If you see "Drowsiness detected" in logs but no popup:**
- Issue is with UI update
- Service-to-Activity communication problem

**If you DON'T see any logs:**
- Camera analyzer not sending frames
- Face detector not running

---

## Quick Diagnostic Test

### Step-by-Step Diagnosis:

**1. Check Service Running:**
```
After "Start Monitoring":
- Pull down notification shade
- Should see "SafeRide Active" notification
- If NO notification: Service not started
```

**2. Check Camera Active:**
```
- Keep app on screen (don't minimize)
- Look at camera preview
- Should see your face moving in real-time
- If preview frozen: Camera paused
```

**3. Check Face Detection:**
```
- Status should show "Active" (green)
- Look away from camera
- Status should change to "No Face Detected"
- Look back
- Status should return to "Active"
- If status doesn't change: Face detection not working
```

**4. Check Eye Detection:**
```
With app in FOREGROUND (on screen):
- Close eyes for 5 seconds (count slowly)
- Listen for vibration
- Listen for sound
- Watch for popup

If nothing happens:
- Connect to Logcat
- Check for "DrowsinessDetector" logs
- Check for "Average EAR" values
```

---

## Fix 1: Keep App in Foreground (TEMPORARY FIX)

**For Testing:**
1. Start monitoring
2. **Keep phone unlocked**
3. **Keep app on screen** (don't go to Home)
4. Close eyes
5. Test if alert works

**If this works:**
Problem confirmed: Camera stops in background

**Permanent Fix Needed:**
- Modify app to keep camera active in background
- Or require app to stay in foreground during monitoring

---

## Fix 2: Check Logcat for Errors

**Steps:**
1. Connect phone via USB
2. Android Studio → Logcat
3. Filter dropdown → your device
4. Search: "SafeRide" or "Drowsiness" or "AndroidRuntime"
5. Start monitoring
6. Close eyes for 5 seconds
7. Look for:
   - Errors (RED)
   - "Drowsiness detected"
   - "Average EAR"

**Send me the logs if you see errors!**

---

## Expected Behavior

### When Working Correctly:

**1. Start Monitoring:**
```
Logcat: "Camera analysis started"
UI: Button changes to "Stop Monitoring"
UI: Status shows "Active" (green)
Notification: "SafeRide Active" appears
```

**2. Close Eyes (App in FOREGROUND):**
```
After ~3 seconds:
Logcat: "Average EAR: 0.15" (low value)
Logcat: "Drowsiness detected! Level: MEDIUM"
Phone: VIBRATES
Phone: Sound plays
UI: Popup dialog appears
UI: Status changes to "Medium Alert"
```

**3. Open Eyes:**
```
Logcat: "Average EAR: 0.85" (high value)
Alerts: Stop
UI: Status returns to "Active"
```

---

## Common Issues & Solutions

### Issue: "It worked once but now doesn't work"

**Possible:**
- Cooldown period (5 seconds between alerts)
- Service crashed and didn't restart
- Camera lost permission

**Solution:**
```
1. Stop monitoring
2. Close app completely (swipe from recents)
3. Reopen app
4. Start monitoring again
5. Test again
```

### Issue: "False alerts when eyes open"

**Possible:**
- Poor lighting
- Wearing dark glasses
- Eyes partially closed

**Solution:**
- Better lighting
- Remove sunglasses
- Open eyes fully

### Issue: "No alerts even after 10 seconds"

**Possible:**
- Face not detected
- Camera not providing frames
- Service not running

**Solution:**
1. Check Logcat for errors
2. Verify "Active" status
3. Restart app
4. Check camera permission

---

## Immediate Action Plan

### Do These Tests NOW:

**TEST A: Foreground Test (2 minutes)**
```
1. Start monitoring
2. KEEP app on screen (don't minimize)
3. KEEP phone unlocked
4. Face clearly visible in preview
5. Close both eyes completely
6. Count slowly: "1 Mississippi, 2 Mississippi, 3 Mississippi"
7. Keep eyes closed for 5 full seconds

Expected at 3 seconds:
- Phone vibrates
- Popup appears
- Sound plays

Result: PASS / FAIL
```

**If PASS:**
✅ Detection works!
❌ Problem is camera stops in background

**If FAIL:**
Check Logcat and tell me what you see.

---

## Send Me This Information:

If it's not working, please provide:

1. **Test A Result:** PASS or FAIL

2. **Logcat Output:**
```
Filter: DrowsinessDetector
Time period: During eye closure test
Copy/paste any lines you see
```

3. **What You See:**
```
- Status showing: ________
- Notification visible: YES / NO
- Camera preview: WORKING / FROZEN
- Face detected changes when you look away: YES / NO
```

4. **Android Version:** _______

5. **Phone Model:** _______

Then I'll know exactly what to fix!

---

## My Next Fix

Based on your testing:
- If it works in foreground only → I'll fix background camera
- If it doesn't work at all → I'll check the service connection
- If logs show errors → I'll fix those errors

**Please do TEST A first and report back!** 🔍
