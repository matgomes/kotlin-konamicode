package com.konamicode

import com.konamicode.KeyMap.*
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

enum class KeyMap(val value: String) {
    UP("38"), DOWN("40"), LEFT("37"), RIGHT("39"), B("66"), A("65")
}

@Component
class WebSocket: WebSocketHandler{

    val expectedOrder = listOf(UP, UP, DOWN, DOWN, LEFT, RIGHT, LEFT, RIGHT, B, A).map { it.value }

    override fun handle(session: WebSocketSession): Mono<Void> {

        return session.send(
                handleMessage(session.receive()).map { message(session) }
        )
    }

    fun handleMessage(messageFlux: Flux<WebSocketMessage>): Flux<MutableList<String>> {

        return messageFlux.map { it.payloadAsText }
                          .buffer(10, 1)
                          .filter { it == expectedOrder }
    }

    fun message(session: WebSocketSession): WebSocketMessage {
        return "KONAMI".monoTextMessage(session)
    }

}

fun String.monoTextMessage(session: WebSocketSession): WebSocketMessage {
    return session.textMessage(this)
}