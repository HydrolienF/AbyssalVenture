package fr.formiko.abyssalventure;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
	
	private Player player = new Player();

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

		startNewGame();
	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 1, 1);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		hudStage.act(Gdx.graphics.getDeltaTime());
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
			public boolean mouseMoved(int screenX, int screenY) { 
				// Point A
				int playerX = (int) player.getCreature().getCenterX();
				int playerY = (int) player.getCreature().getCenterY();
				// Point B 
				int Bx = screenX;
				int By = screenY;
				// Point C
				int Cx = Bx;
				int Cy = playerY;
				
				// Distances
				double AB = Math.sqrt((playerX - Bx) * (playerX - Bx) + (playerY - By) * (playerY - By));
				double AC = Math.sqrt((playerX - Cx) * (playerX - Cx) + (playerY - Cy) * (playerY - Cy));
				
				// Calcul de l'angle entre la souris et le joueur
				int angle = (int) Math.acos(AC/AB);
				
				player.getCreature().setRotation(angle);
				return false; 
			}
			@Override
			public boolean scrolled(float amountX, float amountY) { return false; }
		};
	}

	private void startNewGame() {
		// Add actors to stage
		stage.addActor(new Fish());
	}
}
