package com.xhstormr.app

import java.lang.instrument.Instrumentation
import net.bytebuddy.agent.builder.AgentBuilder
import net.bytebuddy.asm.Advice
import net.bytebuddy.matcher.ElementMatchers

fun agentmain(args: String, inst: Instrumentation) {

    val advice = Advice
        .withCustomMapping()
        .bind(Value::class.java, args)
        .to(Tracing::class.java)
        .on(ElementMatchers.named("internalDoFilter"))

    AgentBuilder.Default()
        // .with(AgentBuilder.Listener.StreamWriting.toSystemOut().withErrorsOnly())
        .with(AgentBuilder.RedefinitionStrategy.REDEFINITION)
        .type(ElementMatchers.named("org.apache.catalina.core.ApplicationFilterChain"))
        .transform { builder, _, _, _ -> builder.visit(advice) }
        .installOn(inst)

    println("OK")
}

/*
https://github.com/apache/tomcat/blob/master/java/org/apache/catalina/core/ApplicationFilterChain.java
*/
