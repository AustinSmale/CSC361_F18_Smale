package com.mygdx.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.game.Assets;

/**
 * Draw the hills in the background of the game
 * 
 * @author Austin
 *
 */
public class Hills extends AbstractGameObject {
	private TextureRegion front;
	private TextureRegion back;

	public Hills() {
		init();
	}

	/**
	 * Initialize the hills
	 */
	private void init() {
		dimension.set(40, 20);
		front = Assets.instance.levelDecoration.hillFront;
		back = Assets.instance.levelDecoration.hillBack;

		// Shift mountain and extend length
		origin.x = -dimension.x * 1.5f;
		origin.y = -dimension.y * 1.075f;
	}

	/**
	 * Render them in the correct positions
	 */
	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;
		float relX = dimension.x;
		float relY = dimension.y;

		reg = back;
		batch.draw(reg.getTexture(), origin.x + relX, position.y + origin.y + relY, origin.x, origin.y, dimension.x,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), false, false);

		reg = front;
		batch.draw(reg.getTexture(), origin.x + relX, position.y + origin.y + relY, origin.x, origin.y, dimension.x,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), false, false);
	}
}
