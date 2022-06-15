package com.talex.game.testgame

import com.badlogic.gdx.Game

class TestGame : Game() {
    override fun create() {
        setScreen(TestScreen())
    }
}
