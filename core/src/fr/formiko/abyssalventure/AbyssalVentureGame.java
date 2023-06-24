package fr.formiko.abyssalventure;

import fr.formiko.abyssalventure.tools.KTexture;
import fr.formiko.abyssalventure.tools.SoundBank;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Align;

public class AbyssalVentureGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Stage stage;
	private Stage hudStage;
	private Camera camera;
	private Viewport viewport;
	private InputMultiplexer inputMultiplexer;

	private static long startTime;
	private static long lastSpawnTime;
	private static long spawnFrequency = 1000;
	public static List<Creature> creatureList = new ArrayList<>();
	public static Player player;
	private Label scoreLabel;
	private Label bestScoreLabel;
	private int bestScore;
	private int maxTime = 100;
	private Label timerLabel;
	private Label help;
	private Skin skin;
	private Label restartLabel;
	public static boolean debugMode = false;
	private static final String DEFAULT_STYLE = "default";
	public static final int FONT_SIZE = 28;
	private static int newFishDelay = 10; // change this to change the delay between each new fish spawn (smaller is harder)

	
	private static boolean needRestart = false;
	private static boolean waitForRestart = false;
	private static Texture background;
	
	
	Table table2; 

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

		restartLabel = new Label("Press space to restart", skin);
		scoreLabel = new Label("", skin);
		bestScoreLabel = new Label("", skin);
		timerLabel = new Label("", skin);
		help = new Label("Display help", skin);
		help.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				debugMode = !debugMode;
				if (debugMode) {
					help.setText("Hide help");
				} else {
					help.setText("Display help");
				}
			}
		});

		Table table = new Table();
		table.top();
		table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		table.add(bestScoreLabel).expandX();
		table.add(scoreLabel).expandX();
		table.add(timerLabel).expandX();
		table.add(help).expandX();
		
		hudStage.addActor(table);
		
		
		table2 = new Table();
		table2.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		table2.add(restartLabel).align(Align.center);
		

		startNewGame();

		background = new KTexture(
				createPixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new Color(0, 0, 0.2f, 1), new Color(0, 0, 0.8f, 1)));
	}

	public static int getTimer() { return (int) ((System.currentTimeMillis() - startTime) / 1000); }
	public static float getRacioWidth() { return Gdx.graphics != null ? Gdx.graphics.getWidth() / 1920f : 1f; }
	public static float getRacioHeight() { return Gdx.graphics != null ? Gdx.graphics.getHeight() / 1080f : 1f; }
	public static float getRacio() { return java.lang.Math.min(getRacioWidth(), getRacioHeight()); }
	public static float getFPSRacio() { return Gdx.graphics != null ? Gdx.graphics.getDeltaTime() * 60f : 1f; }

	public static boolean needRestart() { return needRestart; }
	public static void setNeedRestart(boolean b) { needRestart = b; }

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 1, 1);
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
		// act
		if(!waitForRestart) {
			updateTimeAndScore();
			spawnFishIfTime();
			stage.act(Gdx.graphics.getDeltaTime());
			hudStage.act(Gdx.graphics.getDeltaTime());
		}

		
		// Check if restart is needed
		if (this.needRestart || getTimer() > maxTime) {
			// if (player.getScore() >= bestScore) {
			if(this.waitForRestart == false) {
				if (getTimer() > maxTime) {
					SoundBank.win.play();
				} else {
					SoundBank.lose.play();
				}
				
				this.needRestart = false;
				this.waitForRestart = true;
				
				hudStage.addActor(table2);
				
			}
			//startNewGame();
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && waitForRestart == true) {
			waitForRestart = false;
			table2.remove();
			startNewGame();
		}

		// draw
		hudStage.draw();
		stage.draw();
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
			public boolean mouseMoved(int screenX, int screenY) {
				player.getCreature().goTo(getVectorStageCoordinates(screenX, screenY));
				return false;
			}
			@Override
			public boolean scrolled(float amountX, float amountY) { return false; }
		};
	}

	private void startNewGame() {
		creatureList.clear();
		stage.clear();
		SoundBank.music.play();
		startTime = System.currentTimeMillis();
		creatureList.clear();
		// Add actors to stage
		player = new Player();
		creatureList.add(player.getCreature());
		stage.addActor(player.getCreature());
	}

	private void spawnFish() {
		int level = Math.min((int) (getTimer() / newFishDelay) + 1, 6);
		Fish fish = new Fish((int) (Math.random() * level) + 1);
		int k = 0;
		do {
			fish.setRandomLoaction(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			k++;
		} while (fish.isInRadius(player.getCreature(), (player.getCreature().getHitRadius() + fish.getHitRadius()) + 100) && k < 100);
		if (k >= 100)
			Gdx.app.log("spawnFish", "k >= 100");
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
		timerLabel.setText("Time: " + getTimer() + "s/" + maxTime + "s");
		if (bestScore < player.getScore())
			bestScore = player.getScore();
		bestScoreLabel.setText("Best score: " + bestScore);
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

	public Vector2 getVectorStageCoordinates(float x, float y) { return stage.screenToStageCoordinates(new Vector2(x, y)); }


	/**
	 * {@summary Create a pixmap with a single color.}
	 * 
	 * @param width  width of pixmap
	 * @param height height of pixmap
	 * @param color  color of pixmap
	 * @param color2 2a color of pixmap for gradient (optional)
	 */
	public static Pixmap createPixmap(int width, int height, Color color, @Null Color color2) {
		Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		if (color2 != null) {
			for (int y = 0; y < height; y++) { // draw line with mixed color.
				pixmap.setColor(new Color(color.r * ((float) y / height) + color2.r * (1 - ((float) y / height)),
						color.g * ((float) y / height) + color2.g * (1 - ((float) y / height)),
						color.b * ((float) y / height) + color2.b * (1 - ((float) y / height)),
						color.a * ((float) y / height) + color2.a * (1 - ((float) y / height))));
				pixmap.drawLine(0, y, width, y);
			}
		} else {
			pixmap.setColor(color);
			pixmap.fillRectangle(0, 0, width, height);
		}
		return pixmap;
	}
}
