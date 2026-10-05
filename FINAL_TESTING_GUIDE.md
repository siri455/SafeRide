# SafeRide - Final Testing Guide

## Complete System Test Checklist

### Pre-Testing Setup
- [ ] Phone fully charged (or connected to charger)
- [ ] Good lighting in testing area
- [ ] Android 7.0+ device
- [ ] At least 500 MB free storage
- [ ] All previous modules tested individually

---

## Test Suite 1: Installation & Setup (10 minutes)

### 1.1 Fresh Installation
- [ ] Uninstall any previous version
- [ ] Install app from Android Studio or APK
- [ ] App icon appears in launcher
- [ ] App opens without crash
- [ ] No error messages on first launch

### 1.2 Permission Granting
- [ ] Camera permission requested
- [ ] Location permission requested
- [ ] SMS permission requested
- [ ] Notification permission requested (Android 13+)
- [ ] All permissions granted successfully
- [ ] Camera preview visible after granting

### 1.3 Emergency Contacts
- [ ] Tap "Add Emergency Contact"
- [ ] Dialog appears correctly
- [ ] Enter test contact name
- [ ] Enter test phone number
- [ ] Save successful
- [ ] Contact appears in list
- [ ] Can add multiple contacts (test 2-3)
- [ ] Can remove contact
- [ ] Contacts persist after app restart

**Expected Time**: 10 minutes  
**Pass Criteria**: All items checked ✓

---

## Test Suite 2: Drowsiness Detection (15 minutes)

### 2.1 Face Detection
- [ ] Start monitoring
- [ ] Face detected (status shows "Active")
- [ ] Look away from camera
- [ ] Status changes to "No Face Detected"
- [ ] Look back at camera
- [ ] Status returns to "Active"

### 2.2 Eye Tracking
- [ ] Face clearly visible
- [ ] Keep eyes open normally
- [ ] No false alerts for 1 minute
- [ ] Close eyes for 1 second (blink)
- [ ] No alert (normal blink ignored)
- [ ] Close eyes for 3-4 seconds
- [ ] Alert triggers (vibration + sound)
- [ ] Status shows "Medium Alert" or higher
- [ ] Open eyes
- [ ] Alert stops
- [ ] Status returns to "Active"

### 2.3 Progressive Alerts
- [ ] Close eyes for 2 seconds - Low alert
- [ ] Close eyes for 4 seconds - Medium alert (vibration + sound)
- [ ] Close eyes for 6+ seconds - High/Critical alert (loud + continuous)
- [ ] Alerts escalate with duration
- [ ] Toast message appears

### 2.4 Lighting Conditions
- [ ] Test in bright light - Works perfectly
- [ ] Test in moderate light - Works well
- [ ] Test in low light - May have issues (expected)
- [ ] Test with sunglasses - May not work (expected)

**Expected Time**: 15 minutes  
**Pass Criteria**: 80%+ items checked ✓  
**Critical**: Face detection and basic alerts must work

---

## Test Suite 3: Accident Detection (10 minutes)

### 3.1 Impact Detection
- [ ] Start monitoring
- [ ] Perform drop test (phone on pillow, 12 inches)
- [ ] Impact logged in console
- [ ] Single drop - No accident alert
- [ ] Perform 3-4 rapid drops
- [ ] Accident dialog appears
- [ ] Severity shown (Minor/Moderate)

### 3.2 Shake Test
- [ ] Hold phone firmly
- [ ] Shake vigorously 3-4 times rapidly
- [ ] Accident dialog appears
- [ ] Status shows "Crash Detected!"
- [ ] Vibration + sound alert
- [ ] Tap "I'm OK"
- [ ] Dialog dismisses

### 3.3 False Positive Test
- [ ] Normal phone handling
- [ ] Walking with phone
- [ ] Gentle movements
- [ ] No false accident alerts
- [ ] Only strong impacts trigger

**Expected Time**: 10 minutes  
**Pass Criteria**: All items checked ✓  
**Critical**: 3+ rapid impacts must trigger accident

---

## Test Suite 4: Alert History & Logging (15 minutes)

### 4.1 Accident Logging
- [ ] Trigger accident (3-4 shakes)
- [ ] Tap "View Alert History"
- [ ] Alert History screen opens
- [ ] Accident appears in list
- [ ] Date/time correct
- [ ] Severity correct
- [ ] Status: "Not Acknowledged"
- [ ] Tap on accident
- [ ] Details dialog appears
- [ ] Tap "Acknowledge"
- [ ] Status changes to "✓ Acknowledged"

### 4.2 Drowsiness Logging
- [ ] Start monitoring
- [ ] Trigger drowsiness alert (close eyes 4 sec)
- [ ] View Alert History
- [ ] Drowsiness log appears
- [ ] Level shown (HIGH/MEDIUM/CRITICAL)
- [ ] Timestamp correct

### 4.3 Statistics
- [ ] Check "Total Accidents" count
- [ ] Matches number of triggered accidents
- [ ] Check "Total Drowsiness Alerts"
- [ ] Matches number of alerts
- [ ] Check "Total Ride Time"
- [ ] Increases with monitoring time

### 4.4 Session Tracking
- [ ] Note current ride time
- [ ] Start monitoring
- [ ] Wait 2 minutes (or note time)
- [ ] Stop monitoring
- [ ] View Alert History
- [ ] Total Ride Time increased by ~2 minutes

### 4.5 Clear Logs
- [ ] Have at least 2-3 logs
- [ ] Tap "Clear All Logs"
- [ ] Confirmation dialog appears
- [ ] Confirm clear
- [ ] All logs deleted
- [ ] Statistics reset to 0
- [ ] "No accidents recorded" message

**Expected Time**: 15 minutes  
**Pass Criteria**: All items checked ✓

---

## Test Suite 5: Integration Testing (20 minutes)

### 5.1 Complete Workflow
**Scenario: Full Ride Simulation**

```
Setup (2 min):
- [ ] Fresh app start
- [ ] Add 1 emergency contact
- [ ] Position phone for testing
- [ ] Start monitoring

Riding (5 min):
- [ ] Normal state for 2 minutes
- [ ] No false alerts
- [ ] Status stays "Active"

Drowsiness Event (3 min):
- [ ] Close eyes for 4 seconds
- [ ] Alert triggers correctly
- [ ] Open eyes
- [ ] Alert stops
- [ ] Continue monitoring

Accident Event (2 min):
- [ ] Trigger accident (3-4 shakes)
- [ ] Accident dialog appears
- [ ] Maximum alerts (vibration + sound)
- [ ] Tap "I'm OK"

End Ride (3 min):
- [ ] Stop monitoring
- [ ] View Alert History
- [ ] See 1 accident logged
- [ ] See 1 drowsiness logged
- [ ] Statistics updated

Verification (5 min):
- [ ] All events logged correctly
- [ ] Can acknowledge accident
- [ ] Session recorded
- [ ] Total ride time accurate
```

### 5.2 Multi-Module Interaction
- [ ] Drowsiness and accident detectors run simultaneously
- [ ] No conflicts between detectors
- [ ] Both can trigger in same session
- [ ] Accident takes priority if both trigger
- [ ] UI updates correctly for both

### 5.3 Background Behavior
- [ ] Start monitoring
- [ ] Press Home button
- [ ] Notification persists
- [ ] Wait 1 minute
- [ ] Return to app
- [ ] Still monitoring
- [ ] Detectors still active

**Expected Time**: 20 minutes  
**Pass Criteria**: All items checked ✓  
**Critical**: Full workflow must complete without crashes

---

## Test Suite 6: Performance & Stability (15 minutes)

### 6.1 Battery Test
- [ ] Note starting battery %
- [ ] Start monitoring
- [ ] Let run for 10 minutes (idle)
- [ ] Note ending battery %
- [ ] Battery drain: ~2-4% (expected)

### 6.2 Memory Test
- [ ] Start monitoring
- [ ] Check running services (Settings → Developer Options → Running Services)
- [ ] SafeRide service visible
- [ ] Memory usage: ~110-160 MB (acceptable)

### 6.3 Heat Test
- [ ] Run monitoring for 10 minutes
- [ ] Check phone temperature
- [ ] Slight warmth is normal
- [ ] Should not be uncomfortably hot

### 6.4 Stability Test
- [ ] Start monitoring
- [ ] Run for 5 minutes
- [ ] Trigger drowsiness 2x
- [ ] Trigger accident 2x
- [ ] View Alert History
- [ ] Return to main screen
- [ ] Stop monitoring
- [ ] No crashes or freezes

### 6.5 Stress Test
- [ ] Start monitoring
- [ ] Rapidly trigger 5 drowsiness alerts
- [ ] Rapidly trigger 3 accidents
- [ ] Switch between screens multiple times
- [ ] App remains responsive
- [ ] No crashes

**Expected Time**: 15 minutes  
**Pass Criteria**: 80%+ items checked ✓  
**Critical**: No crashes or major performance issues

---

## Test Suite 7: Edge Cases (10 minutes)

### 7.1 No Emergency Contacts
- [ ] Remove all contacts
- [ ] Try to start monitoring
- [ ] Warning message appears
- [ ] Cannot start without contacts

### 7.2 Permission Denial
- [ ] Revoke camera permission
- [ ] App requests permission again
- [ ] Grant permission
- [ ] Camera preview works

### 7.3 Orientation Changes
- [ ] Rotate device (if not locked)
- [ ] App should stay portrait
- [ ] Or handle rotation gracefully

### 7.4 Low Battery
- [ ] Run with low battery (<20%)
- [ ] App should still function
- [ ] May show battery warning

### 7.5 App Lifecycle
- [ ] Start monitoring
- [ ] Minimize app
- [ ] Open other apps
- [ ] Return to SafeRide
- [ ] Still monitoring
- [ ] Recent apps - swipe away
- [ ] Service stops (expected)

**Expected Time**: 10 minutes  
**Pass Criteria**: All items checked ✓

---

## Test Suite 8: User Experience (10 minutes)

### 8.1 UI/UX Check
- [ ] All text readable
- [ ] Colors make sense (green=good, red=danger)
- [ ] Buttons clearly labeled
- [ ] Icons visible
- [ ] No overlapping elements
- [ ] Scrolling works smoothly

### 8.2 Navigation
- [ ] Can navigate to Alert History
- [ ] Back button returns to main screen
- [ ] Can navigate to Help
- [ ] Help button in toolbar works
- [ ] All screens accessible

### 8.3 Feedback
- [ ] Buttons respond to touch
- [ ] Toasts appear for actions
- [ ] Dialogs show when expected
- [ ] Loading states clear
- [ ] Error messages helpful

### 8.4 Help Screen
- [ ] Help menu item visible
- [ ] Help screen opens
- [ ] All sections readable
- [ ] Information accurate
- [ ] Back button works

**Expected Time**: 10 minutes  
**Pass Criteria**: All items checked ✓

---

## Final Acceptance Test

### Must Pass All:
- [ ] App installs successfully
- [ ] All permissions granted
- [ ] Can add/remove emergency contacts
- [ ] Face detection works
- [ ] Drowsiness alerts trigger correctly
- [ ] Accident detection works (3+ impacts)
- [ ] Alerts are noticeable (sound + vibration)
- [ ] Events are logged
- [ ] Alert History shows all events
- [ ] Statistics are accurate
- [ ] Can acknowledge accidents
- [ ] Can clear logs
- [ ] Help screen accessible
- [ ] No critical bugs
- [ ] No crashes during normal use
- [ ] Battery drain acceptable
- [ ] Performance acceptable

### Optional (Nice to Have):
- [ ] Works in moderate lighting
- [ ] Low false positive rate (<5%)
- [ ] Responsive UI (<100ms delays)
- [ ] Smooth animations
- [ ] Professional appearance

---

## Defect Logging

If you find bugs, log them here:

### Critical Bugs (Must Fix):
```
1. 
2. 
3. 
```

### Medium Bugs (Should Fix):
```
1. 
2. 
3. 
```

### Minor Issues (Can Fix):
```
1. 
2. 
3. 
```

---

## Performance Metrics

Record actual measurements:

```
Battery Drain (10 min test):
Start: ____%
End: ____%
Drain: ____% (Expected: 2-4%)

Memory Usage:
App: ____MB (Expected: 110-160 MB)

False Positives (1 hour monitoring):
Drowsiness: ____ (Expected: 0-2)
Accidents: ____ (Expected: 0-1)

Detection Success Rate:
Drowsiness: ____/10 tests (Expected: 8+/10)
Accidents: ____/10 tests (Expected: 9+/10)
```

---

## Final Checklist

Before marking complete:

- [ ] All test suites executed
- [ ] 90%+ tests passing
- [ ] All critical bugs fixed
- [ ] Documentation reviewed
- [ ] Help screen accurate
- [ ] Ready for demo/submission

---

## Test Report Summary

```
Testing Date: ___________
Tester: ___________
Device: ___________ (Model)
Android Version: ___________

Results:
- Test Suites Completed: ___/8
- Total Tests Passed: ___/___
- Pass Rate: ___%
- Critical Bugs: ___
- Medium Bugs: ___
- Minor Issues: ___

Overall Status: PASS / FAIL / PARTIAL

Comments:
_________________________________
_________________________________
_________________________________

Ready for Deployment: YES / NO
```

---

**Total Testing Time**: ~2 hours  
**Recommended**: Test over 2 sessions (1 hour each)

**Final Recommendation**: App is ready when 90%+ tests pass with 0 critical bugs.
