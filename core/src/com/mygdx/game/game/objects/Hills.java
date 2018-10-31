package com.mygdx.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.game.Assets;

public class Hills extends AbstractGameObject {
	private TextureRegion front;
	private TextureRegion back;

	public Hills() {
		init();
	}

	private void init() {
		dimension.set(10, 2);
		front = Assets.instance.levelDecoration.hillFront;
		back = Assets.instance.levelDecoration.hillBack;

		// Shift mountain and extend length
		origin.x = -dimension.x * 2;
	}

	@Override
	public void render(SpriteBatch batch) {
		
	}	
}
