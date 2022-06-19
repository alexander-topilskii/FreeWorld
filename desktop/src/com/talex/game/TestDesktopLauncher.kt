package com.talex.game

import kotlin.jvm.JvmStatic
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.talex.game.testgame.TestGame

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
object TestDesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = Lwjgl3ApplicationConfiguration()
        config.setForegroundFPS(60)
        config.setWindowedMode(1600, 1600)
        config.setTitle("Free World")
        Lwjgl3Application(TestGame(), config)
    }
}