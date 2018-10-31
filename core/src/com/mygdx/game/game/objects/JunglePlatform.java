package com.mygdx.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.game.Assets;

public class JunglePlatform extends AbstractGameObject {
	private TextureRegion regJPlatform;

	public JunglePlatform() {
		init();
	}

	// Initialization
	private void init() {
		dimension.set(1.0f, 0.25f);
		regJPlatform = Assets.instance.jplatform.jplatform;
	}

	/**
	 * Render the platforms
	 */
	@Override
	public void render(SpriteBatch batch) {
		batch.draw(regJPlatform.getTexture(), position.x + origin.x, position.y + origin.y, origin.x, origin.y,
				dimension.x, dimension.y, scale.x, scale.y, rotation, regJPlatform.getRegionX(),
				regJPlatform.getRegionY(), regJPlatform.getRegionWidth(), regJPlatform.getRegionHeight(), false, false);
	}

}
