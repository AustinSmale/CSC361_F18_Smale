package com.mygdx.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.game.Assets;

/**
 * Allows Jeb to double jump
 * @author Austin
 *
 */
public class DoubleJumpUpgrade extends AbstractGameObject {
	private TextureRegion upgrade;
	public boolean collected;

	public DoubleJumpUpgrade() {
		init();
	}

	/**
	 * Initialize the power up
	 */
	private void init() {
		dimension.set(1, 1);
		upgrade = Assets.instance.powerUps.doubleJump;
		collected = false;
	}

	/**
	 * Render in the upgrade
	 */
	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg;

		if (!collected) {
			reg = upgrade;
			batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x,
					scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
					false, false);
		}
	}
}
