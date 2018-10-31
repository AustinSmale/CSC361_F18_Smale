package com.mygdx.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.game.Assets;

public class SpringPlatform extends AbstractGameObject {
	private TextureRegion regSPlatformMid;
	private TextureRegion regSPlatformLeft;
	private TextureRegion regSPlatformRight;
	private int length;

	public SpringPlatform() {
		init();
	}

	// Initialization
	private void init() {
		dimension.set(1.0f, 0.5f);
		regSPlatformMid = Assets.instance.sPlatform.sPlatformMid;
		regSPlatformLeft = Assets.instance.sPlatform.sPlatformLeft;
		regSPlatformRight = Assets.instance.sPlatform.sPlatformRight;
	}

	// Sets the length of the rock
	public void setLength(int length) {
		this.length = length;
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

		// draw left edge
		reg = regSPlatformLeft;
		relX -= dimension.x;
		batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), false, false);

		// draw the middle
		reg = regSPlatformMid;
		relX = 0;
		for (int i = 0; i < length; i++) {
			batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x,
					dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
					reg.getRegionHeight(), false, false);
			relX += dimension.x;
		}

		// draw right edge
		reg = regSPlatformRight;
		batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x + dimension.x / 8, origin.y,
				dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}

}
