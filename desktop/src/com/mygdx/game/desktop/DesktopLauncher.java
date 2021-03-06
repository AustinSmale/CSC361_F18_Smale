package com.mygdx.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.mygdx.game.JumpinJeb;

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
			TexturePacker.process(settings, "assets-raw/images", "../core/assets/images", "jumpin-jeb.atlas");
			TexturePacker.process(settings, "assets-raw/menu", "../core/assets/images", "jumpin-jeb-menu.atlas");
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Jumpin Jeb";
		config.addIcon("assets-raw/icon/player-icon.png", Files.FileType.Internal);
		config.resizable = false;
		config.width = 700;
		config.height = 700;
		new LwjglApplication(new JumpinJeb(), config);
	}
}
