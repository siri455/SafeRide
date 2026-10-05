# SafeRide - Troubleshooting Guide

## How to Report Build Errors

### Step 1: Identify the Error Type

**Compile Error** (won't build):
```
Error: Cannot find symbol
Error: Unresolved reference
Error: Duplicate class
```

**Runtime Error** (builds but crashes):
```
App crashes on launch
App crashes when clicking button
Feature doesn't work
```

**Logic Error** (runs but wrong behavior):
```
Drowsiness detection doesn't trigger
Accidents not logged
History screen empty
```

### Step 2: Get Error Details

**In Android Studio:**
1. Build → Make Project
2. Check "Build" tab at bottom
3. Copy FULL error message
4. Look for RED text

**From Command Line:**
```bash
cd \code\SafeRide
gradlew clean build > build_log.txt 2>&1
# Check build_log.txt for errors
```

### Step 3: Common Issues & Quick Fixes

#### Issue 1: "Cannot resolve symbol R"
**Fix:**
```
File → Invalidate Caches → Restart
Build → Clean Project
Build → Rebuild Project
```

#### Issue 2: "Unresolved reference"
**Fix:** Missing import
```kotlin
// Add at top of file
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
// etc.
```

#### Issue 3: "Duplicate class"
**Fix:**
```
Build → Clean Project
Delete .gradle folder
Sync Project with Gradle Files
```

#### Issue 4: App crashes on launch
**Check Logcat:**
1. Run app
2. Open Logcat tab
3. Filter by "AndroidRuntime"
4. Look for "FATAL EXCEPTION"
5. Copy stacktrace

#### Issue 5: Permission denied
**Fix:**
```
Settings → Apps → SafeRide → Permissions
Grant all permissions manually
```

#### Issue 6: Camera not working
**Check:**
- Camera permission granted?
- Other app using camera?
- Device has front camera?

#### Issue 7: Drowsiness not detecting
**Check:**
- Monitoring started?
- Face visible in preview?
- Good lighting?
- Eyes actually closed 3+ seconds?

#### Issue 8: Accident not detecting
**Check:**
- Monitoring started?
- Shaking hard enough?
- 3+ rapid shakes needed
- Check Logcat for "High impact detected"

## Please Provide:

To fix your specific issue, I need:

1. **Exact error message** (copy/paste)
2. **When does it happen?** (build time, runtime, specific action)
3. **What were you doing?** (building, installing, using feature)
4. **Screenshot** (if possible)

## Quick Test Checklist

After I fix issues, verify:

- [ ] Project builds without errors
- [ ] APK installs on phone
- [ ] App launches successfully
- [ ] Can tap "Start Monitoring"
- [ ] Camera preview shows your face
- [ ] Can add emergency contact
- [ ] Closing eyes triggers alert
- [ ] Shaking phone triggers accident
- [ ] View Alert History works
- [ ] Help screen opens

## Next Steps

**Please reply with:**
1. Exact error text (or screenshot)
2. Which step fails (build/install/run/specific feature)
3. Any error codes or stack traces

Then I'll fix each issue systematically!
