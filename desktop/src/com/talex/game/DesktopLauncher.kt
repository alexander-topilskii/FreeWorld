package com.talex.game

import kotlin.jvm.JvmStatic
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import main.java.com.talex.domain.FreeWorldGame

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = Lwjgl3ApplicationConfiguration().apply {
            setForegroundFPS(60)
            setTitle("Free World")
            setWindowedMode(1500, 1500)
        }

        Lwjgl3Application(FreeWorldGame(), config)
    }
}