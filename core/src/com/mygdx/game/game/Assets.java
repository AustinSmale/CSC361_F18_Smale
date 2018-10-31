package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

import com.mygdx.game.util.Constants;

public class Assets implements Disposable, AssetErrorListener {
	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	private AssetManager assetManager;

	// Singleton Pattern: prevents instantiation from the other classes.
	private Assets() {

	}

	public AssetPlayer player;
	public AssetWoodPlatform wplatform;
	public AssetJunglePlatform jplatform;

	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;

		// Set asset manager error handler
		assetManager.setErrorListener(this);

		// Load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);

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
		wplatform = new AssetWoodPlatform(atlas);
		jplatform = new AssetJunglePlatform(atlas);
	}

	// Disposes of the assetManager
	@Override
	public void dispose() {
		assetManager.dispose();
	}

	// Error for when the asset cannot be loaded.
	public void error(String filename, Class type, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'", (Exception) throwable);
	}

	/*
	 * ===== Game Assets Below =====
	 */

	// Wood Platform Asset
	public class AssetWoodPlatform {
		public final AtlasRegion wplatform;

		public AssetWoodPlatform(TextureAtlas atlas) {
			wplatform = atlas.findRegion("woodplatform");
		}
	}

	// Jungle Platform
	public class AssetJunglePlatform {
		public final AtlasRegion jplatform;

		public AssetJunglePlatform(TextureAtlas atlas) {
			jplatform = atlas.findRegion("jungleplatform");
		}
	}
	
	// Player Asset, will change just a placeholder
	public class AssetPlayer {
		public final AtlasRegion player;
		
		public AssetPlayer(TextureAtlas atlas) {
			player = atlas.findRegion("player");
		}
	}

	// just need for now
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
	}
}
