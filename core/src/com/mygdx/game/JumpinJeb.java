package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.game.Assets;
import com.mygdx.game.game.WorldController;
import com.mygdx.game.game.WorldRenderer;
import com.mygdx.game.screens.MenuScreen;
import com.mygdx.game.util.AudioManager;
import com.mygdx.game.util.GamePreferences;

public class JumpinJeb extends Game {
	private static final String TAG = JumpinJeb.class.getName();

	private WorldController worldController;
	private WorldRenderer worldRenderer;
	private boolean paused;

	
	/**
	 * Initialize the window and game  
	 */
	@Override 
	public void create() {
		// Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		// Load Assets
		Assets.instance.init(new AssetManager());
		
		// load preferences and play music
		GamePreferences.instance.load();
		AudioManager.instance.play(Assets.instance.music.song);
		
		//Start the game at the menu screen
		setScreen(new MenuScreen(this));
	}
	/**
	@Override
	public void create() {
		// Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		//load the assets
		Assets.instance.init(new AssetManager());
		// Initialize controller and renderer
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);
		// Game world is active on start
		paused = false;
	}

	@Override
	public void render() {
		// Do not update game world when paused
		if (!paused) {
			// Update game world by the time that has passed
			// since last rendered frame.
			worldController.update(Gdx.graphics.getDeltaTime());
		}
		// Sets the clear green screen color to: Cornflower Blue
		Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Render game world to screen
		worldRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, width);
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		paused = false;
	}

	@Override
	public void dispose() {
		worldRenderer.dispose();
	}*/
}
