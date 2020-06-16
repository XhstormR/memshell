-optimizationpasses 5

-adaptclassstrings
-adaptresourcefilenames
-adaptresourcefilecontents
-allowaccessmodification

-obfuscationdictionary proguard-dict.txt
-classobfuscationdictionary proguard-dict.txt
-packageobfuscationdictionary proguard-dict.txt

-repackageclasses com.xhstormr.app

-keepclassmembers enum * {
    public static **[] values();
}

-keepclasseswithmembers class * {
    public static void main(java.lang.String[]);
}

-keepclasseswithmembers class * {
    public static void agentmain(java.lang.String, java.lang.instrument.Instrumentation);
}

-keepattributes *Annotation*

-keepclassmembers @interface net.bytebuddy.asm.Advice$OnMethod* {
    <methods>;
}

-keepclassmembers class com.xhstormr.app.Tracing {
    @net.bytebuddy.asm.Advice$OnMethod* <methods>;
}

-dontwarn edu.umd.cs.findbugs.annotations.SuppressFBWarnings
-dontwarn net.bytebuddy.agent.**
-dontwarn javax.servlet.**
