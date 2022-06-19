package main.java.com.talex.utils;

import com.badlogic.gdx.Game;

public class TetrisGame extends Game {
	@Override
	public void create () {
        this.setScreen(new MainScreen());
	}
}
