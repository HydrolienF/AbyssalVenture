package fr.formiko.abyssalventure;

import java.util.HashSet;
import java.util.Set;
import com.badlogic.gdx.Gdx;

public class Fish extends Creature {
    protected int fishLevel;


    public Fish(String textureName, int level) {
        super(textureName);
        this.fishLevel = level;
        speed = 100f;
        defaultZoom = 0.1f;
        if (level != -1) {
            size = level + AbyssalVentureGame.getTimer() / 60f;
        }
        sizeUp(0);
    }
    public Fish(int level) { this("fish" + level, level); }

    // public int getGivenScore() { return Math.max(1, (int) Math.pow(fishLevel, 2)); }
    public int getGivenScore() { return Math.max(1, fishLevel); }


    // Move fish
    public void move() {
        float tempX = this.speed * Gdx.graphics.getDeltaTime() * (float) Math.cos(Math.toRadians(getRotation()));
        setScaleY(tempX > 0 ? Math.abs(getScaleY()) : -Math.abs(getScaleY())); // change draw direction
        translate(tempX, this.speed * Gdx.graphics.getDeltaTime() * (float) Math.sin(Math.toRadians(getRotation())));

        // Add random angle to make move natural
        if (isAI())
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
        Set<Creature> toRemove = new HashSet<>();
        for (Creature c : AbyssalVentureGame.creatureList) {
            if (c != this && hitBoxConnected(c) && canEat(c)) {
                Gdx.app.log("PlayerFish", "PlayerFish hit " + c);
                toRemove.add(c);
            }
        }
        for (Creature c : toRemove) {
            if (!isAI()) {
                AbyssalVentureGame.player.setScore(AbyssalVentureGame.player.getScore() + c.getGivenScore());
            }
            sizeUp(c.getGivenScore() / 10f);
            c.remove();
        }
    }
}
