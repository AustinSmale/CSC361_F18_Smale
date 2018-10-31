package com.mygdx.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.game.Assets;

public class WoodPlatform extends AbstractGameObject {
	private TextureRegion regWPlatform;

	public WoodPlatform() {
		init();
	}

	// Initialization
	private void init() {
		dimension.set(1.0f, 0.25f);
		regWPlatform = Assets.instance.wplatform.wplatform;
	}

	/**
	 * Render the platforms
	 */
	@Override
	public void render(SpriteBatch batch) {
		batch.draw(regWPlatform.getTexture(), position.x + origin.x, position.y + origin.y, origin.x, origin.y,
				dimension.x, dimension.y, scale.x, scale.y, rotation, regWPlatform.getRegionX(),
				regWPlatform.getRegionY(), regWPlatform.getRegionWidth(), regWPlatform.getRegionHeight(), false, false);
	}

}
