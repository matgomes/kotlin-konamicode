package com.konamicode

import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*


@Component
class WebSocket : WebSocketHandler{

    override fun handle(session: WebSocketSession): Mono<Void> {

        return session.send(Mono.just(session.textMessage("test")))
                      .and(session.receive().map(WebSocketMessage::getPayloadAsText)
                      .doOnNext(::println).log())
    }

    fun stuff(){

        val objects = Arrays.asList(0, 1, 2, 3, 4)

//        Flux<Integer> flux = Flux.range(0, 10);
        val flux = Flux.just(0, 1, 2, 3, 4, 0, 1, 2, 3, 5, 0, 1, 2, 3, 4)

        flux.window(5).flatMap { a ->

            a.zipWithIterable(objects).map { b -> b.t1 == b.t2 }.all { a -> a }.doOnNext { result ->

                println("Result - $result")
                if (result) println("done")
            }

        }.subscribe()
    }

}