package afterplanet;

import static afterplanet.Game.pAngle;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

public class Player extends Entity implements ImageObserver{

    //declare variables
    String name;
    int lives;
    int xSpeed;
    int ySpeed;
    Point center = new Point();
    
    //constructors
    public Player() {
        super();
        lives = 0;
        name = "";
    }
    
    //accepts parameters
    public Player(String name, int xPos, int yPos) {
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        xSpeed = 0;
        ySpeed = 0;
        lives = 5;
    }
    
    //accepts all parameters
    public Player(String n, int health, int xPos, int yPos, int l) {
        super(health, xPos, yPos);
        xSpeed = 0;
        ySpeed = 0;
        lives = l;
        name = n;
    }

    //getter and setter for lives
    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    //getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //getter and setter for xspeed
    public int getXSpeed() {
        return xSpeed;
    }

    public void setXSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    //getter and setter for y speed
    public int getYSpeed() {
        return ySpeed;
    }

    public void setYSpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }
    
    //set center of the player
    public void setCenter(Point p) {
        center = p;
    }
    //get center of player
    public Point getCenter() {
        return this.center;
    }
    
    //draw the player 
    public void draw(Graphics g, Image pImage) {
        Graphics2D g2d = (Graphics2D) g;
        center.setLocation(xPos+27, yPos + 57);
        AffineTransform transform = g2d.getTransform();
        g2d.rotate(pAngle, xPos + 27, yPos + 42);
        g2d.drawImage(pImage, (int)xPos, (int)yPos, this);
        g2d.setTransform(transform);
    }

    //return string method for info
    public String toString() {
        return "Name : " + name + "Lives : " + lives + toString();
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
