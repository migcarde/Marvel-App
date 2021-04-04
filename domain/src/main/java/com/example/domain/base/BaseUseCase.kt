package com.example.domain.base

import com.example.commons.data.types.Either
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseUseCase<F, S, P> : CoroutineScope {

    abstract suspend fun run(params: P): Either<F, S>

    override val coroutineContext: CoroutineContext
        get() = Job()

    open operator fun invoke(params: P, result: (Either<F, S>) -> Unit = {}) {
        val job: Deferred<Either<F, S>> = async(coroutineContext + Dispatchers.IO) { run(params) }
        launch(coroutineContext + Dispatchers.Main) { result.invoke(job.await()) }
    }
}