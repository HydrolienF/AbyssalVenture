package fr.formiko.abyssalventure;

public class PlayerFish extends Fish {
    public PlayerFish() {
        super("playerFish");
        setZoom(0.2f);
    }
    @Override
    public boolean isAI() { return false; }
}
