package com.talex.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.talex.game.freeworld.FreeWorldGame;
import com.talex.game.testgame.TestGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class TestDesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Free World");
		new Lwjgl3Application(new TestGame(), config);
	}
}
