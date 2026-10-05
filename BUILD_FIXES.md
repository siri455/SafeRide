# Build Fixes - Step by Step

## Issues Found & Fixed

### 1. Missing App Icons
- Created ic_launcher.xml
- Created ic_launcher_round.xml

### 2. Build Command

To build the project:

```bash
# Method 1: In Android Studio
1. Open Android Studio
2. File → Open → Select SafeRide folder
3. Wait for Gradle sync
4. Build → Make Project
5. If errors, check below

# Method 2: Command Line
cd C:\code\SafeRide
gradlew clean build
```

### 3. Common Build Errors & Solutions

**Error: Cannot find symbol R**
Solution: Sync project with Gradle files

**Error: Duplicate class**
Solution: Clean build folder

**Error: Missing dependencies**
Solution: Check internet connection, Gradle will download

### 4. Test Build Status

After fixing, the app should:
- [ ] Compile without errors
- [ ] Install on device
- [ ] Run without crashing
- [ ] All permissions work
- [ ] Camera preview visible
- [ ] Monitoring can start/stop

## Current Build Status

Run this command to check:
```bash
cd app
../gradlew assembleDebug
```

Check output for errors.
