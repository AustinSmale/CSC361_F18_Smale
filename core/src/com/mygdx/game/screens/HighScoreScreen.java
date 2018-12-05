package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.mygdx.game.util.Constants;

public class HighScoreScreen extends AbstractGameScreen {

	private static final String TAG = MenuScreen.class.getName();
	private Stage stage;
	private Skin skinJeb;

	// Menu Stuff
	private Image imgBackground;
	private Button btnEnter;
	private TextField tfName;

	public HighScoreScreen(Game game) {
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

	private void rebuildStage() {
		skinJeb = new Skin(Gdx.files.internal(Constants.SKIN_ATLAS_MENU),
				new TextureAtlas(Constants.TEXTURE_ATLAS_MENU));
		Table layerBackground = buildBackground();
		Table layerList = buildList();
		Table layerInput = buildInput();

		// put together the layers
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(layerBackground);
		stack.add(layerList);
		stack.add(layerInput);

	}

	private Table buildBackground() {
		Table layer = new Table();
		// Background
		imgBackground = new Image(skinJeb, "ui-bg");
		layer.add(imgBackground);
		return layer;
	}

	private Table buildInput() {

		return null;
	}

	private Table buildList() {
		return null;
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

}
