package main.java.com.talex.minicraft;

import com.badlogic.gdx.Game;

public class MinicraftGame extends Game {
	public void create() {
		this.setScreen(new MainScreen());
	}
}

