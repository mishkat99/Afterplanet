package afterplanet;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.IOException;
import javax.swing.ImageIcon;

public class extraLife extends powerUp implements ImageObserver{
    //declares image
    Image image;
    
    //constructors
    public extraLife() {
        super();
        image = new ImageIcon("src/Images/powerups/increaseLife.png").getImage();
    }
    
    public extraLife(int x, int y) throws IOException {
        super(x, y);
        image = new ImageIcon("src/Images/powerups/increaseLife.png").getImage();
    }
    
    //draws the power up
    @Override
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, xPos, yPos, this);
    }
    
    
    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
