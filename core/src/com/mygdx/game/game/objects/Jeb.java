package com.mygdx.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.game.Assets;
import com.mygdx.game.util.Constants;

public class Jeb extends AbstractGameObject implements ContactListener {
	public static final String TAG = Jeb.class.getName();
	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.2f;

	public enum VIEW_DIRECTION {
		LEFT, RIGHT
	}

	public enum JUMP_STATE {
		GROUNDED, JUMP
	}

	private TextureRegion regPlayer;
	private TextureRegion jetpack;
	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean slowUpgrade;
	public float slowTimeLeft;
	public boolean jetpackUpgrade;
	public float jetpackTimeLeft;
	public boolean doubleJump;
	public boolean hittingEdge;

	public Jeb() {
		init();
	}

	/**
	 * Initialize Jeb with all of his states
	 */
	public void init() {
		dimension.set(1, 1);
		regPlayer = Assets.instance.player.player;
		// Center image on game object
		origin.set(dimension.x / 2, dimension.y);
		// Bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		// Set physics values
		terminalVelocity.set(4.0f, 7.5f);
		// View direction
		viewDirection = VIEW_DIRECTION.LEFT;
		// Jump state
		jumpState = JUMP_STATE.GROUNDED;
		timeJumping = 0;

		// get the jetpack
		jetpack = Assets.instance.powerUps.jetpackJeb;

		// power up stuff
		slowUpgrade = false;
		slowTimeLeft = 0;
		jetpackUpgrade = false;
		jetpackTimeLeft = 0;
		doubleJump = false;

		// edge detection
		hittingEdge = false;
	}

	/**
	 * If the jump key was pressed make the bunny head jump in game. Keeps track of
	 * the states of the jump of the bunny head
	 * 
	 * @param jumpKeyPressed
	 *            whether the key was pressed or not
	 */
	public void setJumping(boolean jumpKeyPressed) {
		// you can jump if you are not jumping or have dobule jump
		if (jumpKeyPressed && (jumpState != JUMP_STATE.JUMP || doubleJump)) {
			body.setLinearVelocity(body.getLinearVelocity().x, terminalVelocity.y);
			jumpState = JUMP_STATE.JUMP;
		}
	}

	/**
	 * Render in jeb
	 */
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;
		// render the jetpack if jeb has it before jeb
		if (jetpackUpgrade) {
			reg = jetpack;
			batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x,
					scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
					false, false);
		}

		// set the region to jeb
		reg = regPlayer;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x,
				scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
				viewDirection == VIEW_DIRECTION.LEFT, false);
	}

	public void setSlowUpgrade(boolean pickedUp) {
		slowUpgrade = pickedUp;
		if (pickedUp)
			slowTimeLeft = Constants.SLOW_DURATION;
	}

	public void setJetpackUpgrade(boolean pickedUp) {
		jetpackUpgrade = pickedUp;
		if (pickedUp)
			jetpackTimeLeft = Constants.JETPACK_DURATION;
	}

	public void setDoubleJumpUpgrade(boolean pickedUp) {
		doubleJump = pickedUp;
	}

	/**
	 * Call the superclass's update, but also check for power ups and update their
	 * timers
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		// update slow down time
		if (slowTimeLeft > 0) {
			slowTimeLeft -= deltaTime;
			// disable to power up
			if (slowTimeLeft < 0) {
				slowTimeLeft = 0;
				setSlowUpgrade(false);
			}
		}
		// update jetpack time
		if (jetpackTimeLeft > 0) {
			jetpackTimeLeft -= deltaTime;
			// disable to power up
			if (jetpackTimeLeft < 0) {
				jetpackTimeLeft = 0;
				setJetpackUpgrade(false);
			}
		}
	}

	/**
	 * Jeb touches a platform
	 */
	@Override
	public void beginContact(Contact contact) {
		// the platform
		Fixture a = contact.getFixtureA();
		// jeb
		Fixture b = contact.getFixtureB();

		// check if jeb is standing on top of a platform
		if (b.getBody().getPosition().y - a.getBody().getPosition().y >= 0.89f) {
			System.out.println("grounded");
			jumpState = JUMP_STATE.GROUNDED;
			hittingEdge = false;
		}

		// check if jeb is hitting the left or right edge of a platform, move him down
		else if (b.getBody().getPosition().y - a.getBody().getPosition().y <= 0.89f
				&& b.getBody().getPosition().y - a.getBody().getPosition().y >= -0.89f) {
			hittingEdge = true;
		}
		else {
			hittingEdge = false;
		}
	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}
}
