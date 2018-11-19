package com.mygdx.game.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.game.objects.DoubleJumpUpgrade;
import com.mygdx.game.game.objects.Jeb;
import com.mygdx.game.game.objects.JetpackUpgrade;
import com.mygdx.game.game.objects.SlowDownUpgrade;
import com.mygdx.game.game.objects.Jeb.JUMP_STATE;
import com.mygdx.game.game.objects.SpringPlatform;
import com.mygdx.game.util.CameraHelper;
import com.mygdx.game.util.Constants;

public class WorldController extends InputAdapter implements Disposable {
	private static final String TAG = WorldController.class.getName();

	public Level level;
	public int score;
	// bounding boxes
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	public CameraHelper cameraHelper;
	public World b2World;

	// camera movement start time;
	private float startCamera;
	private float timeUntilCamera;

	public WorldController() {
		init();
	}

	/**
	 * Initialize the world and level
	 */
	private void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		initLevel();
		b2World.setContactListener(level.jeb);
	}

	// Initializes the level
	private void initLevel() {
		score = 0;
		level = new Level(Constants.LEVEL_01);
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
			init();
		} else {
			handleInputJeb(deltaTime);
		}

		level.update(deltaTime);
		testCollisions();
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
				level.jeb.body.setLinearVelocity(level.jeb.body.getLinearVelocity().x, level.jeb.terminalVelocity.y*1.5f);
			} else if (Gdx.input.isKeyPressed(Keys.DOWN) && level.jeb.jetpackUpgrade) {
				// only if there is a jet pack upgrade
				level.jeb.body.setLinearVelocity(level.jeb.body.getLinearVelocity().x, -level.jeb.terminalVelocity.y*0.5f);
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
		}
		else if(keycode == Keys.LEFT) {
			level.jeb.body.setLinearVelocity(0, 0);
		}
		else if(keycode == Keys.RIGHT) {
			level.jeb.body.setLinearVelocity(0, 0);
		}
		return false;
	}

	// check if the game is over
	// Method check if game is over.
	public boolean isGameOver() {
		return false;
	}

	/**
	 * Collision detection for each object in the game below
	 * 
	 * @param slow
	 */
	private void onCollisionJebWithSlow(SlowDownUpgrade slow) {
		slow.collected = true;
		level.jeb.setSlowUpgrade(true);
	}

	private void onCollisionJebWithJetpack(JetpackUpgrade jetpack) {
		jetpack.collected = true;
		level.jeb.setJetpackUpgrade(true);
	}

	private void onCollisionJebWithDoubleJump(DoubleJumpUpgrade jump) {
		jump.collected = true;
		level.jeb.setDoubleJumpUpgrade(true);
	}

	/**
	 * Check to see if jeb is in contact with any of the game objects
	 */
	private void testCollisions() {
		r1.set(level.jeb.position.x, level.jeb.position.y, level.jeb.bounds.width, level.jeb.bounds.height);
		// check if jeb hit a slow down time upgrade
		for (SlowDownUpgrade slow : level.slow) {
			r2.set(slow.position.x, slow.position.y, slow.bounds.width, slow.bounds.height);
			if (!r1.overlaps(r2))
				continue;
			onCollisionJebWithSlow(slow);
		}

		// check if jeb hit a jetpack upgrade
		for (JetpackUpgrade jetpack : level.jetpack) {
			r2.set(jetpack.position.x, jetpack.position.y, jetpack.bounds.width, jetpack.bounds.height);
			if (!r1.overlaps(r2))
				continue;
			onCollisionJebWithJetpack(jetpack);
		}

		// check if jeb hit a double jump upgrade
		for (DoubleJumpUpgrade jump : level.doubles) {
			r2.set(jump.position.x, jump.position.y, jump.bounds.width, jump.bounds.height);
			if (!r1.overlaps(r2))
				continue;
			onCollisionJebWithDoubleJump(jump);
		}
	}

	// box2d stuff below
	// Initializes the physics for the in-game objects
	private void initPhysics() {
		if (b2World != null)
			b2World.dispose();
		b2World = new World(new Vector2(0, -9.81f), true);

		// Rocks
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
