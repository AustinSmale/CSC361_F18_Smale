package com.mygdx.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.game.Assets;
import com.mygdx.game.util.Constants;

public class Jeb extends AbstractGameObject {
	public static final String TAG = Jeb.class.getName();
	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;

	public enum VIEW_DIRECTION {
		LEFT, RIGHT
	}

	public enum JUMP_STATE {
		GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING
	}

	private TextureRegion regPlayer;
	private TextureRegion jetpack;
	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean slowUpgrade;
	public float slowTimeLeft;

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
		terminalVelocity.set(4.0f, 5.5f);
		friction.set(12.0f, 0.0f);
		acceleration.set(0.0f, -20.0f);
		// View direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		// Jump state
		jumpState = JUMP_STATE.FALLING;
		timeJumping = 0;

		// get the jetpack
		jetpack = Assets.instance.powerUps.jetpackJeb;

		// power up stuff
		slowUpgrade = false;
		slowTimeLeft = 0;
	}

	/**
	 * If the jump key was pressed make the bunny head jump in game. Keeps track of
	 * the states of the jump of the bunny head
	 * 
	 * @param jumpKeyPressed
	 *            whether the key was pressed or not
	 */
	public void setJumping(boolean jumpKeyPressed) {
		switch (jumpState) {
		case GROUNDED: // Character is standing on a platform
			if (jumpKeyPressed) {
				// Start counting jump time from the beginning
				timeJumping = 0;
				jumpState = JUMP_STATE.JUMP_RISING;
			}
			break;
		case JUMP_RISING: // Rising in the air
			if (!jumpKeyPressed)
				jumpState = JUMP_STATE.JUMP_FALLING;
			break;
		case FALLING:// Falling down
		case JUMP_FALLING: // Falling down after jump
			break;
		}
	}

	protected void updateMotionY(float deltaTime) {
		switch (jumpState) {
		case GROUNDED:
			jumpState = JUMP_STATE.FALLING;
			break;
		case JUMP_RISING:
			// Keep track of jump time
			timeJumping += deltaTime;
			// Jump time left?
			if (timeJumping <= JUMP_TIME_MAX) {
				// Still jumping
				velocity.y = terminalVelocity.y;
			}
			break;
		case FALLING:
			break;
		case JUMP_FALLING:
			// Add delta times to track jump time
			timeJumping += deltaTime;
			// Jump to minimal height if jump key was pressed too short
			if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN) {
				// Still jumping
				velocity.y = terminalVelocity.y;
			}
		}
		super.updateMotionY(deltaTime);
	}

	/**
	 * Render in jeb
	 */
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;
		// render the jetpack if jeb has it before jeb
		// reg = jetpack;
		// batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y,
		// dimension.x, dimension.y, scale.x,
		// scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
		// reg.getRegionHeight(),
		// viewDirection == VIEW_DIRECTION.LEFT, false);

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

	/**
	 * Call the superclass's update, but also check for power ups and update their
	 * timers
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (slowTimeLeft > 0) {
			slowTimeLeft -= deltaTime;
			// disable to power up
			if (slowTimeLeft < 0) {
				slowTimeLeft = 0;
				setSlowUpgrade(false);
			}
		}
	}
}
