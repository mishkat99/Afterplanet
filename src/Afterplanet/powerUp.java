package afterplanet;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class powerUp {
    
    //declares variables
    boolean gActive;
    boolean pActive;
    String type;
    int xPos;
    int yPos;
    int duration;

    //primary constructor
    public powerUp() {
        gActive = false;
        pActive = false;
        type = "";
        xPos = 0;
        yPos = 0;
        duration = 0;
    }
    
    //secondary constructor
    public powerUp(int x, int y) {
        gActive = true;
        pActive = false;
        type = "";
        xPos = x;
        yPos = y;
        duration = 5;
    }
    
    //tertiary constructor, accepts all parameters
    public powerUp(boolean ga, boolean pa, String t, int x, int y, int d) {
        gActive = ga;
        pActive = pa;
        type = t;
        xPos = x;
        yPos = y;
        duration = d;

    }

    //accessor for ground active
    public boolean isGActive() {
        return gActive;
    }

    //mutator for ground active
    public void setGActive(boolean active) {
        this.gActive = active;
    }
    
    //accessor for player active
    public boolean isPActive() {
        return pActive;
    }
    
    //mutator for player active
    public void setPActive(boolean active){
        this.pActive = active;
    }

    //accessor and mutator for type of power up
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    //getter and setter for xpos
    public int getXPos() {
        return xPos;
    }
    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    //getter and setter for ypos
    public int getYPos() {
        return yPos;
    }
    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    //setter and getter for duration of power up
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    //draws power ups
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        if (gActive) {
            //draw on ground
        } else if (pActive) {
            //draw on bottom right
        }
    }
    
    //activate power up
    public void activate() {
        
    }
    //deactiveate power up
    public void deActivate() {
        
    }
    
    //to string method to print out info.
    public String toString() {
        return "Type = " + type + "Active = " + pActive + "x position = " + xPos + "y position = " + yPos + "Duration = " + duration;

    }

}
