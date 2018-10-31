package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.game.objects.AbstractGameObject;
import com.mygdx.game.game.objects.JunglePlatform;
import com.mygdx.game.game.objects.WoodPlatform;

public class Level {
	public static final String TAG = Level.class.getName();

	// Colors
	public enum BLOCK_TYPE {
		EMPTY(0, 0, 0), // black
		JUNGLE(0, 255, 0), // green
		PLAYER_SPAWNPOINT(255, 255, 255), // white
		WOOD(0, 0, 255); // blue
		private int color;

		private BLOCK_TYPE(int r, int g, int b) {
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}

		public boolean sameColor(int color) {
			return this.color == color;
		}

		public int getColor() {
			return color;
		}
	}

	// Objects
	public Array<JunglePlatform> jPlatforms;
	public Array<WoodPlatform> wPlatforms;

	public Level(String filename) {
		init(filename);
	}

	private void init(String filename) {
		// need to initialize rock array here
		jPlatforms = new Array<JunglePlatform>();
		wPlatforms = new Array<WoodPlatform>();

		// Load image file that represents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));

		// Scan pixels from top-left to bottom-right
		int lastPixel = -1;
		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
				AbstractGameObject obj = null;
				float offsetHeight = 0;

				// Height grows from bottom to top
				float baseHeight = pixmap.getHeight() - pixelY;

				// Get color of current pixel as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);

				/*
				 * Find matching color value to identify block type at (x,y) point and create
				 * the corresponding game object if there is a match
				 */

				// Empty space
				if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
					// Do nothing
				}

				// Jungle Platform
				else if (BLOCK_TYPE.JUNGLE.sameColor(currentPixel)) {
					obj = new JunglePlatform();
					float heightIncreaseFactor = 0.25f;
					offsetHeight = -2.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
					jPlatforms.add((JunglePlatform) obj);
				}

				// Wood platform
				else if (BLOCK_TYPE.WOOD.sameColor(currentPixel)) {
					obj = new WoodPlatform();
					float heightIncreaseFactor = 0.25f;
					offsetHeight = -2.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
					wPlatforms.add((WoodPlatform) obj);
				}
				// Player spawn point
				else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {

				}

				// Unknown object/pixel color
				else {
					int r = 0xff & (currentPixel >>> 24); // Red color channel
					int g = 0xff & (currentPixel >>> 16); // Green color channel
					int b = 0xff & (currentPixel >>> 8); // Blue color channel
					int a = 0xff & currentPixel; // Alpha channel
					Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<" + pixelY + ">: r<" + r + "> g<" + g
							+ "> b<" + b + "> a<" + a + ">");
				}
				lastPixel = currentPixel;
			}
		}

		// Free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "level '" + filename + "' loaded");
	}

	public void render(SpriteBatch batch) {
		// Draw jungle platforms
		for (JunglePlatform platform : jPlatforms)
			platform.render(batch);

		// Draw wood platforms
		for (WoodPlatform platform : wPlatforms)
			platform.render(batch);
	}
}
