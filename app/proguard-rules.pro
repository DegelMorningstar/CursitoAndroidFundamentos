# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Modelos que Firestore deserializa por reflexión (Contacto, Usuario, Mensaje, Chat...)
-keep class com.yaeldev.cursitodefundamentosandroid.**.domain.models.** { *; }
-keep class com.yaeldev.cursitodefundamentosandroid.**.data.**.dto.** { *; }

# kotlinx-serialization (rutas @Serializable)
-keepattributes *Annotation*, InnerClasses
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> { *** Companion; }
-keepclasseswithmembers class ** { kotlinx.serialization.KSerializer serializer(...); }

# Retrofit + converter kotlinx-serialization
-keepattributes Signature, Exceptions
-dontwarn okhttp3.**
-dontwarn retrofit2.**