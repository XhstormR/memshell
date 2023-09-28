package com.xhstormr.app

import org.springframework.boot.system.ApplicationHome
import java.io.InputStream
import java.lang.management.ManagementFactory
import java.net.URLEncoder
import kotlin.io.path.toPath

private val clazz = clazz<Tracing>()

fun getCurrentJar() = ApplicationHome(clazz).source

fun getCurrentPID() =
    // ProcessHandle.current().pid() // since 9
    ManagementFactory.getRuntimeMXBean().name.substringBefore('@').toLong() // since 1.5

fun getSystemResource(name: String) =
    (ClassLoader.getSystemResource(name) ?: clazz.classLoader.getResource(name))?.toURI()?.toPath()

fun getSystemResourceAsStream(name: String): InputStream? =
    ClassLoader.getSystemResourceAsStream(name)
        ?: clazz.classLoader.getResourceAsStream(name)

fun renderOptions(options: Map<String, String>): String {
    if (options.isEmpty()) return ""

    return options
        .mapValues { (_, v) -> URLEncoder.encode(v, Charsets.UTF_8) }
        .toList()
        .joinToString("&") { (k, v) -> "$k=$v" }
}

/**
 * 内联：获得带泛型的 Class，eg: Class<X<Y>>
 */
inline fun <reified T> clazz() = T::class.java

/**
 * 内联：隐藏 StackTrace
 */
@Suppress("NOTHING_TO_INLINE")
inline fun readProcessOutput(command: String) = Runtime.getRuntime()
    .exec(command)
    .inputStream
    .bufferedReader()
    .useLines { it.toList() }
