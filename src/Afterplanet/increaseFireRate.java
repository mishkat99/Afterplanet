package afterplanet;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.io.IOException;
import javax.swing.ImageIcon;

public class increaseFireRate extends powerUp implements ImageObserver {
    //declare image of the power up
    Image image;
    
    //constructors
    public increaseFireRate() {
        super();
        image = new ImageIcon("src/Images/powerups/increasedFireRate.png").getImage();
    }
    
    public increaseFireRate(int x, int y) throws IOException {
        super(x, y);
        image = new ImageIcon("src/Images/powerups/increasedFireRate.png").getImage();
    }
    
    //draw method
    @Override
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, xPos, yPos, this);
    }
    
    //activate increases speed
    @Override
    public void activate() {
        Game.bSpeedX = 6;
        Game.bSpeedY = 6;
    }
    
    //deactivate resets 
    @Override
    public void deActivate() {
        Game.bSpeedX = 3;
        Game.bSpeedY = 3;
    }
    
    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
