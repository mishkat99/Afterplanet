package afterplanet;

import static afterplanet.Game.OFFCENTER;
import static afterplanet.Game.angle;
import static afterplanet.Game.player;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.ImageObserver;

public class projectile implements ImageObserver{
    
    //Declare variables
    
    //radius of bullet 
    public static int radius = 8;
    
    //declare x pos and y pos
    double xPos;
    double yPos;
    //declare x and y speed for movement
    double xSpeed;
    double ySpeed;
    //declare the damage of the projectile
    int damage;
    //declare boolean if active and if it hits
    boolean active;
    boolean hit;

    //primary constructor
    public projectile() {
        xPos = 640;
        yPos = 360;
        damage = 0;
        active = false;
        hit = false;
        xSpeed = 0;
        ySpeed = 0;
    }

    //secondary constructer, accepts parameter
    public projectile(double x, double y, double xSpeed, double ySpeed) {
        this.xPos = x;
        yPos = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        active = false;
    }

    //tertiary constructor, accepts all parameters and values
    public projectile(double x, double y, int d, boolean a, boolean h, double xSpeed, double ySpeed) {
        this.xPos = x;
        yPos = y;
        damage = d;
        active = a;
        hit = h;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }
    
    //accessors for xpos
    public double getXPos() {
        return xPos;
    }

    //mutators for xpos
    public void setXPos(double xPos) {
        this.xPos = xPos;
    }

    //accessor for ypos
    public double getYPos() {
        return yPos;
    }

    //mutator for ypos
    public void setYPos(double yPos) {
        this.yPos = yPos;
    }

    //accessors for damage
    public int getDamage() {
        return damage;
    }

    //mutator for damage
    public void setDamage(int damage) {
        this.damage = damage;
    }

    //accessors for activity
    public boolean isActive() {
        return active;
    }

    //mutator for activity
    public void setActive(boolean active) {
        this.active = active;
    }

    //accessors for hits
    public boolean isHit() {
        return hit;
    }

    //mutator for hits
    public void setHit(boolean hit) {
        this.hit = hit;
    }

    //accessors for xspeed
    public double getXSpeed() {
        return xSpeed;
    }

    //mutator for xspeed
    public void setXSpeed(double xSpeed) {
        this.xSpeed = xSpeed;
    }

    //accessors for yspeed
    public double getYSpeed() {
        return ySpeed;
    }

    //mutator for yspeed
    public void setYSpeed(double ySpeed) {
        this.ySpeed = ySpeed;
    }

    //draws the bullet
    public void draw(Graphics g, Image bulletImage) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.drawImage(bulletImage, (int)Math.round(xPos), (int)Math.round(yPos), this);
    }

    //move method
    public void move() {
        xPos = xPos + xSpeed;
        yPos = yPos + ySpeed;
    }

    //reset method
    public void reset() {
        this.active = false;
        xPos = (player.getXPos() + 27) + (42 * Math.cos(-angle - OFFCENTER));
        yPos = (player.getYPos() + 42) - (42 * Math.sin(-angle - OFFCENTER));
        this.xSpeed = 0;
        this.ySpeed = 0;
    }

    //to string method
    public String toString() {
        return "xPos = " + xPos + ", yPos = " + yPos + ", damage = " + damage + ", active = " + active + ", hit = " + hit + '}';
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}