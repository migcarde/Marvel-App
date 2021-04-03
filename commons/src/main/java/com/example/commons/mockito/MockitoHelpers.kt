package com.example.commons.mockito

import org.mockito.Mockito

// Creates Mockito.eq() as nullable type because Any type is nullable and could do calls with Unit as parameter
fun <Any> eq(obj: Any): Any = Mockito.eq(obj)

// Creates Mockito.eq() as nullable type because Any type is nullable
fun <Any> any(): Any = Mockito.any()