package com.reminder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.*

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    Locale.setDefault(Locale.ENGLISH) // For avoiding localization of javax validation error
    runApplication<Application>(*args)
}