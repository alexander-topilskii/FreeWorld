package main.java.com.talex.domain

import com.badlogic.gdx.Game

class FreeWorldGame : Game() {
    override fun create() {
        setScreen(FreeWorldScreen())
    }
}