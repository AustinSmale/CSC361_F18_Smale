package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.GamePreferences;

public class WorldRenderer implements Disposable {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController;
	private static final boolean DEBUG = false;
	private Box2DDebugRenderer debug;
	private OrthographicCamera gui;
	
	public WorldRenderer(WorldController worldController) {
		this.worldController = worldController;
		init();
	}

	/**
	 * Initialize the camera and set its positions
	 */
	private void init() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		// static gui camera
		gui = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		gui.position.set(0,0,0);
		gui.setToOrtho(true);
		gui.position.set(gui.viewportWidth / 2, gui.viewportHeight / 2, 0);
		gui.update();
		debug = new Box2DDebugRenderer();
	}

	/**
	 * Render out the game world
	 */
	public void render() {
		renderWorld(batch);
		renderGui(batch);
	}

	private void renderGui(SpriteBatch batch2) {
		batch.setProjectionMatrix(gui.combined);
		batch.begin();
		renderGuiScore(batch);
		if (GamePreferences.instance.showFpsCounter)
			renderGuiFpsCounter(batch);
		// draw game over text
		renderGuiGameOverMessage(batch);
		batch.end();
	}

	private void renderGuiScore(SpriteBatch batch) {
		float x = -15;
		float y = -15;
		Assets.instance.fonts.defaultNormal.draw(batch, "" + worldController.score, x + 30, y + 30);
	}

	private void renderGuiFpsCounter(SpriteBatch batch) {
		float x = gui.viewportWidth - 55;
		float y = gui.viewportHeight - 15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = Assets.instance.fonts.fps;
		if (fps >= 45) {
			// 45 or more FPS show up in green
			fpsFont.setColor(0, 1, 0, 1);
		} else if (fps >= 30) {
			// 30 or more FPS show up in yellow
			fpsFont.setColor(1, 1, 0, 1);
		} else {
			// less than 30 FPS show up in red
			fpsFont.setColor(1, 0, 0, 1);
		}
		fpsFont.draw(batch, "FPS: " + fps, x, y);
		fpsFont.setColor(1, 1, 1, 1); // white
	}

	private void renderGuiGameOverMessage(SpriteBatch batch) {
		float x = gui.viewportWidth / 2;
		float y = gui.viewportHeight / 2;
		if (worldController.isGameOver() && worldController.gameOverDelay < 1.64f) {
			BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
			fontGameOver.setColor(1, 0, 0, 1);
			fontGameOver.draw(batch, "GAME OVER", x, y, 1, Align.center, false);
			fontGameOver.setColor(1, 1, 1, 1);
		}	
	}

	/**
	 * Used to render the world in the game using a SpriteBatch
	 * 
	 * @param batch
	 */
	private void renderWorld(SpriteBatch batch) {
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
		if(DEBUG) {
			debug.render(worldController.b2World, camera.combined);
		}
	}

	public void resize(int width, int height) {
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}