package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.mygdx.game.CSC361_F18_Smale;

public class DesktopLauncher {
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;

	public static void main(String[] arg) {
		// if you need to rebuild the atlas settings
		if (rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.debug = drawDebugOutline;
			// process the texture and add it to a pack
			TexturePacker.process(settings, "assets-raw/images", "../core/assets/images", "mygame.atlas");
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "My Game";
		config.width = 300;
		config.height = 500;
		new LwjglApplication(new CSC361_F18_Smale(), config);
	}
}
