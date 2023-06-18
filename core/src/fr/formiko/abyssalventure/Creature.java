package fr.formiko.abyssalventure;

import fr.formiko.abyssalventure.tools.KTexture;
import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
    /** Hit radius is used as a hitBox for interaction */
    protected int hitRadius;

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
        hitRadius = 100;
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
    public void setRandomRotation() { setRotation((float) (Math.random() * 360)); }
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
    public void setZoom(float zoom) { setScale(zoom, zoom); }
    public float getZoom() { return getScaleX(); }
    public int getHitRadius() { return hitRadius; }
    public void setHitRadius(int hitRadius) { this.hitRadius = hitRadius; }

    public boolean isAI() { return true; }

    /**
     * Return true if hit box of the 2 MapItems are connected.
     */
    public boolean hitBoxConnected(Creature it) { return isInRadius(it, hitRadius + it.hitRadius); }

    /**
     * Return true if other MapItem is in radius.
     */
    public boolean isInRadius(Creature mi2, double radius) { return distanceTo(mi2) < radius; }

    /**
     * {@summary Return the distance between center point of this &#38; center point of an other MapItem.}
     */
    public float distanceTo(Creature mi2) {
        return (float) getDistanceBetweenPoints(getCenterX(), getCenterY(), mi2.getCenterX(), mi2.getCenterY());
    }
    /**
     * {@summary Return the distance between center point of this &#38; stage coordinate.}
     */
    public float distanceTo(Vector2 mi2) { return (float) getDistanceBetweenPoints(getCenterX(), getCenterY(), mi2.x, mi2.y); }

    /**
     * {@summary Return distance between 2 points.}
     * 
     * @param x1 x of 1a point
     * @param y1 y of 1a point
     * @param x2 x of 2a point
     * @param y2 y of 2a point
     * @return
     */
    public static double getDistanceBetweenPoints(float x1, float y1, float x2, float y2) {
        return java.lang.Math.sqrt(java.lang.Math.pow((y2 - y1), 2) + java.lang.Math.pow((x2 - x1), 2));
    }
    public int getGivenScore() { return 0; }

    /**
     * {@summary Set wanted rotation to go to v.}
     * 
     * @param v      contains coordinate of Point to got to
     * @param degdif Degre difference to go to
     */
    public void goTo(Vector2 v) {
        Vector2 v2 = new Vector2(v.x - getCenterX(), v.y - getCenterY());
        float newRotation = v2.angleDeg();
        goTo(newRotation);
    }
    /**
     * {@summary Set wanted rotation to reach newRotation.}
     * 
     * @param newRotation rotation to reach
     */
    public void goTo(float newRotation) { setRotation(newRotation); }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(getTextureRegion(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(),
                getRotation());
        // drawLifePoint(batch);
        drawDebugCircles(batch, parentAlpha);
    }

    private ShapeRenderer shapeRenderer;
    private void drawDebugCircles(Batch batch, float parentAlpha) {
        batch.end();
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        if (shapeRenderer == null) {
            shapeRenderer = new ShapeRenderer();
        }
        // if (GameScreen.getCamera() != null) {
        // shapeRenderer.setProjectionMatrix(GameScreen.getCamera().combined);
        shapeRenderer.begin(ShapeType.Line);
        // shapeRenderer.setColor(new Color(0f, 0f, 1f, parentAlpha * 1f));
        // shapeRenderer.circle(getCenterX(), getCenterY(), c.getVisionRadius());
        shapeRenderer.setColor(new Color(1f, 0f, 0f, parentAlpha * 1f));
        shapeRenderer.circle(getCenterX(), getCenterY(), getHitRadius());
        shapeRenderer.end();
        // }
        Gdx.gl.glDisable(GL30.GL_BLEND);
        batch.begin();
    }

    @Override
    public boolean remove() {
        AbyssalVentureGame.creatureList.remove(this);
        return super.remove();
    }


    // private -----------------------------------------------------------------
    private TextureRegion getTextureRegion() { return textureRegionMap.get(textureName); }
}
