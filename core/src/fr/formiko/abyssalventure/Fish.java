package fr.formiko.abyssalventure;

import com.badlogic.gdx.Gdx;

public class Fish extends Creature {
    public Fish(String textureName) {
        super(textureName);
        speed = 1f;
    }
    public Fish() { this("fish"); }


    // Move fish
    public void move() {
        translate(this.speed * (float) Math.cos(Math.toRadians(getRotation())),
                this.speed * (float) Math.sin(Math.toRadians(getRotation())));

        // Add random angle to make move natural
        minorRandomRotation(0.02);
    }

    public void minorRandomRotation(double frequency) {
        double r = java.lang.Math.random() / (Gdx.graphics.getDeltaTime() * 100);
        if (r < frequency) { // randomize rotation
            setRotation((float) (java.lang.Math.random() * 20) - 10f);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        move();
    }
}
