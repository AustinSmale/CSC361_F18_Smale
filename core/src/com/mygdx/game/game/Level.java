package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.game.objects.AbstractGameObject;
import com.mygdx.game.game.objects.Hills;
import com.mygdx.game.game.objects.Jeb;
import com.mygdx.game.game.objects.SpringPlatform;

public class Level {
	public static final String TAG = Level.class.getName();

	// Colors
	public enum BLOCK_TYPE {
		EMPTY(0, 0, 0), // black
		SPRING(0, 255, 0), // green
		PLAYER_SPAWNPOINT(255, 255, 255), // white
		WINTER(0, 0, 255); // blue
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
	public Array<SpringPlatform> sPlatforms;
	public Hills hills;
	public Jeb jeb;

	public Level(String filename) {
		init(filename);
	}

	private void init(String filename) {
		// need to initialize rock array here
		sPlatforms = new Array<SpringPlatform>();

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
				else if (BLOCK_TYPE.SPRING.sameColor(currentPixel)) {
					if (lastPixel != currentPixel) {
						obj = new SpringPlatform();
						float heightIncreaseFactor = 1.0f;
						offsetHeight = -2.5f;
						obj.position.set(pixelX, baseHeight * heightIncreaseFactor + offsetHeight);
						sPlatforms.add((SpringPlatform) obj);
					} else {
						sPlatforms.get(sPlatforms.size - 1).increaseLength(1);
					}
				}

				// Winter platform
				else if (BLOCK_TYPE.WINTER.sameColor(currentPixel)) {
					// not added yet
				}
				// Player spawn point
				else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
					obj = new Jeb();
					offsetHeight = -3.0f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					jeb = (Jeb) obj;
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

		// level decoration
		hills = new Hills();

		// Free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "level '" + filename + "' loaded");
	}

	public void render(SpriteBatch batch) {
		// render in the hills
		hills.render(batch);

		// Draw jungle platforms
		for (SpringPlatform platform : sPlatforms)
			platform.render(batch);

		// draw jeb
		jeb.render(batch);
	}


	public void update(float deltaTime) {
		jeb.update(deltaTime);
		for (SpringPlatform sp : sPlatforms)
			sp.update(deltaTime);
	}
}
