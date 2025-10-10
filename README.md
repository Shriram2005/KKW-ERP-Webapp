# KKW ERP Portal App

A simple Android application built with Kotlin and Jetpack Compose that provides quick access to
three educational portals.

## Features

- **Splash Screen**: Displays the college logo with a welcome message on app launch
- **Home Screen**: Clean interface with three navigation buttons
- **WebView Screens**: Integrated web views for each portal with proper navigation
- **Material Design 3**: Modern, clean UI following Material Design guidelines

## Portals Included

1. **LMS Portal** - Learning Management System
    - URL: http://era.mkcl.org/lms/#/15477477481473922139

2. **Mobile App Development Course** - Online learning platform
    - URL: https://eranx.mkcl.org/learner/login

3. **AERP Login** - Academic ERP system
    - URL: https://aerp.kkwagh.edu.in

## Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Navigation**: Jetpack Compose Navigation
- **WebView**: Android WebView with JavaScript enabled
- **Material Design**: Material 3 Components
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 36

## Project Structure

```
app/src/main/java/com/kkwieer/erpwebapp/
├── MainActivity.kt                    # Main activity entry point
├── navigation/
│   ├── Screen.kt                      # Navigation routes definition
│   └── NavGraph.kt                    # Navigation graph setup
├── screens/
│   ├── SplashScreen.kt                # Splash screen with college logo
│   ├── HomeScreen.kt                  # Home screen with navigation buttons
│   └── WebViewScreen.kt               # Reusable WebView component
└── ui/theme/                          # Theme and styling
```

## How to Build

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Build and run the app on an emulator or physical device

```bash
./gradlew assembleDebug
```

## Features Implemented

### Splash Screen

- Displays college logo (KKW)
- Animated welcome message
- Auto-navigates to home screen after 2 seconds

### Home Screen

- Three prominent buttons for each portal
- Material Design 3 color scheme
- Clean and minimal UI

### WebView Screens

- Full-screen web view
- JavaScript enabled
- DOM storage support
- Zoom controls enabled
- Back navigation with arrow button
- Custom title bar for each portal

## Permissions

- **INTERNET**: Required for loading web content in WebViews

## Navigation Flow

```
Splash Screen (2s delay)
    ↓
Home Screen
    ├── Button 1 → LMS Portal (WebView)
    ├── Button 2 → Mobile App Development (WebView)
    └── Button 3 → AERP Login (WebView)
```

## Customization

### Changing URLs

Edit the URLs in `NavGraph.kt`:

```kotlin
WebViewScreen(
    title = "Your Title",
    url = "https://your-url.com",
    onNavigateBack = { navController.popBackStack() }
)
```

### Modifying College Logo

Replace or modify `app/src/main/res/drawable/college_logo.xml`

### Splash Screen Duration

Adjust the delay in `SplashScreen.kt`:

```kotlin
delay(2000) // milliseconds
```

## Dependencies

```gradle
implementation("androidx.navigation:navigation-compose:2.8.5")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.ui:ui")
```

## License

This project is created for educational purposes for KKW Institution.

## Support

For issues or questions, please contact the development team.
