package com.mygdx.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.game.Assets;

public class SlowDownUpgrade extends AbstractGameObject {

	private TextureRegion upgrade;
	public boolean collected;

	public SlowDownUpgrade() {
		init();
	}

	private void init() {
		dimension.set(1, 1);
		upgrade = Assets.instance.powerUps.slow;
		collected = false;
	}

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
