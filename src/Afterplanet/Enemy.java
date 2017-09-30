package afterplanet;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

//subclass of entity
public class Enemy extends Entity implements ImageObserver {
    boolean active;
    //attribute, used to rotate zombie
    double angle;
    //attribute, used to save location of zombie's center (used in hitdetection)
    Point center = new Point();
    
    //constructor
    public Enemy(){
        super();
        active = false;
    }
    
    //constructor
    public Enemy(int health, double xPos, double yPos) {
        this.health = health;
        this.xPos = xPos;
        this.yPos = yPos;
        this.angle = 0;
        active = true;
    }
    
    //constructor
    public Enemy(boolean a, int health, double xPos, double yPos){
        super(health, xPos, yPos);
        active = a;
    }
    
    //Accessors and Mutators
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void setAngle(double angle) {
        this.angle = angle;
    }
    
    public double getAngle() {
        return this.angle;
    }
    
    public void setCenter(Point p) {
        center = p;
    }
    
    public Point getCenter() {
        return center;
    }
    
    //Draws the zombie, then rotates it towards the player
    public void draw(Graphics2D g2d, Image zImage){
        AffineTransform transform = g2d.getTransform();
        center.setLocation(xPos + 22.5, yPos + 22.5);
        g2d.rotate(angle, this.xPos + 22.5, this.yPos + 22.5);
        g2d.drawImage(zImage, (int)xPos, (int)yPos, this);
        g2d.setTransform(transform);
    }
    //To String method, returns if specified zombie is active
    public String toString() {
        return "Enemy{" + "active=" + active + '}';
    }
    
    //Throws error if image file is updated
    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    
}
