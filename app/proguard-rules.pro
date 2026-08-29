# Jetpack Compose inline value classes protection (TextUnit, Dp, Color)
# Prevents R8 aggressive optimizations from corrupting 64-bit packed value bitmasks
-keepclassmembers class androidx.compose.ui.unit.** { *; }
-keepclassmembers class androidx.compose.ui.graphics.Color { *; }