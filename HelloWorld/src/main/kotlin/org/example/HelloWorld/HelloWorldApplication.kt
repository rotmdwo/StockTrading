package org.example.HelloWorld

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class HelloWorldApplication

fun main() {
    runApplication<HelloWorldApplication>()
}