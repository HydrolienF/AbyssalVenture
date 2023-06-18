package fr.formiko.abyssalventure;

import fr.formiko.abyssalventure.tools.KTexture;
import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

/**
 * {@summary A creature is an actor that can move and interact with other Creature.}<br>
 */
public class Creature extends Actor {
    private static Map<String, TextureRegion> textureRegionMap = new HashMap<>();
    private String textureName;
    
    protected int x;
    protected int y;
    protected int speed;
    protected int angle;

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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(getTextureRegion(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(),
                getRotation());
        // drawLifePoint(batch);
    }
    
    

    

    // private -----------------------------------------------------------------
    private TextureRegion getTextureRegion() { return textureRegionMap.get(textureName); }
}
