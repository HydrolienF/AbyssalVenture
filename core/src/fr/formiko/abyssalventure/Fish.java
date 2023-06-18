package fr.formiko.abyssalventure;

import com.badlogic.gdx.Gdx;

public class Fish extends Creature {
    protected int fishLevel;
    public Fish(String textureName) {
        super(textureName);
        speed = 1f;
        setZoom(0.3f);
    }
    public Fish(int level) { this("fish" + level); }


    // Move fish
    public void move() {
        float tempX = this.speed * (float) Math.cos(Math.toRadians(getRotation()));
        setScaleY(tempX > 0 ? Math.abs(getScaleY()) : -Math.abs(getScaleY()));
        translate(tempX, this.speed * (float) Math.sin(Math.toRadians(getRotation())));

        // Add random angle to make move natural
        minorRandomRotation(0.02, 10);
    }

    public void minorRandomRotation(double frequency, float maxRotation) {
        double r = java.lang.Math.random() / (Gdx.graphics.getDeltaTime() * 100);
        if (r < frequency) { // randomize rotation
            setRotation(getRotation() + (float) (java.lang.Math.random() * maxRotation) - (maxRotation / 2f));
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        move();
    }
}
