# App Icons Setup

## Required Icons

The app references these icon files in `AndroidManifest.xml`:
- `@mipmap/ic_launcher` 
- `@mipmap/ic_launcher_round`

## Option 1: Use Android Studio's Image Asset Tool (EASIEST)

1. In Android Studio, right-click on `app` folder
2. Select: New → Image Asset
3. Icon Type: Launcher Icons (Adaptive and Legacy)
4. Name: `ic_launcher`
5. Foreground Layer:
   - Source Asset Type: Clip Art
   - Click on the icon image
   - Search for "motorcycle" or "directions_bike"
   - Choose an icon you like
   - Adjust size and color as needed
6. Background Layer:
   - Color: Choose app color (e.g., #6200EE)
7. Click "Next" → "Finish"
8. Android Studio will auto-generate all required sizes

## Option 2: Use Default Android Icons (QUICK FIX)

If you just want to get the app running quickly, the default Android icons will work fine for testing.

1. Android Studio automatically provides default launcher icons
2. If missing, copy from any other Android project
3. Or download from: https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html

## Icon Sizes Needed

Android requires multiple icon sizes:
- `mipmap-mdpi/` - 48x48 px
- `mipmap-hdpi/` - 72x72 px
- `mipmap-xhdpi/` - 96x96 px
- `mipmap-xxhdpi/` - 144x144 px
- `mipmap-xxxhdpi/` - 192x192 px

## For Now

The project will build with default icons if you:
1. Let Android Studio create them automatically during first build
2. Or use the Image Asset tool (Option 1 above)

**This is optional for Module 1 - the app will use default icons if these aren't created.**
