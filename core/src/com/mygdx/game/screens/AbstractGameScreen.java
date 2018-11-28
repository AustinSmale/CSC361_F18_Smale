package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.game.Assets;

/**
 * Abstract Game Screen class for each game screen to implement 
 * @author Austin
 *
 */
public abstract class AbstractGameScreen implements Screen {
	
	protected Game game;
	
	/**
	 * Set the game we want to use
	 * @param game
	 */
	public AbstractGameScreen(Game game) {
		this.game = game;
	}
	
	/**
	 * Abstract methods needed
	 */
	public abstract void render(float deltaTime);

	public abstract void resize(int width, int height);

	public abstract void show();

	public abstract void hide();

	public abstract void pause();

	/**
	 * Initialize the assets in the game
	 */
	public void resume() {
		Assets.instance.init(new AssetManager());
	}

	/**
	 * Dispose of the asset objects in the game
	 */
	public void dispose() {
		Assets.instance.dispose();
	}

}
