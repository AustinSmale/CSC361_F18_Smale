package com.mygdx.game.screens;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.game.WorldController;
import com.mygdx.game.util.Constants;

public class HighScoreScreen extends AbstractGameScreen {

	private static final String TAG = MenuScreen.class.getName();
	private Stage stage;
	private Skin skinLibgdx;

	// Score Stuff
	private Button btnEnter;
	private TextField tfName;

	// list of top 10 score and user names
	List<Integer> hs;
	List<String> un;

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
		skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_ATLAS_LIBGDX_UI),
				new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));
		Table layerList = buildList();
		Table layerInput = buildInput();

		// put together the layers
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(layerList);
		stack.add(layerInput);

	}

	private Table buildInput() {
		Table layer = new Table();
		layer.padBottom(10);
		Label nameLabel = new Label("Name: ", skinLibgdx);
		tfName = new TextField("", skinLibgdx);

		layer.add(nameLabel).padTop(Constants.VIEWPORT_GUI_HEIGHT - 125);
		layer.add(tfName).width(300).padTop(Constants.VIEWPORT_GUI_HEIGHT - 125);
		layer.row();
		btnEnter = new TextButton("Save", skinLibgdx);
		btnEnter.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onSaveClicked();
			}
		});
		layer.add(btnEnter).width(410).colspan(2).padTop(5);

		return layer;
	}

	protected void onSaveClicked() {
		String name = tfName.getMessageText();
		if (name == null) {
			name = "Unknown";
		}
		Integer score = WorldController.score;
		for (int i = 0; i < hs.size(); i++) {
			if (hs.get(i) < score) {
				hs.add(i, score);
				un.add(i, name);
			}
		}

		saveFile();
		
		game.setScreen(new MenuScreen(game));
	}

	private void saveFile() {
		PrintWriter pw;
		try {
			pw = new PrintWriter(new File("highscore.txt"));
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < 10; i++) {
				sb.append(un.get(i));
				sb.append(",");
				sb.append(hs.get(i));
				sb.append("\n");
			}
			
			pw.write(sb.toString());
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println("saved!");
	}

	private Table buildList() {
		Table layer = new Table();
		Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
		bgPixmap.setColor(.4f, .4f, .4f, 0f);
		bgPixmap.fill();
		TextureRegionDrawable textureRegionDrawableBg = new TextureRegionDrawable(
				new TextureRegion(new Texture(bgPixmap)));
		layer.setBackground(textureRegionDrawableBg);

		// get the users
		for (Label a : getUserList())
			layer.add(a).row();

		return layer;
	}

	private List<Label> getUserList() {
		List<Label> list = new ArrayList<Label>();
		hs = new ArrayList<Integer>(10);
		un = new ArrayList<String>(10);

		String highScoreFile = "highscore.txt";
		String split = ",";
		String line = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(highScoreFile));
			while ((line = br.readLine()) != null) {
				String[] score = line.split(split);
				System.out.printf("User Name: %s\tScore: %s\n", score[0], score[1]);
				hs.add(Integer.parseInt(score[1]));
				un.add(score[0]);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String text = "";
		for (int i = 0; i < hs.size(); i++) {
			System.out.println(un.get(i) + hs.get(i));
			text = un.get(i) + ": " + hs.get(i);
			list.add(new Label(text, skinLibgdx));
		}
		return list;
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

}
