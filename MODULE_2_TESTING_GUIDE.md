# Module 2: Drowsiness Detection - Testing Guide

## Quick Test Procedure

### Prerequisites
- ✅ Module 1 working correctly
- ✅ App rebuilt with Module 2 code
- ✅ All permissions granted
- ✅ At least one emergency contact added

---

## Test 1: Face Detection (2 minutes)

### Steps:
1. Open SafeRide app
2. Start monitoring
3. Hold phone so front camera can see your face

### Expected Results:
- ✅ Camera preview shows your face
- ✅ Status shows "Active" in GREEN
- ✅ Notification says "Monitoring your safety..."

### Test Variations:
**Look Away from Camera:**
- Status changes to "No Face Detected" in YELLOW
- No alerts triggered

**Look Back at Camera:**
- Status returns to "Active" in GREEN

### ❌ If Face Not Detected:
- Check camera permission
- Improve lighting
- Position face closer to camera
- Ensure camera lens is clean

---

## Test 2: Drowsiness Detection - Low Level (2 minutes)

### Steps:
1. Ensure monitoring is active and face detected
2. Close your eyes for exactly 1-2 seconds
3. Open your eyes

### Expected Results:
- ✅ Short vibration (200ms bursts)
- ✅ Status briefly shows "Low Alert" in YELLOW
- ✅ Returns to "Active" when eyes open

### ❌ Troubleshooting:
- If no alert: Close eyes longer (3+ seconds)
- If too sensitive: May be lighting issue
- Ensure you fully close both eyes

---

## Test 3: Drowsiness Detection - Medium Level (3 minutes)

### Steps:
1. Ensure monitoring is active and face detected
2. Close your eyes for 3-4 seconds
3. Keep eyes closed
4. Open your eyes

### Expected Results:
- ✅ Longer vibration pattern (500ms bursts)
- ✅ Alert sound plays (medium volume)
- ✅ Status shows "Medium Alert" in YELLOW
- ✅ Notification updates to "Drowsiness Alert! Level: MEDIUM"
- ✅ Alerts stop when you open eyes

---

## Test 4: Drowsiness Detection - High/Critical Level (3 minutes)

### Steps:
1. Ensure monitoring is active and face detected
2. Close your eyes and keep them closed
3. Wait 5-7 seconds
4. Listen for escalating alerts
5. Open your eyes

### Expected Results:
- ✅ Continuous vibration (repeating pattern)
- ✅ Loud alarm sound (full volume)
- ✅ Status shows "High Alert!" or "CRITICAL ALERT!" in RED
- ✅ Notification updates urgently
- ✅ Toast message: "Drowsiness Detected! Please take a break."
- ✅ All alerts stop when eyes open

### What You Should Feel:
- Vibration should be strong and attention-grabbing
- Sound should be loud enough to wake you
- Should be impossible to ignore

---

## Test 5: Blink Detection (2 minutes)

### Steps:
1. Ensure monitoring is active
2. Blink normally (quick eye closures)
3. Blink 10-15 times in 1 minute
4. Blink slowly a few times

### Expected Results:
- ✅ Normal quick blinks: NO ALERTS
- ✅ Status stays "Active" in GREEN
- ✅ Only sustained eye closure triggers alerts

### ❌ If Normal Blinks Trigger Alerts:
- You may be blinking very slowly
- Try faster blinks
- This is rare and not critical

---

## Test 6: Alert Cooldown (2 minutes)

### Steps:
1. Trigger a drowsiness alert (close eyes 3+ sec)
2. Immediately close eyes again when alert stops
3. Repeat quickly

### Expected Results:
- ✅ Second alert may not trigger immediately
- ✅ 5-second cooldown prevents spam
- ✅ This is intentional design

---

## Test 7: Background Monitoring (3 minutes)

### Steps:
1. Start monitoring
2. Press Home button (app goes to background)
3. Wait 1 minute
4. Close your eyes for 5 seconds
5. Open SafeRide from recent apps

### Expected Results:
- ✅ Notification still visible
- ✅ Alerts still work in background
- ✅ App returns to monitoring state
- ✅ Camera still active

---

## Test 8: Stop and Restart (2 minutes)

### Steps:
1. Start monitoring
2. Close eyes to trigger alert
3. Stop monitoring
4. Start monitoring again
5. Close eyes again

### Expected Results:
- ✅ Alerts stop when monitoring stops
- ✅ Alerts resume when monitoring restarts
- ✅ Counter resets (no carryover)

---

## Test 9: Lighting Conditions (5 minutes)

Test in different lighting:

### Bright Light:
- ✅ Should work perfectly
- ✅ Face detection very accurate
- ✅ Eye tracking precise

### Moderate Light (indoor):
- ✅ Should work well
- ✅ May have occasional missed frames

### Low Light (evening/night):
- ⚠️ May have difficulty detecting face
- ⚠️ Turn on room lights for better detection
- ⚠️ Avoid riding in dark (safety issue anyway)

### Direct Sunlight:
- ⚠️ Avoid - can cause glare
- ⚠️ Position phone to avoid direct sun on camera

---

## Test 10: Sunglasses Test (1 minute)

### Steps:
1. Start monitoring without sunglasses - verify it works
2. Put on dark sunglasses
3. Check if face/eyes still detected

### Expected Results:
- ⚠️ Clear glasses: Works fine
- ⚠️ Light sunglasses: May work
- ❌ Dark sunglasses: Likely won't detect eyes
- **Recommendation**: Don't wear dark sunglasses when using SafeRide

---

## Performance Tests

### Battery Test (30 minutes):
1. Start with 100% battery (or note current %)
2. Start monitoring
3. Let run for 30 minutes
4. Note battery level

**Expected:**
- 7-10% battery drain in 30 minutes
- 15-20% per hour is normal
- **Recommendation**: Always use charger during rides

### Heat Test (10 minutes):
1. Start monitoring
2. Let run for 10 minutes
3. Feel phone temperature

**Expected:**
- Phone will be slightly warm (normal)
- Should not be uncomfortably hot
- If overheating: Take a break, let phone cool

---

## Full Integration Test (10 minutes)

Complete realistic scenario:

### Steps:
1. **Setup Phase:**
   - Open app
   - Verify contacts added
   - Mount phone as if on bike (facing you)
   - Start monitoring

2. **Riding Simulation:**
   - Simulate 2 minutes of alert riding (eyes open)
   - Status should stay "Active" green
   - No false alerts

3. **Drowsiness Simulation:**
   - Close eyes for 4-5 seconds (simulate drowsiness)
   - Alert should trigger
   - Open eyes immediately when alert sounds
   - Simulate recovering from drowsiness

4. **Continue Riding:**
   - Keep monitoring for 2 more minutes
   - Status should return to normal
   - Blink normally - no false alerts

5. **End Ride:**
   - Stop monitoring
   - Verify all alerts stopped

### Success Criteria:
- ✅ Detected real drowsiness
- ✅ No false alerts during normal riding
- ✅ Alerts were attention-grabbing
- ✅ System recovers when eyes open
- ✅ No crashes or freezes

---

## Checklist Summary

Before proceeding to Module 3, verify:

### Core Functionality:
- [ ] Face detection works in good lighting
- [ ] Eye tracking detects open/closed eyes
- [ ] Closing eyes 3+ seconds triggers alert
- [ ] Normal blinking doesn't trigger alerts
- [ ] Vibration works
- [ ] Sound alerts work
- [ ] UI updates show correct status
- [ ] Color coding works (green/yellow/red)

### Edge Cases:
- [ ] "No face" detection works when looking away
- [ ] Alert cooldown prevents spam
- [ ] Background monitoring works
- [ ] Stop/restart works correctly

### Performance:
- [ ] Battery drain is acceptable (~15-20%/hour)
- [ ] Phone doesn't overheat
- [ ] App doesn't crash
- [ ] No significant lag

### User Experience:
- [ ] Alerts are noticeable and urgent
- [ ] Status updates are clear
- [ ] Easy to understand what's happening
- [ ] Notifications work properly

---

## Common Issues & Solutions

### Issue: "No alerts at all"
**Solutions:**
1. Verify monitoring is started (button says "Stop Monitoring")
2. Check face is detected ("Active" status)
3. Close eyes for full 3+ seconds
4. Ensure volume not muted
5. Check vibration enabled in phone settings
6. Restart app and try again

### Issue: "Too many false alerts"
**Solutions:**
1. Improve lighting (brighter room)
2. Remove sunglasses
3. Ensure camera can see both eyes clearly
4. Reduce phone vibrations (mount stably)
5. Avoid extreme face angles

### Issue: "Face not detected"
**Solutions:**
1. Better lighting
2. Position face closer/more centered
3. Clean camera lens
4. Remove dark sunglasses
5. Ensure camera permission granted

### Issue: "Battery drains too fast"
**Solutions:**
1. This is expected (camera + ML = power hungry)
2. Always ride with charger/power bank
3. Close other apps
4. Reduce screen brightness
5. Use power saving mode when not monitoring

### Issue: "App crashes"
**Solutions:**
1. Restart phone
2. Clear app cache (Settings → Apps → SafeRide → Clear Cache)
3. Reinstall app
4. Check Android version (need 7.0+)
5. Check available RAM

---

## Debug Mode (For Developers)

### View Logs in Android Studio:

1. Connect phone via USB
2. Open Logcat in Android Studio
3. Filter: `com.saferide.app`
4. Look for these tags:
   - `DrowsinessDetector` - Face detection logs
   - `AlertManager` - Alert trigger logs
   - `CameraAnalyzer` - Frame processing logs

### What to Look For:
- "Average EAR: X.XX" - Eye aspect ratio values
- "Drowsiness detected!" - Alert triggers
- "Blink detected" - Blink counting
- "Face detection failed" - Detection errors

---

## Performance Metrics

### Normal Operation:
- **Frame Rate**: 30 FPS capture, 5 FPS analysis
- **Detection Latency**: ~200ms
- **Alert Trigger Time**: 3 seconds of closed eyes
- **Alert Duration**: 3 seconds (auto-stop)
- **Cooldown**: 5 seconds between alerts

### Resource Usage:
- **CPU**: Moderate (ML processing)
- **RAM**: ~100-150 MB
- **Battery**: ~15-20% per hour
- **Network**: None (all on-device)

---

## Ready for Module 3?

If all tests pass, you're ready for **Module 3: Accident Detection**!

Module 3 will add:
- Accelerometer sensor monitoring
- Impact detection
- Crash detection algorithm
- Integration with emergency SMS system

**Proceed when:**
- ✅ All core tests pass
- ✅ You understand how drowsiness detection works
- ✅ Performance is acceptable
- ✅ No critical bugs

---

**Current Progress**: 40% Complete (2/5 modules) ✅  
**Next**: Module 3 - Accident Detection 🚗💥
