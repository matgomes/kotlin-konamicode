package com.konamicode

import com.konamicode.KeyMap.*
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

enum class KeyMap(val value: String) {
    UP("38"), DOWN("40"), LEFT("37"), RIGHT("39"), B("66"), A("65")
}

@Component
class WebSocket: WebSocketHandler{

    val expectedOrder: List<String> = listOf(UP, UP, DOWN, DOWN, LEFT, RIGHT, LEFT, RIGHT, B, A).map { it.value }

    override fun handle(session: WebSocketSession): Mono<Void> {

        return session.send(
                handleMessage(session.receive()).flatMap { message(it, session) }
        )
    }

    fun handleMessage(messageFlux: Flux<WebSocketMessage>): Flux<Boolean> {

        return messageFlux.map { it.payloadAsText }
                          .buffer(10, 1)
                          .map { it == expectedOrder }
    }

    fun message(result: Boolean, session: WebSocketSession): Mono<WebSocketMessage> {
        return if(result) session.textMessage("KONAMI").toMono() else Mono.empty()
    }

}