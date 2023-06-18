package fr.formiko.abyssalventure;

public class PlayerFish extends Fish {
    public PlayerFish() {
        super("playerFish", -1);
        defaultZoom = 0.04f;
        speed = 200f;
        size = 5f / 3f;
        sizeUp(0);
    }
    @Override
    public boolean isAI() { return false; }

    public boolean canEat(Creature it) { return getSize() > it.getSize(); }
}
