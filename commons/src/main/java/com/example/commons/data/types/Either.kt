package com.example.commons.data.types

sealed class Either<out L, out R> {
    data class Left<out L>(val l: L): Either<L, Nothing>()
    data class Right<out R>(val r: R): Either<Nothing, R>()

    val isLeft get() = this is Left<L>
    val isRight get() = this is Right<R>

    fun fold(left: (L) -> Any = {}, right: (R) -> Any = {}): Any = when (this) {
        is Left -> left(l)
        is Right -> right(r)
    }

    fun toRightValueOrNull(): R? = when (this) {
        is Left -> null
        is Right -> r
    }

    fun toLeftValueOrNull(): L? = when (this) {
        is Left -> l
        is Right -> null
    }
}