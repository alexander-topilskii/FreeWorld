package com.talex.utils

import java.nio.charset.StandardCharsets
import java.util.*

object Kutils {

    fun lala(){
        println("GGGG: Kutils:lala")
    }

    fun loadResource(fileName: String?): String {
        var result: String
        Kutils::class.java.getResourceAsStream(fileName).use { `in` -> Scanner(`in`, StandardCharsets.UTF_8.name()).use { scanner -> result = scanner.useDelimiter("\\A").next() } }
        return result
    }
}


fun Float.toRadians(): Float = this * 0.017453292519943295f
fun Float.toDegrees(): Float = this * 57.29577951308232f
val PI: Float = 3.141592653589793f

val Number.f: Float
    get() = this.toFloat()
