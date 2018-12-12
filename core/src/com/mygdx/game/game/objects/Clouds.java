package com.mygdx.game.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.game.Assets;

public class Clouds extends AbstractGameObject{

	private final int length = 10;
	private Array<TextureRegion> regClouds;
	private Array<Cloud> clouds;

	private class Cloud extends AbstractGameObject
	{
		private TextureRegion regCloud;
		public Cloud () {}
		public void setRegion (TextureRegion region)
		{
			regCloud = region;
		}
		@Override
		public void render (SpriteBatch batch)
		{
			TextureRegion reg = regCloud;
			batch.draw(reg.getTexture(), position.x + origin.x,
					position.y + origin.y, origin.x, origin.y, dimension.x,
					dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
					reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
					false, false);
		}
	}
	public Clouds () 
	{
		init();
	}

	private void init()
	{
		dimension.set(2.0f, 1f);
		regClouds = new Array<TextureRegion>();
		regClouds.add(Assets.instance.levelDecoration.cloud1);
		regClouds.add(Assets.instance.levelDecoration.cloud2);
		int distFac = 2;
		int numClouds = (int)(length / distFac);
		clouds = new Array<Cloud>(2 * numClouds);
		for (int i = 0; i < numClouds; i++) 
		{
			Cloud cloud = spawnCloud();
			cloud.position.x = i * distFac;
			clouds.add(cloud);
		}
	}

	/**
	 * creates a cloud that makes use of a simple 
	 * physics simulation code
	 * @return
	 */
	private Cloud spawnCloud () {
		Cloud cloud = new Cloud();
		cloud.dimension.set(dimension);
		// select random cloud image
		cloud.setRegion(regClouds.random());
		// position
		Vector2 pos = new Vector2();
		pos.x = length + 10; // position after end of level
		pos.y += position.y + 10; // base position
		pos.y += MathUtils.random(0.0f, 0.2f)
				* (MathUtils.randomBoolean() ? 10 : -1); // random additional position
		cloud.position.set(pos);
		cloud.position.set(pos);
		// speed
		Vector2 speed = new Vector2();
		speed.x += 0.5f; // base speed
		// random additional speed
		speed.x += MathUtils.random(0.0f, 0.75f);
		cloud.terminalVelocity.set(speed);
		speed.x *= -1; // move left
		cloud.velocity.set(speed);
		return cloud;
	}
	@Override
	public void render (SpriteBatch batch) 
	{
		batch.setColor(1, 1, 1, .8f);
		for (Cloud cloud : clouds)
			cloud.render(batch);
		batch.setColor(1, 1, 1, 1);
	}

	/**
	 * updates the location of the clouds
	 * by letting the physics move them
	 */
	@Override
	public void update (float deltaTime) {
		for (int i = clouds.size - 1; i>= 0; i--) 
		{
			Cloud cloud = clouds.get(i);
			cloud.update(deltaTime);
			if (cloud.position.x < - 3)
			{
				System.out.println("Removing cloud");
				// cloud moved outside of world.
				// destroy and spawn new cloud at end of level.
				clouds.removeIndex(i);
				clouds.add(spawnCloud());
			}
		}
	}
}
