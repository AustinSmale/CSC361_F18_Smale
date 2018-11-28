package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.game.objects.AbstractGameObject;
import com.mygdx.game.game.objects.DoubleJumpUpgrade;
import com.mygdx.game.game.objects.Hills;
import com.mygdx.game.game.objects.Jeb;
import com.mygdx.game.game.objects.JetpackUpgrade;
import com.mygdx.game.game.objects.SlowDownUpgrade;
import com.mygdx.game.game.objects.SpringPlatform;

public class Level {
	public static final String TAG = Level.class.getName();

	// Colors
	public enum BLOCK_TYPE {
		EMPTY(0, 0, 0), // black
		SPRING(0, 255, 0), // green
		PLAYER_SPAWNPOINT(255, 255, 255), // white
		WINTER(0, 0, 255), // blue
		JETPACK(255, 0, 255), // purple
		DOUBLE(255, 255, 0), // yellow
		SLOW(255, 0, 0); // red
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
	public static Array<SlowDownUpgrade> slow;
	public static Array<JetpackUpgrade> jetpack;
	public static Array<DoubleJumpUpgrade> doubles;

	public Level(String filename) {
		init(filename);
	}

	private void init(String filename) {
		// need to initialize rock array here
		sPlatforms = new Array<SpringPlatform>();
		slow = new Array<SlowDownUpgrade>();
		jetpack = new Array<JetpackUpgrade>();
		doubles = new Array<DoubleJumpUpgrade>();

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
						sPlatforms.get(sPlatforms.size - 1).increaseLength(1);
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
					offsetHeight = -2.4f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					jeb = (Jeb) obj;
				}

				// Slow down time upgrade
				else if (BLOCK_TYPE.SLOW.sameColor(currentPixel)) {
					obj = new SlowDownUpgrade();
					offsetHeight = -2.4f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					slow.add((SlowDownUpgrade) obj);
				}

				// jetpack upgrade
				else if (BLOCK_TYPE.JETPACK.sameColor(currentPixel)) {
					obj = new JetpackUpgrade();
					offsetHeight = -2.4f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					jetpack.add((JetpackUpgrade) obj);
				}

				// double jump upgrade
				else if (BLOCK_TYPE.DOUBLE.sameColor(currentPixel)) {
					obj = new DoubleJumpUpgrade();
					offsetHeight = -2.4f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					doubles.add((DoubleJumpUpgrade) obj);
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

		// draw slow down time upgrades
		for (SlowDownUpgrade slowDown : slow) {
			slowDown.render(batch);
		}

		// draw jet pack upgrades
		for (JetpackUpgrade jet : jetpack) {
			jet.render(batch);
		}

		// draw jet pack upgrades
		for (DoubleJumpUpgrade jump : doubles) {
			jump.render(batch);
		}

		// draw jeb
		jeb.render(batch);

	}

	public void update(float deltaTime) {
		jeb.update(deltaTime);
		for (SpringPlatform sp : sPlatforms)
			sp.update(deltaTime);

		for (SlowDownUpgrade slowDown : slow) {
			slowDown.update(deltaTime);
		}

		for (JetpackUpgrade jet : jetpack) {
			jet.update(deltaTime);
		}

		for (DoubleJumpUpgrade jump : doubles) {
			jump.update(deltaTime);
		}
	}
}
