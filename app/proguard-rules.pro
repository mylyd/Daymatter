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

-dontwarn com.facebook.**
-dontwarn android.content.pm.**
-dontwarn android.support.v4.**
-dontwarn android.database.sqlite.**
-dontwarn android.content.**
-dontwarn com.google.**
-dontwarn com.android.volley.**
-dontwarn com.google.android.**
-dontwarn com.crashlytics.**
-dontwarn io.fabric.sdk.android.**
-dontwarn retrofit.**
-dontwarn okio.**

-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keep class android.content.Context { *;}
-keep class com.google.android.gms.common.** { *; }
-keep class com.google.android.gms.ads.identifier.** { *; }
-keep class com.google.** { *; }
-keep class com.facebook.** { *; }
-keep class com.android.vending.billing.**

-keep class retrofit2.** { *; }
-keep class com.mobo.daymatter.network.**{*;}
-keep class security.mobo.security.**{*;}
-keep class ad.mobo.common.network.**{*;}

-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}

-ignorewarnings
