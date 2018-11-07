package com.mygdx.game.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.game.objects.Jeb;
import com.mygdx.game.game.objects.Jeb.JUMP_STATE;
import com.mygdx.game.game.objects.SpringPlatform;
import com.mygdx.game.util.CameraHelper;
import com.mygdx.game.util.Constants;

public class WorldController extends InputAdapter {
	private static final String TAG = WorldController.class.getName();

	public Level level;
	public int score;
	// bounding boxes
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	public CameraHelper cameraHelper;

	public WorldController() {
		init();
	}

	private void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		initLevel();
	}

	// Initializes the level
	private void initLevel() {
		score = 0;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.jeb);
	}

	private Pixmap createProceduralPixmap(int width, int height) {
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);

		// Fill square with red color at 50% opacity
		pixmap.setColor(1, 0, 0, 0.5f);
		pixmap.fill();

		// Draw a yellow-colored X shape on square
		pixmap.setColor(1, 1, 0, 1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.drawLine(width, 0, 0, height);

		// Draw a cyan-colored border around square
		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawRectangle(0, 0, width, height);
		return pixmap;
	}

	public void update(float deltaTime) {

		// TimeLeft game over.
		if (isGameOver()) {
			init();
		} else {
			handleInputJeb(deltaTime);
		}

		level.update(deltaTime);
		testCollisions();
		cameraHelper.update(deltaTime);

	}

	/**
	 * Handle Jeb
	 */
	private void handleInputJeb(float deltaTime) {
		if (cameraHelper.hasTarget(level.jeb)) {
			// Player Movement
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				level.jeb.velocity.x = -level.jeb.terminalVelocity.x;
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				level.jeb.velocity.x = level.jeb.terminalVelocity.x;
			} else {
				// Execute auto-forward movement on non-desktop platform
				if (Gdx.app.getType() != ApplicationType.Desktop) {
					level.jeb.velocity.x = level.jeb.terminalVelocity.x;
				}
			}

			// Bunny Jump
			if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) {
				level.jeb.setJumping(true);
			} else {
				level.jeb.setJumping(false);
			}
		}
	}

	private void moveCameraUp(float x) {
		x += cameraHelper.getPosition().x;
		cameraHelper.setPosition(x);
	}

	@Override
	public boolean keyUp(int keycode) {
		// Reset game world
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game world resetted");
		}
		return false;
	}

	// check if the game is over
	// Method check if game is over.
	public boolean isGameOver() {
		return false;
	}

	/**
	 * Box2D collison with platforms
	 * 
	 * @param rock
	 */
	private void onCollisionJebWithPlatform(SpringPlatform platform) {
		Jeb jeb = level.jeb;
		float heightDifference = Math.abs(jeb.position.y - (platform.position.y + platform.bounds.height));
		if (heightDifference > 0.25f) {
			boolean hitLeftEdge = jeb.position.x > (platform.position.x + platform.bounds.width / 2.0f);
			if (hitLeftEdge) {
				jeb.position.x = platform.position.x + platform.bounds.width;
			} else {
				jeb.position.x = platform.position.x - jeb.bounds.width;
			}
			return;
		}
		;

		// Switch statement for jumpstate
		switch (jeb.jumpState) {
		case GROUNDED:
			break;
		case FALLING:
		case JUMP_FALLING:
			jeb.position.y = platform.position.y + platform.bounds.height + platform.origin.y;
			jeb.jumpState = JUMP_STATE.GROUNDED;
			break;
		case JUMP_RISING:
			jeb.position.y = platform.position.y + platform.bounds.height + platform.origin.y;
			break;
		}
	}

	private void testCollisions() {
		r1.set(level.jeb.position.x, level.jeb.position.y, level.jeb.bounds.width, level.jeb.bounds.height);

		// Test collision: Bunny Head <-> Rocks
		for (SpringPlatform platform : level.sPlatforms) {
			r2.set(platform.position.x, platform.position.y, platform.bounds.width, platform.bounds.height);
			if (!r1.overlaps(r2))
				continue;
			onCollisionJebWithPlatform(platform);
		}
	}
}
