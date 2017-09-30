package afterplanet;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class biggerBullets extends powerUp implements ImageObserver{
    
    //declares image
    Image image;
    
    //constructors
    public biggerBullets() {
        super();
        image = new ImageIcon("src/Images/powerups/increasedBulletSize.png").getImage();
    }
    
    public biggerBullets(int x, int y) throws IOException {
        super(x,y);
        image = new ImageIcon("src/Images/powerups/increasedBulletSize.png").getImage();
    }
    
    //draw method to draw power up
    @Override
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, xPos, yPos, this);
    }
    
    //activates the power up, increases bullet size
    @Override
    public void activate() {
        try {
            Game.bulletImage = ImageIO.read(new File("src/Images/powerups/biggerBullet.png"));
            projectile.radius = 30;
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
    
    //resets bullet after duration
    @Override
    public void deActivate() {
        try {
            Game.bulletImage = ImageIO.read(new File("src/Images/bullet.png"));
            projectile.radius = 8;
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
    
    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
