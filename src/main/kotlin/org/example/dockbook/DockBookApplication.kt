package org.example.dockbook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DockBookApplication

fun main(args: Array<String>) {
    runApplication<DockBookApplication>(*args)
}
