package com.chekak.messenger.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MessengerServerApplication

fun main(args: Array<String>) {
    runApplication<MessengerServerApplication>(*args)
}
