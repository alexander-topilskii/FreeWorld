package com.talex.game.tetris;

import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {
	@Override
	public void create () {
        this.setScreen(new MainScreen());
	}
}
