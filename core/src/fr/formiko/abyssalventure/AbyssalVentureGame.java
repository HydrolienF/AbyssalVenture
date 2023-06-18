package fr.formiko.abyssalventure;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class AbyssalVentureGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Stage stage;
	private Stage hudStage;
	private Camera camera;
	private Viewport viewport;
	private InputMultiplexer inputMultiplexer;
	private long startTime;
	private long lastSpawnTime;
	private long spawnFrequency = 1000;
	public static List<Creature> creatureList = new ArrayList<>();
	public static Player player;
	private Label scoreLabel;
	private Label timerLabel;
	private Skin skin;
	private static final String DEFAULT_STYLE = "default";
	public static final int FONT_SIZE = 28;

	@Override
	public void create() {
		camera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);
		batch = new SpriteBatch();
		// assets = new Assets();
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
		stage = new Stage(viewport, batch);
		hudStage = new Stage(viewport, batch);
		// stage.setDebugAll(true); // @a
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(hudStage);
		inputMultiplexer.addProcessor(getInputProcessor());

		skin = getDefautSkin();

		scoreLabel = new Label("", skin);
		timerLabel = new Label("", skin);

		Table table = new Table();
		table.top();
		table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		table.add(scoreLabel).expandX();
		table.add(timerLabel).expandX();

		stage.addActor(table);

		startNewGame();
	}

	public int getTimer() { return (int) ((System.currentTimeMillis() - startTime) / 1000); }
	public static float getRacioWidth() { return Gdx.graphics != null ? Gdx.graphics.getWidth() / 1920f : 1f; }
	public static float getRacioHeight() { return Gdx.graphics != null ? Gdx.graphics.getHeight() / 1080f : 1f; }
	public static float getRacio() { return java.lang.Math.min(getRacioWidth(), getRacioHeight()); }
	public static float getFPSRacio() { return Gdx.graphics != null ? Gdx.graphics.getDeltaTime() * 60f : 1f; }

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 1, 1);
		// act
		updateTimeAndScore();
		spawnFishIfTime();
		stage.act(Gdx.graphics.getDeltaTime());
		hudStage.act(Gdx.graphics.getDeltaTime());

		// draw
		stage.draw();
		hudStage.draw();
	}

	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
	}


	/**
	 * {@summary Handle user input.}<br>
	 */
	private InputProcessor getInputProcessor() {
		return new InputProcessor() {
			@Override
			public boolean keyDown(int keycode) { return false; }
			@Override
			public boolean keyUp(int keycode) {
				// if (keycode == Input.Keys.M) {
				// endGame(true);
				// }
				return false;
			}
			@Override
			public boolean keyTyped(char character) { return false; }
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
			@Override
			public boolean mouseMoved(int screenX, int screenY) { return false; }
			@Override
			public boolean scrolled(float amountX, float amountY) { return false; }
		};
	}

	private void startNewGame() {
		startTime = System.currentTimeMillis();
		creatureList.clear();
		// Add actors to stage
		player = new Player();
		creatureList.add(player.getCreature());
		stage.addActor(player.getCreature());
	}

	private void spawnFish() {
		// TODO spawn fish randomly with higher probability for low level fish when time is low
		Fish fish = new Fish(1);
		fish.setRandomLoaction(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		fish.setRandomRotation();
		stage.addActor(fish);
		creatureList.add(fish);
	}
	private void spawnFishIfTime() {
		if (System.currentTimeMillis() - lastSpawnTime > spawnFrequency) {
			spawnFish();
			lastSpawnTime = System.currentTimeMillis();
		}
	}
	private void updateTimeAndScore() {
		scoreLabel.setText("Score: " + player.getScore());
		timerLabel.setText("Time: " + getTimer() + "s");
	}


	/**
	 * @param fontSize size of the font
	 * @return A simple skin that menus use
	 */
	public static Skin getDefautSkin() {
		Skin skin = new Skin();

		// Generate a 1x1 white texture and store it in the skin named "white".
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));
		pixmap.dispose();

		BitmapFont bmf = new BitmapFont(Gdx.files.internal("fonts/dominican.fnt"));

		bmf.getData().markupEnabled = true; // Use to set color label by label

		// Store the default libGDX font under the name DEFAULT_STYLE.
		skin.add(DEFAULT_STYLE, bmf);

		// Configure a TextButtonStyle and name it DEFAULT_STYLE. Skin resources are stored by type, so this doesn't overwrite the font.
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = skin.getFont(DEFAULT_STYLE);
		skin.add(DEFAULT_STYLE, textButtonStyle);

		ButtonStyle buttonStyle = new ButtonStyle();
		skin.add(DEFAULT_STYLE, buttonStyle);

		LabelStyle labelStyle = new LabelStyle(skin.getFont(DEFAULT_STYLE), null);
		labelStyle.background = getWhiteBackground();

		skin.add(DEFAULT_STYLE, labelStyle);
		// skin.add(DEFAULT_STYLE, new LabelStyle(skin.getFont(DEFAULT_STYLE), null)); //Use to set color label by label

		// skin.add("default-horizontal", getSliderStyle(fontSize));
		// skin.add(DEFAULT_STYLE, getScrollPaneStyle());
		// skin.add(DEFAULT_STYLE, getListStyle(skin));
		// skin.add(DEFAULT_STYLE, getSelectBoxStyle(skin));
		// skin.add(DEFAULT_STYLE, getTextFieldStyle(skin));

		return skin;
	}


	/**
	 * {@summary Return a white background with transparency.}
	 * 
	 * @param alpha alpha of the background
	 * @return a white background
	 */
	public static Drawable getWhiteBackground(float alpha) {
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(new Color(1f, 1f, 1f, alpha));
		pixmap.fill();
		Drawable drawable = new Image(new Texture(pixmap)).getDrawable();
		pixmap.dispose();
		return drawable;
	}
	/**
	 * {@summary Return a white background with transparency.}
	 * 
	 * @return a white background
	 */
	public static Drawable getWhiteBackground() { return getWhiteBackground(0.3f); }
}
