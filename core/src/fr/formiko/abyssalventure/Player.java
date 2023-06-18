package fr.formiko.abyssalventure;

public class Player {
    private Creature creature;
    private int score;

    public Player() {
        creature = new PlayerFish();
        score = 0;
    }

    public Creature getCreature() { return creature; }
    public void setCreature(Creature creature) { this.creature = creature; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}
