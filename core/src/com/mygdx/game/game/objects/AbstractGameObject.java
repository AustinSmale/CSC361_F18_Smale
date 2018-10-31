package com.mygdx.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

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

	/**
	 * Constructor for AbstractGameObject to set vectors
	 */
	public AbstractGameObject() {
		position = new Vector2();
		dimension = new Vector2(1, 1);
		origin = new Vector2();
		scale = new Vector2(1, 1);
		rotation = 0;
	}

	/**
	 * Everytime an update is called
	 * 
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
	}

	/**
	 * make each subclass implement this method to use the batch to render
	 * 
	 * @param batch
	 */
	public abstract void render(SpriteBatch batch);
}
