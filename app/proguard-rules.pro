# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in J:\Program Files (x86)\android-studio\android-sdk-windows/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify

-keep public class * extends android.app.Activity
-keep public class * extends com.brotherjing.danmakubay.base.BaseActionBarActivity
-keep public class com.brotherjing.danmakubay.base.BaseActionBarActivity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.perference.Preference
-keep public class * extends android.view.ViewGroup
-keep public class * extends android.view.View
-keep public class * extends android.widget.TextView
-keep public class com.android.vending.licensing.ILicensingService

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.brotherjing.danmakubay.utils.beans.** { *; }
##---------------End: proguard configuration for Gson  ----------

-keep class com.android.support.** {*;}
-keep class nostra13.universalimageloader.** {*;}
-keep class google.code.gson.** {*;}
-keep public class com.tencent.bugly.**{*;}

## apache

-dontwarn org.apache.http.**
-keep class org.apache.http.** { *;}

# # -------------------------------------------
# #  ######## greenDao»ìÏý  ##########
# # -------------------------------------------
-keep class de.greenrobot.dao.** {*;}
-keep class com.greendao.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties

## xiaomi update: SDK?????????????????????????????R.java?????????proguard???/??????apk??proguard????R.java??
-keep public class com.brotherjing.danmakubay.R$*{
    public static final int *;
}

-dontwarn android.net.http.SslError
-keep public class android.webkit.WebViewClient
-dontwarn android.webkit.WebView
-dontwarn android.webkit.WebViewClient

-keepattributes Signature
-keepattributes *Annotation*

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.perference.Preference
-keep public class * extends android.view.ViewGroup
-keep public class com.android.vending.licensing.ILicensingService
#-keep public class android.view.** {*;}
-keep class com.joanzapata.** { *; }

-keep class com.android.support.** {*;}
-keep class nostra13.universalimageloader.** {*;}
-keep class google.code.gson.** {*;}
-keep class com.jakewharton.** {*;}
-keep class com.roomorama.** {*;}
-keep class com.joanzapata.android.** {*;}
-keep class com.github.alamkanak.** {*;}
-keep class com.github.PhilJay.** {*;}

-keep class org.jsoup.** {*;}
-keep class com.google.** {*;}
-keep class net.sourceforge.zbar.** {*;}
-keep class com.google.zxing.** {*;}

-keep class cn.leancloud.android.** {*;}
-keep public class com.tencent.bugly.**{*;}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
##---------------End: proguard configuration for Gson  ----------

-keepclassmembers class org.alljoyn.bus.* {
    public *;
}
-keep class org.alljoyn.bus.** {*;}
-dontwarn org.alljoyn.bus.**

# # -------------------------------------------
# #  ######## greenDao»ìÏý  ##########
# # -------------------------------------------
-keep class de.greenrobot.dao.** {*;}
-keep class com.greendao.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties

## proguard rules for butterknife, from stack over flow
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keepattributes *Annotation*,EnclosingMethod,Signature
-keepnames class com.fasterxml.jackson.** { *; }
 -dontwarn com.fasterxml.jackson.databind.**
 -keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
 public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *; }
-keep public class your.class.** {
  public void set*(***);
  public *** get*();
}

##---------------Begin: proguard configuration for fastjson  ----------

-keepnames class * implements java.io.Serializable
-keep public class * implements java.io.Serializable {
        public *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-dontwarn android.support.**
-dontwarn com.alibaba.fastjson.**

-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses

-keep class com.alibaba.fastjson.** { *; }

-keepclassmembers class * {
public <methods>;
}

## apache

-dontwarn org.apache.http.**
-keep class org.apache.http.** { *;}

## amap

-keep   class com.amap.api.mapcore.**{*;}
-keep   class com.amap.api.maps.**{*;}
-dontwarn com.amap.api.**
-keep   class com.autonavi.amap.mapcore.*{*;}
#Location
-keep   class com.amap.api.location.**{*;}
-keep   class com.aps.**{*;}
#Service
-keep   class com.amap.api.services.**{*;}


## ptr
-keep class in.srain.cube.** {*;}
-dontwarn in.srain.cube.**


-keep class com.squareup.okhttp.** {*;}
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**


-dontwarn android.net.http.SslError
-keep public class android.webkit.WebViewClient
-dontwarn android.webkit.WebView
-dontwarn android.webkit.WebViewClient

-keepattributes Signature
-keepattributes *Annotation*