package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.util.Constants;

public class Assets implements Disposable, AssetErrorListener {
	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	private AssetManager assetManager;
	public AssetFonts fonts;

	// Singleton Pattern: prevents instantiation from the other classes.
	private Assets() {
	}

	public AssetPlayer player;
	public AssetSpringPlatform sPlatform;
	public AssetLevelDecoration levelDecoration;
	public AssetPowerUps powerUps;
	public AssetSound sounds;
	public AssetMusic music;

	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;

		// Set asset manager error handler
		assetManager.setErrorListener(this);

		// Load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);

		// Load sounds into engine
		assetManager.load("sounds/jump.wav", Sound.class);
		assetManager.load("sounds/gameover.mp3", Sound.class);
		assetManager.load("sounds/jetpack.wav", Sound.class);

		// Load music into engine
		assetManager.load("music/wallpaper.mp3", Music.class);

		// Start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);

		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

		// Enable texture filtering for pixel smoothing
		for (Texture t : atlas.getTextures())
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// Create game resource objects
		player = new AssetPlayer(atlas);
		sPlatform = new AssetSpringPlatform(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
		powerUps = new AssetPowerUps(atlas);
		fonts = new AssetFonts();
		sounds = new AssetSound(assetManager);
		music = new AssetMusic(assetManager);
	}

	// Disposes of the assetManager
	@Override
	public void dispose() {
		assetManager.dispose();
		fonts.defaultBig.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultSmall.dispose();
		fonts.fps.dispose();
	}

	// Error for when the asset cannot be loaded.
	public void error(String filename, Class type, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'", (Exception) throwable);
	}

	/*
	 * ===== Game Assets Below =====
	 */

	// Spring Platform Asset
	public class AssetSpringPlatform {
		public final AtlasRegion sPlatformMid;
		public final AtlasRegion sPlatformLeft;
		public final AtlasRegion sPlatformRight;

		public AssetSpringPlatform(TextureAtlas atlas) {
			sPlatformMid = atlas.findRegion("GrassMidSpring");
			sPlatformLeft = atlas.findRegion("GrassLeftSpring");
			sPlatformRight = atlas.findRegion("GrassRightSpring");
		}
	}

	// Player Asset, will change just a placeholder
	public class AssetPlayer {
		public final AtlasRegion idle;
		public final Animation running;
		public final AtlasRegion up;
		public final AtlasRegion down;

		public AssetPlayer(TextureAtlas atlas) {
			idle = atlas.findRegion("player");

			// animation
			Array<AtlasRegion> anm = new Array<AtlasRegion>();
			anm.add(atlas.findRegion("run_anm", 1));
			anm.add(atlas.findRegion("run_anm", 2));
			anm.add(atlas.findRegion("run_anm", 3));
			anm.add(atlas.findRegion("run_anm", 4));
			anm.add(atlas.findRegion("run_anm", 5));
			anm.add(atlas.findRegion("run_anm", 6));

			running = new Animation(5.0f, anm, Animation.PlayMode.LOOP);
			up = atlas.findRegion("jump-up");
			down = atlas.findRegion("jump-down");
		}
	}

	/**
	 * All the power ups in the game 1: Slow Down Time 2: Jetpack 3: Double Jump
	 * 
	 * @author Austin
	 *
	 */
	public class AssetPowerUps {
		public final AtlasRegion slow;
		public final AtlasRegion jetpackPU; // the power up icon
		public final AtlasRegion jetpackJeb; // the jet pack that goes on jeb
		public final AtlasRegion doubleJump;

		public AssetPowerUps(TextureAtlas atlas) {
			slow = atlas.findRegion("slow");
			jetpackPU = atlas.findRegion("jetpackPower");
			jetpackJeb = atlas.findRegion("jetpack");
			doubleJump = atlas.findRegion("double");
		}
	}

	// Level Decoration Assets
	public class AssetLevelDecoration {
		public final AtlasRegion hillFront;
		public final AtlasRegion hillBack;
		public final AtlasRegion cloud1;
		public final AtlasRegion cloud2;

		public AssetLevelDecoration(TextureAtlas atlas) {
			hillFront = atlas.findRegion("Hills1");
			hillBack = atlas.findRegion("Hills2");
			cloud1 = atlas.findRegion("Cloud1");
			cloud2 = atlas.findRegion("Cloud2");
		}
	}

	// just need for now
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
	}

	public class AssetFonts {
		// sizes
		public final BitmapFont fps;
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;

		public AssetFonts() {
			// create three fonts using Libgdx's 15px bitmap font
			fps = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			defaultSmall = new BitmapFont(Gdx.files.internal("images/jebfont.fnt"), true);
			defaultNormal = new BitmapFont(Gdx.files.internal("images/jebfont.fnt"), true);
			defaultBig = new BitmapFont(Gdx.files.internal("images/jebfont.fnt"), true);

			// set font sizes
			fps.getData().setScale(1);
			defaultSmall.getData().setScale(0.75f);
			defaultNormal.getData().setScale(1.0f);
			defaultBig.getData().setScale(2.0f);

			// enable linear texture filtering for smooth fonts
			fps.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		}
	}

	public class AssetMusic {
		public final Music song;

		public AssetMusic(AssetManager am) {
			song = am.get("music/wallpaper.mp3", Music.class);
		}
	}

	public class AssetSound {
		public final Sound gameover;
		public final Sound jetpack;
		public final Sound jump;

		public AssetSound(AssetManager am) {
			gameover = am.get("sounds/gameover.mp3", Sound.class);
			jetpack = am.get("sounds/jetpack.wav", Sound.class);
			jump = am.get("sounds/jump.wav", Sound.class);
		}
	}
}
