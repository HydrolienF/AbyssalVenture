package fr.formiko.abyssalventure;

public class Fish extends Creature {
    public Fish(String textureName) { super(textureName); }
    public Fish() { this("fish"); }
    
    // Move fish
    public void move() {
    	this.x += this.speed * Math.cos(Math.toRadians(this.angle));
    	this.y += this.speed * Math.sin(Math.toRadians(this.angle));
    	
    	// Add random angle to make move natural
    	this.angle += (int)(Math.random()*10);
    }
}
