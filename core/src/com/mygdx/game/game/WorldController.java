package com.mygdx.game.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.game.objects.DoubleJumpUpgrade;
import com.mygdx.game.game.objects.JetpackUpgrade;
import com.mygdx.game.game.objects.SlowDownUpgrade;
import com.mygdx.game.game.objects.SpringPlatform;
import com.mygdx.game.screens.MenuScreen;
import com.mygdx.game.util.CameraHelper;
import com.mygdx.game.util.Constants;

public class WorldController extends InputAdapter implements Disposable {
	private static final String TAG = WorldController.class.getName();

	public Level level;
	public int score;
	private boolean gameOver;
	private float gameOverDelay;
	// bounding boxes
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	public CameraHelper cameraHelper;
	public World b2World;

	// camera movement start time;
	private float startCamera;
	private float timeUntilCamera;

	private Game game;

	public WorldController(Game game) {
		this.game = game;
		init();
	}

	/**
	 * Initialize the world and level
	 */
	private void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		gameOverDelay = 0;
		initLevel();
		b2World.setContactListener(level.jeb);
	}

	// Initializes the level
	private void initLevel() {
		score = 0;
		level = new Level(Constants.LEVEL_01);
		gameOver = false;
		cameraHelper.setTarget(level.jeb);
		startCamera = 0;
		timeUntilCamera = 5;
		initPhysics();
	}

	/**
	 * Update the world
	 * 
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		// TimeLeft game over.
		if (isGameOver()) {
			gameOverDelay -= deltaTime;
			if(gameOverDelay < 0) {
				game.setScreen(new MenuScreen(game));
			}
		} else {
			handleInputJeb(deltaTime);
		}

		level.update(deltaTime);
		cameraHelper.update(deltaTime);
		b2World.step(deltaTime, 8, 3);

		// move the camera up slowly
		if (timeUntilCamera < startCamera)
			if (level.jeb.slowUpgrade)
				moveCameraUp(0.005f);
			else
				moveCameraUp(0.02f);
		else
			startCamera += deltaTime;

		// check if jeb is below camera
		// game over if jeb is lower than the camera
		if (level.jeb.position.y < cameraHelper.getPosition().y - 9 && !gameOver) {
			gameOver = true;
			gameOverDelay = Constants.GAME_OVER_DELAY;
		}
		
		// get the score
		score = level.jeb.maxHeight;
	}

	/**
	 * Handle Jeb
	 */
	private void handleInputJeb(float deltaTime) {
		if (cameraHelper.hasTarget(level.jeb)) {
			// Player Movement
			if (Gdx.input.isKeyPressed(Keys.LEFT) && !level.jeb.hittingEdge) {
				level.jeb.body.setLinearVelocity(-level.jeb.terminalVelocity.x, level.jeb.body.getLinearVelocity().y);
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT) && !level.jeb.hittingEdge) {
				level.jeb.body.setLinearVelocity(level.jeb.terminalVelocity.x, level.jeb.body.getLinearVelocity().y);
			} else if (Gdx.input.isKeyPressed(Keys.SPACE) && level.jeb.jetpackUpgrade) {
				// only if there is a jet pack upgrade
				level.jeb.body.setLinearVelocity(level.jeb.body.getLinearVelocity().x,
						level.jeb.terminalVelocity.y * 1.5f);
			} else if (Gdx.input.isKeyPressed(Keys.DOWN) && level.jeb.jetpackUpgrade) {
				// only if there is a jet pack upgrade
				level.jeb.body.setLinearVelocity(level.jeb.body.getLinearVelocity().x,
						-level.jeb.terminalVelocity.y * 0.5f);
			} else {
				// Execute auto-forward movement on non-desktop platform
				if (Gdx.app.getType() != ApplicationType.Desktop) {
					level.jeb.body.getLinearVelocity().x = level.jeb.terminalVelocity.x;
				}
			}

			// jeb Jump
			if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) {
				level.jeb.setJumping(true);
			} else {
				level.jeb.setJumping(false);
			}
		}
	}

	/**
	 * Allows for the camera to pan up
	 * 
	 * @param y
	 */
	private void moveCameraUp(float y) {
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(y);
	}

	/**
	 * Reset the game if R is pressed
	 */
	@Override
	public boolean keyUp(int keycode) {
		// Reset game world
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game world resetted");
		} else if (keycode == Keys.LEFT) {
			level.jeb.body.setLinearVelocity(0, level.jeb.body.getLinearVelocity().y);
		} else if (keycode == Keys.RIGHT) {
			level.jeb.body.setLinearVelocity(0, level.jeb.body.getLinearVelocity().y);
		}
		else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			game.setScreen(new MenuScreen(game));
		}
		return false;
	}

	// check if the game is over
	// Method check if game is over.
	public boolean isGameOver() {
		return gameOver;
	}

	// box2d stuff below
	// Initializes the physics for the in-game objects
	private void initPhysics() {
		if (b2World != null)
			b2World.dispose();
		b2World = new World(new Vector2(0, -9.81f), true);

		// platforms
		Vector2 origin = new Vector2();

		for (SpringPlatform platform : level.sPlatforms) {
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			bodyDef.position.set(platform.position);
			Body body = b2World.createBody(bodyDef);
			platform.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = platform.bounds.width / 2.0f;
			origin.y = platform.bounds.height / 2.0f;
			polygonShape.setAsBox(platform.bounds.width / 2.0f, platform.bounds.height / 2.0f, origin, 0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}

		// upgrades
		for (SlowDownUpgrade slow : level.slow) {
			System.out.println(slow);
			BodyDef upgrade = new BodyDef();
			upgrade.type = BodyType.StaticBody;
			upgrade.position.set(slow.position);
			Body body = b2World.createBody(upgrade);
			slow.body = body;
			slow.body.setUserData(slow);
			PolygonShape polygonShape = new PolygonShape();
			origin.x = slow.bounds.width / 2.0f;
			origin.y = slow.bounds.height / 2.0f;
			polygonShape.setAsBox(slow.bounds.width / 4.0f, slow.bounds.height / 4.0f, origin, 0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixtureDef.isSensor = true;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}
		for (DoubleJumpUpgrade doubles: level.doubles) {
			System.out.println(doubles);
			BodyDef upgrade = new BodyDef();
			upgrade.type = BodyType.StaticBody;
			upgrade.position.set(doubles.position);
			Body body = b2World.createBody(upgrade);
			doubles.body = body;
			doubles.body.setUserData(doubles);
			PolygonShape polygonShape = new PolygonShape();
			origin.x = doubles.bounds.width / 2.0f;
			origin.y = doubles.bounds.height / 2.0f;
			polygonShape.setAsBox(doubles.bounds.width / 4.0f, doubles.bounds.height / 4.0f, origin, 0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixtureDef.isSensor = true;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}
		for (JetpackUpgrade jet : level.jetpack) {
			System.out.println(jet);
			BodyDef upgrade = new BodyDef();
			upgrade.type = BodyType.StaticBody;
			upgrade.position.set(jet.position);
			Body body = b2World.createBody(upgrade);
			jet.body = body;
			jet.body.setUserData(jet);
			PolygonShape polygonShape = new PolygonShape();
			origin.x = jet.bounds.width / 2.0f;
			origin.y = jet.bounds.height / 2.0f;
			polygonShape.setAsBox(jet.bounds.width / 4.0f, jet.bounds.height / 4.0f, origin, 0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixtureDef.isSensor = true;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}

		// jeb box
		BodyDef jeb = new BodyDef();
		jeb.type = BodyType.DynamicBody;
		jeb.position.set(level.jeb.position);
		Body body = b2World.createBody(jeb);
		level.jeb.body = body;
		PolygonShape polygonShape = new PolygonShape();
		origin.x = level.jeb.bounds.width / 2.0f;
		origin.y = level.jeb.bounds.height / 2.0f;
		polygonShape.setAsBox(level.jeb.bounds.width / 2.5f, level.jeb.bounds.height / 2.5f, origin, 0);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		body.createFixture(fixtureDef);
		polygonShape.dispose();
	}

	@Override
	public void dispose() {
		if (b2World != null) {
			b2World.dispose();
		}
	}
}
