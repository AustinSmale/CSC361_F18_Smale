package com.mygdx.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.game.Assets;

/**
 * Give Jeb a jet pack to fly around with for 5 seconds
 * 
 * @author Austin
 *
 */
public class JetpackUpgrade extends AbstractGameObject {

	private TextureRegion upgrade;
	public boolean collected;

	public JetpackUpgrade() {
		init();
	}

	/**
	 * Initialize the power up
	 */
	private void init() {
		dimension.set(1, 1);
		upgrade = Assets.instance.powerUps.jetpackPU;
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
