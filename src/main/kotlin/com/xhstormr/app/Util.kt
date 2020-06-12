package com.xhstormr.app

import java.io.File

fun readProcessOutput(command: String) = Runtime.getRuntime()
    .exec("cmd /c %s".format(command))
    .inputStream
    .bufferedReader()
    .useLines { it.toList() }

fun getCurrentJar() = File(Tracing::class.java.protectionDomain.codeSource.location.toURI())
