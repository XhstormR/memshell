package com.xhstormr.app;

import java.net.URL;
import java.net.URLClassLoader;

public class StandaloneAgentClassloader extends URLClassLoader {
    public StandaloneAgentClassloader(URL[] urls) {
        super(urls, ClassLoader.getSystemClassLoader().getParent());
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        final Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) {
            return loadedClass;
        }

        // 优先从parent（SystemClassLoader）里加载系统类，避免抛出ClassNotFoundException
        if (name != null && (name.startsWith("sun.") || name.startsWith("java."))) {
            return super.loadClass(name, resolve);
        }
        try {
            Class<?> aClass = findClass(name);
            if (resolve) {
                resolveClass(aClass);
            }
            return aClass;
        } catch (Exception e) {
            // ignore
        }
        return super.loadClass(name, resolve);
    }
}

/*
https://github.com/alibaba/arthas/blob/master/arthas-agent-attach/src/main/java/com/taobao/arthas/agent/attach/AttachArthasClassloader.java
*/
