package fr.formiko.abyssalventure;

import java.util.HashSet;
import java.util.Set;
import com.badlogic.gdx.Gdx;

public class PlayerFish extends Fish {
    public PlayerFish() {
        super("playerFish");
        setZoom(0.07f);
        hitRadius = 50;
        speed = 200f;
    }
    @Override
    public boolean isAI() { return false; }

    @Override
    public void act(float delta) {
        super.act(delta);
        Set<Creature> toRemove = new HashSet<>();
        for (Creature c : AbyssalVentureGame.creatureList) {
            if (c != this && hitBoxConnected(c)) { // TODO && can be eaten
                Gdx.app.log("PlayerFish", "PlayerFish hit " + c);
                toRemove.add(c);
                // setZoom(getZoom() + 0.01f);
                AbyssalVentureGame.player.setScore(AbyssalVentureGame.player.getScore() + c.getGivenScore());
            }
        }
        for (Creature c : toRemove) {
            c.remove();
        }
    }
}
