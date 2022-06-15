package com.talex.game.freeworld

import com.badlogic.gdx.Game

class FreeWorldGame : Game() {
    override fun create() {
        setScreen(FreeWorldScreen())
    }
}