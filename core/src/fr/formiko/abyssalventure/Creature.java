package fr.formiko.abyssalventure;

import fr.formiko.abyssalventure.tools.KTexture;
import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

/**
 * {@summary A creature is an actor that can move and interact with other Creature.}<br>
 */
public class Creature extends Actor {
    private static Map<String, TextureRegion> textureRegionMap = new HashMap<>();
    protected String textureName;

    protected float speed;

    public Creature(String textureName) {
        this.textureName = textureName;

        if (!textureRegionMap.containsKey(textureName) && Gdx.files != null) {
            FileHandle file = Gdx.files.internal("images/creatures/" + textureName + ".png");
            if (file.exists()) {
                textureRegionMap.put(textureName, new TextureRegion(new KTexture(file)));
            }
        }

        if (getTextureRegion() != null) {
            setSize(getTextureRegion().getRegionWidth(), getTextureRegion().getRegionHeight());
            setOrigin(Align.center);
        }
    }

    // get set -----------------------------------------------------------------
    public float getSpeed() { return speed; }
    /**
     * {@summary Set center loaction to a random loaction}
     * 
     * @param maxX max value of x
     * @param maxY max value of y
     */
    public void setRandomLoaction(float maxX, float maxY) {
        setCenterX((float) Math.random() * maxX);
        setCenterY((float) Math.random() * maxY);
    }
    public float getCenterX() { return getX() + getWidth() / 2; }
    public float getCenterY() { return getY() + getHeight() / 2; }
    public Vector2 getCenter() { return new Vector2(getCenterX(), getCenterY()); }
    public void setCenterX(float x) { setX(x - getWidth() / 2); }
    public void setCenterY(float y) { setY(y - getHeight() / 2); }
    /**
     * {@summary Set center location.}
     */
    public void setCenter(float x, float y) {
        setCenterX(x);
        setCenterY(y);
    }
    /**
     * {@summary Move in x &#38; y}
     * 
     * @param x x
     * @param y y
     */
    public void translate(float x, float y) {
        setX(getX() + x);
        setY(getY() + y);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(getTextureRegion(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(),
                getRotation());
        // drawLifePoint(batch);
    }


    // private -----------------------------------------------------------------
    private TextureRegion getTextureRegion() { return textureRegionMap.get(textureName); }
}
