package com.xhstormr.app

import net.bytebuddy.agent.ByteBuddyAgent

fun main(args: Array<String>) {

    if (args.size != 2) {
        println("Usage: memshell <pid> <pass>")
        return
    }

    val pid = args[0]
    val arg = args[1]
    val agent = getCurrentJar()

    ByteBuddyAgent.attach(agent, pid, arg)
}

/*
for (vmd in VirtualMachine.list()) {
    if (vmd.id() != pid) continue
    println(vmd)

    val vm = VirtualMachine.attach(vmd)
    try {
        vm.loadAgent(agent.path, pass)
    } finally {
        vm.detach()
    }
}
*/
