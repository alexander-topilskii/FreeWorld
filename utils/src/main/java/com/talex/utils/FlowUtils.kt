package com.talex.utils

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

private object Unitialized

public fun <A, B, R> Flow<A>.withLatestFrom(
    other: Flow<B>,
    transform: suspend (A, B) -> R
): Flow<R> {
    return flow {
        coroutineScope {
            val otherValues = Channel<Any>(Channel.CONFLATED)
            launch(start = CoroutineStart.UNDISPATCHED) {
                other.collect {
                    return@collect otherValues.send(it ?: NULL_VALUE)
                }
            }

            var lastValue: Any? = null
            collect { value ->
                otherValues
                    .tryReceive()
                    .onSuccess { lastValue = it }

                emit(
                    transform(
                        value,
                        NULL_VALUE.unbox(lastValue ?: return@collect)
                    ),
                )
            }
        }
    }
}

@Suppress("ClassName")
public object NULL_VALUE {
    @Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
    public inline fun <T> unbox(v: Any?): T = if (this === v) null as T else v as T
}

@Suppress("NOTHING_TO_INLINE")
public inline fun <A, B> Flow<A>.withLatestFrom(other: Flow<B>): Flow<Pair<A, B>> =
    withLatestFrom(other) { a, b -> a to b }
//
//fun <T, V> Flow<T>.withLatestFrom(other: Flow<V>): Flow<Pair<T, V>> {
//    return let { upstream ->
//        upstream.withLatestFrom(other, { t, v -> Pair(t, v) })
//    }
//}


class CoroutineImpl : CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob()
}
