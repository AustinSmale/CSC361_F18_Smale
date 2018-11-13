package com.mygdx.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.game.Assets;

/**
 * The platforms that jeb is allowed to land on
 * 
 * @author Austin
 *
 */
public class SpringPlatform extends AbstractGameObject {
	private TextureRegion regSPlatformMid;
	private int length;

	public SpringPlatform() {
		init();
	}

	// Initialization
	private void init() {
		dimension.set(1.0f, 1.0f);
		regSPlatformMid = Assets.instance.sPlatform.sPlatformMid;

	}

	// Sets the length of the rock
	public void setLength(int length) {
		this.length = length;

		// Update bounding box for collision detection
		bounds.set(0, 0, dimension.x * length, dimension.y);

	}

	// Increases the overall length of the rock by a fixed length
	public void increaseLength(int amount) {
		setLength(length + amount);
	}

	/**
	 * Render the platforms
	 */
	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;
		float relX = 0;
		float relY = 0;

		// draw the middle
		reg = regSPlatformMid;
		relX = 0;
		for (int i = 0; i < length; i++) {
			batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x,
					dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
					reg.getRegionHeight(), false, false);
			relX += dimension.x;
		}
	}

}
