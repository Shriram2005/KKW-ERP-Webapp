# ============================================
# KKW ERP Webapp - ProGuard Rules
# ============================================

# Keep line numbers for crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ============================================
# WebView
# ============================================
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep WebView related classes
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
    public void *(android.webkit.WebView, java.lang.String);
}

# ============================================
# JSON Parsing (org.json)
# ============================================
-keep class org.json.** { *; }

# ============================================
# Update Checker
# ============================================
-keep class com.kkwieer.erpwebapp.update.UpdateChecker$UpdateInfo { *; }

# ============================================
# Data Classes (JSON Serialization)
# ============================================
-keep class com.kkwieer.erpwebapp.data.PortalLink { *; }
-keep class com.kkwieer.erpwebapp.data.PortalLink$Companion { *; }

# ============================================
# Kotlin
# ============================================
-dontwarn kotlin.**
-keep class kotlin.Metadata { *; }

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# ============================================
# Compose
# ============================================
-dontwarn androidx.compose.**

# ============================================
# General Android
# ============================================
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
