package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.GamePreferences;

public class MenuScreen extends AbstractGameScreen {

	private static final String TAG = MenuScreen.class.getName();

	private Stage stage;
	private Skin skinJeb;

	private Skin skinLibgdx;

	// Menu Stuff
	private Image imgBackground;
	private Button btnMenuPlay;
	private Button btnMenuOptions;

	// Options Stuff
	private Window winOptions;
	private TextButton btnWinOptSave;
	private TextButton btnWinOptCancel;
	private CheckBox chkSound;
	private Slider sldSound;
	private CheckBox chkMusic;
	private Slider sldMusic;
	private CheckBox chkShowFpsCounter;

	public MenuScreen(Game game) {
		super(game);
	}

	@Override
	public void render(float deltaTime) {
		stage.act(deltaTime);
		stage.draw();
		// Book version that was removed
		// Table.drawDebug(stage);
		// My version to fix it
		stage.setDebugAll(false);

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport();

	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		rebuildStage();
	}

	/**
	 * Draw the stage of the menu
	 */
	private void rebuildStage() {
		skinJeb = new Skin(Gdx.files.internal(Constants.SKIN_ATLAS_MENU),
				new TextureAtlas(Constants.TEXTURE_ATLAS_MENU));
		skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_ATLAS_LIBGDX_UI),
				new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

		// build all the layers
		Table layerBackground = buildBackground();
		Table layerPlayButton = buildPlayButton();
		Table layerSettingsButton = buildSettingsButton();
		Table layerSettingsWindow = buildSettingsWindow();

		// put together the layers
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(layerBackground);
		stack.add(layerPlayButton);
		stack.add(layerSettingsButton);
		stack.add(layerSettingsWindow);
	}

	/**
	 * Settings Window stuff
	 * 
	 * @return
	 */
	private Table buildSettingsWindow() {
		Table layer = new Table();
		winOptions = new Window("Settings", skinLibgdx);

		// Audio Settings: Sound/Music CheckBox and Volume Slider
		winOptions.add(buildOptWinAudioSettings()).row();

		// Debug: Show FPS Counter
		winOptions.add(buildOptWinDebug()).row();

		// Separator and Buttons (Save, Cancel)
		winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);

		// Make options window slightly transparent
		winOptions.setColor(1, 1, 1, 0.8f);

		// Hide options window by default
		winOptions.setVisible(false);

		// Let TableLayout recalculate widget sizes and positions
		winOptions.pack();

		// Move options window to bottom right corner
		winOptions.setPosition(Constants.VIEWPORT_GUI_WIDTH - winOptions.getWidth() - 50, 50);
		return winOptions;
	}

	private Actor buildOptWinButtons() {
		Table tbl = new Table();

		// Separator
		Label lbl = null;
		lbl = new Label("", skinLibgdx);
		lbl.setColor(1.0f, 0.5f, 0.5f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skinLibgdx.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
		tbl.row();
		lbl = new Label("", skinLibgdx);
		lbl.setColor(0.5f, 0.5f, 0.5f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skinLibgdx.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
		tbl.row();

		// Save Button with event handler
		btnWinOptSave = new TextButton("Save", skinLibgdx);
		tbl.add(btnWinOptSave).padRight(30);
		btnWinOptSave.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onSaveClicked();
			}
		});

		// Cancel Button with event handler
		btnWinOptCancel = new TextButton("Cancel", skinLibgdx);
		tbl.add(btnWinOptCancel);
		btnWinOptCancel.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onCancelClicked();
			}
		});
		return tbl;
	}

	private Actor buildOptWinDebug() {
		Table tbl = new Table();

		// Title: "Debug"
		tbl.pad(10, 10, 0, 10);
		tbl.add(new Label("Debug", skinLibgdx, "jebfont", Color.RED)).colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);

		// Checkbox, "Show FPS Counter" label
		chkShowFpsCounter = new CheckBox("", skinLibgdx);
		tbl.add(new Label("Show FPS Counter", skinLibgdx));
		tbl.add(chkShowFpsCounter);
		tbl.row();
		return tbl;
	}

	private Actor buildOptWinAudioSettings() {
		Table tbl = new Table();

		// Title: "Audio"
		tbl.pad(10, 10, 0, 10);
		tbl.add(new Label("Audio", skinLibgdx, "jebfont", Color.RED)).colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);

		// Checkbox, "Sound" label, sound volume slider
		chkSound = new CheckBox("", skinLibgdx);
		tbl.add(chkSound);
		tbl.add(new Label("Sound", skinLibgdx));
		sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
		tbl.add(sldSound);
		tbl.row();

		// Checkbox, "Music" label, music volume slider
		chkMusic = new CheckBox("", skinLibgdx);
		tbl.add(chkMusic);
		tbl.add(new Label("Music", skinLibgdx));
		sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
		tbl.add(sldMusic);
		tbl.row();
		return tbl;
	}

	/**
	 * Buttons
	 * 
	 * @return
	 */
	private Table buildPlayButton() {
		Table layer = new Table();
		layer.right().bottom();

		// Play Button
		btnMenuPlay = new Button(skinJeb, "play");
		layer.add(btnMenuPlay).padRight(575).padBottom(425);
		btnMenuPlay.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onPlayClicked();
			}
		});

		return layer;
	}

	private Table buildSettingsButton() {
		Table layer = new Table();
		layer.right().bottom();

		// Options Button
		btnMenuOptions = new Button(skinJeb, "options");
		layer.add(btnMenuOptions).padRight(475).padBottom(425);

		btnMenuOptions.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onOptionsClicked();
			}
		});

		return layer;
	}

	/**
	 * Button clicks
	 */
	protected void onOptionsClicked() {
		loadSettings();
		btnMenuPlay.setVisible(false);
		btnMenuOptions.setVisible(false);
		winOptions.setVisible(true);
	}

	protected void onPlayClicked() {
		game.setScreen(new GameScreen(game));
	}

	protected void onCancelClicked() {
		btnMenuPlay.setVisible(true);
		btnMenuOptions.setVisible(true);
		winOptions.setVisible(false);
	}

	protected void onSaveClicked() {
		saveSettings();
		onCancelClicked();
	}

	/**
	 * Backgorund image layer
	 * 
	 * @return
	 */
	private Table buildBackground() {
		Table layer = new Table();
		// Background
		imgBackground = new Image(skinJeb, "ui-bg");
		layer.add(imgBackground);
		return layer;
	}

	
	/**
	 * Load the settings saved
	 */
	private void loadSettings() {
		GamePreferences prefs = GamePreferences.instance;
		prefs.load();
		chkSound.setChecked(prefs.sound);
		sldSound.setValue(prefs.volSound);
		chkMusic.setChecked(prefs.music);
		sldMusic.setValue(prefs.volMusic);
		chkShowFpsCounter.setChecked(prefs.showFpsCounter);
	}
	
	/**
	 * Method saves preferences
	 */
	private void saveSettings() {
		GamePreferences prefs = GamePreferences.instance;
		prefs.sound = chkSound.isChecked();
		prefs.volSound = sldSound.getValue();
		prefs.music = chkMusic.isChecked();
		prefs.volMusic = sldMusic.getValue();
		prefs.showFpsCounter = chkShowFpsCounter.isChecked();
		prefs.save();
	}
	
	@Override
	public void hide() {
		stage.dispose();
		skinJeb.dispose();
		skinLibgdx.dispose();
	}

	@Override
	public void pause() {
	}

}
