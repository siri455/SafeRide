# Module 3: Accident Detection - Testing Guide

## ⚠️ SAFETY FIRST

**DO NOT test while actually riding a bike!**

All tests should be done safely in a controlled environment.

---

## Safe Testing Methods

### Test 1: Phone Drop Test (2 minutes)

**Recommended for:** Testing impact detection safely

#### Steps:
1. Start SafeRide app
2. Add emergency contact (if not done)
3. Tap "Start Monitoring"
4. Prepare a soft surface (pillow, mattress, or folded blanket)
5. Hold phone 6-12 inches above the soft surface
6. Drop the phone onto the soft surface
7. Observe the app response

#### Expected Results:
- ✅ Phone detects impact
- ✅ Logs show "High impact detected: XX m/s²"
- ✅ May show "Minor Impact!" status
- ✅ Vibration may trigger (depends on drop height)

#### Variations:
- **Low drop (6 inches)**: May not trigger (5-15 m/s²)
- **Medium drop (12 inches)**: Should detect (15-25 m/s²)
- **Higher drop**: Stronger detection but ONLY on soft surface!

⚠️ **Never drop on hard surface - will damage phone!**

---

### Test 2: Controlled Shake Test (3 minutes)

**Recommended for:** Testing consecutive impact detection

#### Steps:
1. Ensure monitoring is active
2. Hold phone firmly with both hands
3. Shake the phone vigorously in one direction
4. Do 3-4 rapid shakes within 1-2 seconds
5. Stop and observe

#### Expected Results:
- ✅ Multiple impacts detected
- ✅ Consecutive impact counter increases
- ✅ After 3+ impacts: Accident dialog appears
- ✅ Status shows "Crash Detected!" or severity level
- ✅ Maximum alert (vibration + sound)

#### Variations:
- **Gentle shakes**: Won't trigger (too low force)
- **Single hard shake**: Detected but won't trigger accident
- **3-4 rapid shakes**: Should trigger accident detection

---

### Test 3: Table Tap Test (2 minutes)

**Recommended for:** Testing threshold sensitivity

#### Steps:
1. Ensure monitoring is active
2. Place phone flat on sturdy table
3. Tap the table hard next to phone 3-4 times rapidly
4. Or tap phone directly (not too hard!)
5. Observe response

#### Expected Results:
- ✅ Impacts may be detected if strong enough
- ✅ Vibrations through table/phone register
- ✅ Multiple rapid taps may trigger accident
- ✅ Alert dialog may appear

⚠️ Don't tap too hard - could damage phone screen!

---

### Test 4: Free Fall Detection (2 minutes)

**Recommended for:** Testing free fall + impact combination

#### Steps:
1. Ensure monitoring is active
2. Hold phone in one hand
3. Toss phone UP gently (6-8 inches)
4. CATCH IT with other hand
5. Repeat 2-3 times

#### Expected Results:
- ✅ Brief free fall detected during toss
- ✅ Logs show "Free fall started/ended"
- ✅ Catching provides small impact
- ✅ Combination may trigger "Fall with Impact"

⚠️ **Be very careful - only toss if confident in catching!**

Alternative safe method:
1. Put phone in a sock or soft pouch
2. Hold by the opening
3. Let it drop a few inches while holding sock
4. Catch before it hits anything

---

### Test 5: Bike Simulation Test (5 minutes)

**Recommended for:** Testing realistic scenarios safely

#### Steps:
1. Sit on stationary bike (or chair)
2. Mount phone as if riding
3. Start monitoring
4. Simulate riding movements:
   - Normal pedaling motion
   - Gentle swaying
   - Smooth turns
5. Then simulate accident:
   - Sudden stop motion
   - Drop phone onto cushion
   - Or hard shake

#### Expected Results:

**Normal Riding:**
- ✅ No false alarms
- ✅ Status stays "Active"
- ✅ Small vibrations ignored

**Accident Simulation:**
- ✅ Impact detected
- ✅ Accident dialog appears
- ✅ Maximum alerts

---

## Logcat Monitoring (For Developers)

### View Real-Time Logs:

1. Connect phone to computer
2. Open Android Studio
3. Open Logcat tab
4. Filter: `AccidentDetector`
5. Perform tests while watching logs

### What to Look For:

```
Normal monitoring:
D/AccidentDetector: Accident detection started

Impact detected:
D/AccidentDetector: High impact detected: 27.3 m/s²
D/AccidentDetector: Consecutive impact count: 1

Multiple impacts:
D/AccidentDetector: High impact detected: 28.5 m/s²
D/AccidentDetector: Consecutive impact count: 2
D/AccidentDetector: High impact detected: 29.1 m/s²
D/AccidentDetector: Consecutive impact count: 3
E/AccidentDetector: ACCIDENT DETECTED! Type: MODERATE_CRASH

Free fall:
D/AccidentDetector: Free fall started
D/AccidentDetector: Free fall ended after 420ms
W/AccidentDetector: Free fall followed by impact - possible crash!
```

---

## Understanding the Numbers

### Acceleration Values:

```
0-5 m/s²:    Still or gentle movement
5-10 m/s²:   Normal riding
10-15 m/s²:  Hard braking or bumps
15-20 m/s²:  Very rough terrain
20-25 m/s²:  Hard impact (threshold edge)
25-30 m/s²:  Minor collision/crash
30-40 m/s²:  Moderate crash
40+ m/s²:    Severe crash
```

### Test Result Interpretation:

**Drop from 6 inches:**
- Acceleration: ~15-20 m/s²
- Result: Detected but may not trigger accident

**Drop from 12 inches:**
- Acceleration: ~25-30 m/s²
- Result: Should trigger if 3+ drops quickly

**Hard shake (phone held):**
- Acceleration: ~20-35 m/s²
- Result: 3-4 rapid shakes should trigger

**Actual bike crash:**
- Acceleration: 30-50+ m/s²
- Result: Immediate detection and alert

---

## Troubleshooting Test Issues

### Issue: No Impacts Detected At All

**Diagnosis:**
- Accelerometer may not be available
- Service may not be running
- Threshold too high

**Solutions:**
1. Check logcat for "Accelerometer sensor not available"
2. Verify monitoring started (button says "Stop Monitoring")
3. Try harder impacts (safely!)
4. Check Settings → About Phone → Sensors (should list accelerometer)

---

### Issue: Too Sensitive (False Alarms)

**Diagnosis:**
- Threshold too low
- Phone mount too loose
- Testing on very rough surface

**Solutions:**

**Option 1 - Increase threshold in code:**
```kotlin
// In AccidentDetector.kt line ~13:
private val impactThreshold = 30.0f  // Was 25.0f
```

**Option 2 - Require more impacts:**
```kotlin
// In AccidentDetector.kt line ~15:
private val highImpactThreshold = 4  // Was 3
```

**Option 3 - Shorten time window:**
```kotlin
// In AccidentDetector.kt line ~16:
private val impactTimeWindow = 1500L  // Was 2000L (1.5 seconds)
```

Rebuild and test again.

---

### Issue: Not Sensitive Enough (Misses Accidents)

**Diagnosis:**
- Threshold too high
- Impacts not strong enough
- Not enough consecutive impacts

**Solutions:**

**Option 1 - Decrease threshold:**
```kotlin
// In AccidentDetector.kt:
private val impactThreshold = 20.0f  // Was 25.0f
```

**Option 2 - Require fewer impacts:**
```kotlin
// In AccidentDetector.kt:
private val highImpactThreshold = 2  // Was 3
```

**Option 3 - Longer time window:**
```kotlin
// In AccidentDetector.kt:
private val impactTimeWindow = 3000L  // Was 2000L (3 seconds)
```

---

### Issue: Free Fall Not Detected

**Diagnosis:**
- Fall duration too short (<300ms)
- Not actually in free fall

**Solutions:**
1. Toss phone higher (more airtime)
2. Use the sock method (safer)
3. Check logs for "Free fall started"
4. Reduce duration threshold if needed:

```kotlin
// In AccidentDetector.kt:
private val freeFallDuration = 200L  // Was 300L
```

---

## Performance Testing

### Battery Drain Test (30 minutes):

1. Note starting battery %
2. Start monitoring
3. Let run for 30 minutes with phone idle
4. Note ending battery %

**Expected:**
- ~0.5% battery drain in 30 minutes
- Total with Module 2: ~8-10% in 30 minutes
- Accelerometer is very efficient

---

### CPU Usage Test:

1. Start monitoring
2. Go to Settings → Developer Options → Running Services
3. Find SafeRide
4. Check memory usage

**Expected:**
- RAM: ~110-160 MB (slightly more than Module 2)
- CPU: Very low (sensor is hardware-based)

---

### Heat Test:

1. Start monitoring
2. Run for 10 minutes
3. Check phone temperature

**Expected:**
- Phone should not heat up significantly
- Accelerometer uses minimal power
- Same warmth as Module 2 alone

---

## Integration Test (Full System)

### Complete Safety System Test (10 minutes):

#### Steps:

1. **Setup:**
   - Start SafeRide
   - Ensure emergency contacts added
   - Start monitoring
   - Verify both detections active

2. **Test Drowsiness (Module 2):**
   - Close eyes for 3 seconds
   - Should trigger drowsiness alert
   - Open eyes - alert stops

3. **Test Accident (Module 3):**
   - Perform 3-4 rapid shakes
   - Should trigger accident alert
   - Tap "I'm OK" to dismiss

4. **Test Both:**
   - Close eyes for alert
   - While drowsiness alert active, trigger impact
   - Accident should take priority

#### Expected Results:

✅ **Drowsiness Detection:**
- Works independently
- Alerts as expected from Module 2

✅ **Accident Detection:**
- Works independently  
- Alerts as expected from Module 3

✅ **Priority Handling:**
- Accident takes precedence
- Both can run simultaneously
- No conflicts or crashes

---

## Real-World Scenario Simulation

### Scenario 1: Safe Ride (No Alerts)

**Simulate:**
1. Mount phone
2. Start monitoring
3. Gentle movements for 2 minutes
4. No harsh impacts
5. Eyes open

**Expected:**
- ✅ No false alarms
- ✅ Status stays "Active" (green)
- ✅ No interruptions

---

### Scenario 2: Drowsy Rider

**Simulate:**
1. Start monitoring
2. Normal movements
3. Close eyes for 4 seconds
4. Open eyes

**Expected:**
- ✅ Drowsiness alert triggers
- ✅ Vibration + sound
- ✅ Returns to normal when eyes open
- ✅ No accident alerts

---

### Scenario 3: Minor Accident

**Simulate:**
1. Start monitoring
2. Drop phone on pillow 2-3 times quickly

**Expected:**
- ✅ Impacts detected
- ✅ Accident dialog appears
- ✅ "Minor Impact!" or "Moderate Crash!"
- ✅ Tap "I'm OK" to dismiss

---

### Scenario 4: Severe Accident

**Simulate:**
1. Start monitoring
2. Multiple hard shakes (3-4 rapid)

**Expected:**
- ✅ Multiple impacts detected
- ✅ Accident dialog appears
- ✅ "SEVERE CRASH!" status
- ✅ Maximum alerts
- ✅ Notification updated
- ✅ Ready to send SMS (Module 4)

---

## Acceptance Criteria

Module 3 passes testing if:

### Core Functionality:
- [ ] Accelerometer monitoring works
- [ ] Impacts are detected (via logs)
- [ ] Multiple rapid impacts trigger accident
- [ ] Accident dialog appears
- [ ] Severity classification works
- [ ] "I'm OK" button dismisses alert

### Integration:
- [ ] Works alongside drowsiness detection
- [ ] No conflicts between modules
- [ ] Both can alert simultaneously
- [ ] Accident takes priority

### Performance:
- [ ] Battery drain acceptable (~0.5% per hour added)
- [ ] No significant CPU usage
- [ ] No app crashes
- [ ] No memory leaks

### User Experience:
- [ ] Clear accident alerts
- [ ] Obvious severity indication
- [ ] Easy to dismiss with "I'm OK"
- [ ] Notifications update properly

---

## Common Test Results

### ✅ Pass:
- 3-4 rapid shakes → Accident detected
- Drop test → Impact logged
- Normal use → No false alarms
- Severity matches impact strength

### ⚠️ Marginal:
- 2 impacts detected but not 3 → Increase sensitivity
- Too many false alarms → Decrease sensitivity
- Delayed detection → Normal (200ms is expected)

### ❌ Fail:
- No impacts ever detected → Check accelerometer
- Crashes when shaking → Fix bug, report error
- Constant false alarms → Threshold too low
- Never triggers accident → Threshold too high

---

## Next Steps After Testing

### If Tests Pass:
✅ Proceed to **Module 4: Emergency Alerts**

### If Tests Partially Pass:
⚠️ Adjust thresholds as needed  
⚠️ Retest after adjustments  
✅ Then proceed to Module 4

### If Tests Fail:
❌ Check logcat for errors  
❌ Verify accelerometer exists  
❌ Review code for bugs  
❌ Ask for help if stuck

---

## Module 4 Preview

Next module will add the actual emergency response:

1. **GPS Location** - Get rider's coordinates
2. **SMS Sending** - Text all emergency contacts
3. **Google Maps Link** - Include location in SMS
4. **Countdown Timer** - 10 seconds to cancel
5. **"I'm OK" Override** - Cancel SMS if false alarm

**After Module 4**, accident detection will automatically:
1. Detect crash ✅ (Module 3)
2. Get GPS location (Module 4)
3. Send SMS to contacts (Module 4)
4. Include location link (Module 4)

---

**Testing Complete?** ✅  
**Ready for Module 4?** Let me know! 🚀

---

**Module 3 Testing**: Complete this guide  
**Next**: Module 4 - Emergency Alerts 📱🆘📍
