package com.mygdx.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * The super class for every game object
 * 
 * @author Austin Smale
 *
 */
public abstract class AbstractGameObject {
	// The vectors to deal with object
	public Vector2 position;
	public Vector2 dimension;
	public Vector2 origin;
	public Vector2 scale;
	public float rotation;

	// milestone three
	public Vector2 velocity; // current speed
	public Vector2 terminalVelocity; // max speed
	public Vector2 friction; // opposing force that slows down object
	public Vector2 acceleration; // object's constant acceleration
	public Rectangle bounds;
	public Body body;

	/**
	 * Constructor for AbstractGameObject to set vectors
	 */
	public AbstractGameObject() {
		position = new Vector2();
		dimension = new Vector2(1, 1);
		origin = new Vector2();
		scale = new Vector2(1, 1);
		rotation = 0;
		velocity = new Vector2();
		terminalVelocity = new Vector2(1, 1);
		friction = new Vector2();
		acceleration = new Vector2();
		bounds = new Rectangle();
	}

	/**
	 * Everytime an update is called
	 * 
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		// set the body
		if (body == null) {
			updateMotionX(deltaTime);
			updateMotionY(deltaTime);
			// Move to new position
			position.x += velocity.x * deltaTime;
			position.y += velocity.y * deltaTime;
		} else {
			position.set(body.getPosition());
			rotation = body.getAngle() * MathUtils.radiansToDegrees;
		}
	}

	/**
	 * Update the velocity in the x direction based on the friction, acceleration
	 * and then clamp it inside of its terminal velocity
	 * 
	 * @param deltaTime
	 */
	protected void updateMotionX(float deltaTime) {
		if (velocity.x != 0) {
			// Apply friction
			if (velocity.x > 0) {
				velocity.x = Math.max(velocity.x - friction.x * deltaTime, 0);
			} else {
				velocity.x = Math.min(velocity.x + friction.x * deltaTime, 0);
			}
		}
		// Apply acceleration
		velocity.x += acceleration.x * deltaTime;
		// Make sure the object's velocity does not exceed the
		// positive or negative terminal velocity
		velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x, terminalVelocity.x);
	}

	/**
	 * Update the velocity in the y direction based on the friction, acceleration
	 * and then clamp it inside of its terminal velocity
	 * 
	 * @param deltaTime
	 */
	protected void updateMotionY(float deltaTime) {
		if (velocity.y != 0) {
			// Apply friction
			if (velocity.y > 0) {
				velocity.y = Math.max(velocity.y - friction.y * deltaTime, 0);
			} else {
				velocity.y = Math.min(velocity.y + friction.y * deltaTime, 0);
			}
		}
		// Apply acceleration
		velocity.y += acceleration.y * deltaTime;
		// Make sure the object's velocity does not exceed the
		// positive or negative terminal velocity
		velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y);
	}

	/**
	 * make each subclass implement this method to use the batch to render
	 * 
	 * @param batch
	 */
	public abstract void render(SpriteBatch batch);
}
