package afterplanet;

import java.awt.Graphics2D;

public class Entity {
    
    //attributes
    int health;
    double xPos;
    double yPos;
    
    //constructor
    public Entity() {

        health = 0;
        xPos = 0;
        yPos = 0;
    }
    
    //constructor
    public Entity(int h, double x, double y) {
        this.health = h;
        xPos = x;
        yPos = y;
    }
    
    //Accessors and Mutators
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public double getXPos() {
        return xPos;
    }

    public void setXPos(double xPos) {
        this.xPos = xPos;
    }

    public double getYPos() {
        return yPos;
    }

    public void setYPos(double yPos) {
        this.yPos = yPos;
    }
    
    //Draw (overridden by subclasses)
    public void draw(Graphics2D g){
        
    }
    
    //To String - returns information about specified entity
    public String toString() {
        return "health: " + health + ", xPos=" + xPos + ", yPos=" + yPos;
    }

}

