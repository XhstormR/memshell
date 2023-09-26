-optimizationpasses 5

-adaptclassstrings
-adaptresourcefilenames
-adaptresourcefilecontents
-allowaccessmodification

-obfuscationdictionary proguard-dict.txt
-classobfuscationdictionary proguard-dict.txt
-packageobfuscationdictionary proguard-dict.txt

-repackageclasses com.xhstormr.app

-keepattributes *Annotation*

-keepclassmembers enum * {
    public static **[] values();
}

-keepclasseswithmembers class * {
    public static void main(java.lang.String[]);
}

-keepclasseswithmembers class * {
    public static void agentmain(java.lang.String, java.lang.instrument.Instrumentation);
}

-keepclassmembers @interface net.bytebuddy.asm.Advice$OnMethod* {
    <methods>;
}

-keepclassmembers class com.xhstormr.app.Tracing {
    @net.bytebuddy.asm.Advice$OnMethod* <methods>;
}

-dontwarn net.bytebuddy.**
-dontwarn javax.servlet.**

-printusage ../build/usage.txt
-printmapping ../build/mapping.txt
