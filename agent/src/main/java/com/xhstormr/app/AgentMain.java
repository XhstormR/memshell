package com.xhstormr.app;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.Map;

public class AgentMain {

    public static void premain(String args, Instrumentation inst) {
        main(args, inst);
    }

    public static void agentmain(String args, Instrumentation inst) {
        main(args, inst);
    }

    private static void main(String args, Instrumentation inst) {
        String clazz = "com.xhstormr.app.AgentKt";
        String method = "agent";

        Map<String, String> options = Util.parseOptions(args);
        String pass = options.get("pass");
        String bootJar = options.get("bootJar");

        try {
            new StandaloneAgentClassloader(new URL[]{new File(bootJar).toURI().toURL()})
                .loadClass(clazz)
                .getMethod(method, String.class, Instrumentation.class)
                .invoke(null, pass, inst);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
