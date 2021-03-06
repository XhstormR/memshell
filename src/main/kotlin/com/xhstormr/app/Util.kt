package com.xhstormr.app

import java.io.File
import java.lang.management.ManagementFactory

fun readProcessOutput(command: String) = Runtime.getRuntime()
    .exec(command)
    .inputStream
    .bufferedReader()
    .useLines { it.toList() }

fun getCurrentJar() = File(clazz<Tracing>().protectionDomain.codeSource.location.toURI())

fun getCurrentPID() =
    // ProcessHandle.current().pid() // since 9
    ManagementFactory.getRuntimeMXBean().name.substringBefore('@').toLong() // since 1.5

/**
 * 内联：获得带泛型的 Class，eg: Class<X<Y>>
 */
inline fun <reified T> clazz() = T::class.java
