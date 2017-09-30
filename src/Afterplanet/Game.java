package afterplanet;

import static afterplanet.MainMenu.clip2;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import static java.awt.MouseInfo.getPointerInfo;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Game extends JPanel implements ActionListener {
    
    //load images for player, zombies, bullets, surface, and the red cover for when damage is taken
    Image pImage = new ImageIcon(this.getClass().getResource("/Images/Player.png")).getImage();
    Image zImage = new ImageIcon(this.getClass().getResource("/Images/zombie.png")).getImage();
    static BufferedImage bulletImage;
    Image image = new ImageIcon(this.getClass().getResource("/Images/surface.png")).getImage();
    Image dImage = new ImageIcon(this.getClass().getResource("/Images/damageTaken.png")).getImage();
    
    //load images for each health amount
    Image hp1 = new ImageIcon(this.getClass().getResource("/Images/health/HP1.png")).getImage();
    Image hp2 = new ImageIcon(this.getClass().getResource("/Images/health/HP2.png")).getImage();
    Image hp3 = new ImageIcon(this.getClass().getResource("/Images/health/HP3.png")).getImage();
    Image hp4 = new ImageIcon(this.getClass().getResource("/Images/health/HP4.png")).getImage();
    Image hp5 = new ImageIcon(this.getClass().getResource("/Images/health/HP5.png")).getImage();
    
    //calculate magnitude of angle that the player's gun is offset from the center of
    //the circular radius of the player.
    final static double OFFCENTER = 33 / ((2 * Math.PI) * 42);
    
    //keeps track of score
    static int score;
    
    //declare booleans (used later on)
    boolean gameOver = false;
    boolean drawDamaged = false;
    boolean leftPressed = false, rightPressed = false, upPressed = false, downPressed = false;
    boolean waveDone = false;
    
    //number of bullets
    final int BULLETCOUNT = 7;
    //keeps track of wave number
    int wave = 0;
    
    //timers used to perform tasks for a set time
    javax.swing.Timer spawn;
    javax.swing.Timer b;
    private javax.swing.Timer t;
    private java.util.Timer bulletT;
    private javax.swing.Timer incRate;
    private javax.swing.Timer bigBull;
    
    //create the player object
    static Player player;
    
    //arrayList used to store objects which are in multiples (bullets, zombies, buffs)
    static ArrayList<projectile> bullets = new ArrayList();
    ArrayList<Enemy> zombies = new ArrayList();
    ArrayList<powerUp> buffs = new ArrayList();

    //default speed of bullets and zombies
    static double bSpeedX = 3;
    static double bSpeedY = 3;
    double zSpeed = 0.5;
    
    //used later
    int bLoc;
    
    //angle used for rotating player and bullets
    static double angle, pAngle;
    
    //Constructor
    public Game() throws IOException {
        initGame(); //calls method which initializes game
    }
    
    //Method - initializes game
    private void initGame() throws IOException {
        //adds key listener (used for player input)
        addKeyListener(new keyInput());
        setFocusable(true);
        
        //instantiates the player object
        player = new Player("Blank", 640, 360);
        
        //First bullet that is created is at index '0' in bullet arrayList
        bLoc = 0;
        
        //resets the score
        score = 0;
        
        //loads the bullet image
        bulletImage = ImageIO.read(new File("src/Images/bullet.png"));
        
        //adds the three buffs to the arrayList
        buffs.add(new increaseFireRate());
        buffs.add(new extraLife());
        buffs.add(new biggerBullets());
        
        //instantiates the timers which will be used
        spawn = new javax.swing.Timer(3000, new betweenWaves()); //used to displaye information between waves
        b = new javax.swing.Timer(500, new showDamage()); //used to give notification when damage is taken
        bulletT = new java.util.Timer(); //used to shoot bullets at constant rate
        bulletT.scheduleAtFixedRate(new directBullets(), 0, 250);
        t = new javax.swing.Timer(5, this); //used to update game
        t.start();
    }

    @Override
    public void paintComponent(Graphics g) { 
        //creates Graphics2D object
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);
        
        //draws the game if game is not over
        if (!gameOver) {
            g.drawImage(image, 0, 0, null);
            drawHUD(g);
            player.draw(g, pImage);
            drawBullets(g);
            drawZombies(g);
            drawPowerUps(g);
            
            //displays information between waves if wave is complete
            if (waveDone) {
                AffineTransform transform = g2d.getTransform();
                g2d.setColor(new Color(0, 0, 0, 123));
                g2d.fillRect(320, 180, 640, 360);
                g2d.setColor(Color.red);
                g2d.setFont(new Font("Serif", Font.PLAIN, 30));
                g2d.drawString("WAVE " + (wave - 1) + " CLEARED, ", 340, 240);
                g2d.drawString("SPAWNING WAVE " + wave + "... ", 360, 270);
                g2d.drawString("YOUR CURRENT SCORE: " + score, 340, 330);
                g2d.setTransform(transform);
            }
            
            //highlights screen red if damage has been taken
            if (drawDamaged) {
                AffineTransform transform = g2d.getTransform();
                g2d.drawImage(dImage, 0, 0, this);
                g2d.setTransform(transform);
            }
        } else {
            //removes bullets and stops timer task.
            for (int i = 0; i < bullets.size(); i++ ) {
                bullets.remove(i);
            }
            bulletT.cancel();
            //displays information about the game once the game is over
            g.drawImage(image, 0, 0, null);
            g2d.setColor(new Color(0, 0, 0, 123));
            g2d.fillRect(320, 180, 640, 360);
            g2d.setColor(Color.red);
            g2d.setFont(new Font("Serif", Font.PLAIN, 30));
            g2d.drawString("GAME OVER!", 340, 240);
            g2d.drawString("YOU GOT TO WAVE " + wave, 340, 270);
            g2d.drawString("YOUR FINAL SCORE: " + score, 340, 330);
            g2d.setFont(new Font("Helvetica", Font.PLAIN, 14));
            g2d.setColor(Color.red);
            g2d.fillRect(340, 500, 160, 30);
            g2d.fillRect(770, 500, 160, 30);
            g2d.setColor(Color.white);
            g2d.drawString("Back to Main Menu", 359, 520);
            g2d.drawString("Save Score", 789, 520);
            addMouseListener(new gameOver());
        }
    }
    
    //Method - draws the bullets
    private void drawBullets(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i).isActive()) { //draws if bullet is active
                bullets.get(i).draw(g, bulletImage);
            }
        }

    }
    
    //Method - draws the HUD (i.e wave # and HP Bar)
    private void drawHUD(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform transform = g2d.getTransform();
        g2d.setColor(Color.red);
        Font font = new Font("TimesNewRoman", Font.BOLD, 36);
        TextLayout tl = new TextLayout("WAVE: " + wave, font, g2d.getFontRenderContext());
        Shape shape = tl.getOutline(null);
        g2d.setFont(font);
        g2d.drawString("WAVE: " + wave, 575, 50);
        g2d.translate(575, 50);
        g2d.setColor(Color.black);
        g2d.draw(shape);
        g2d.setTransform(transform);

        if (player.getLives() == 5) {
            g2d.drawImage(hp5, 1100, 600, this);
        } else if (player.getLives() == 4) {
            g2d.drawImage(hp4, 1100, 600, this);
        } else if (player.getLives() == 3) {
            g2d.drawImage(hp3, 1100, 600, this);
        } else if (player.getLives() == 2) {
            g2d.drawImage(hp2, 1100, 600, this);
        } else if (player.getLives() == 1) {
            g2d.drawImage(hp1, 1100, 600, this);
        }

        g2d.setTransform(transform);
    }
    
    //Method - draws the zombies
    private void drawZombies(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < zombies.size(); i++) {
            zombies.get(i).draw(g2d, zImage);
        }

    }
    
    //Method - draws powerups
    private void drawPowerUps(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < buffs.size(); i++) {
            if (buffs.get(i).isGActive()) { //if buff is active on ground, draw on ground
                buffs.get(i).draw(g);
            }
        }

        for (int k = 0; k < buffs.size(); k++) {
            if (buffs.get(k).isPActive()) { //if buff is active on player, activate buff
                buffs.get(k).activate();
            }
        }
    }
    
    //Method - moves the player
    private void movePlayer() {
        //if player reaches edge of screen, stop player from moving further
        if (player.getXPos() < 0) {
            player.setXSpeed(0);
            player.setXPos(0);
        }

        if (player.getYPos() < 0) { 
            player.setYSpeed(0);
            player.setYPos(0);
        }

        if (player.getXPos() > 1200) {
            player.setXSpeed(0);
            player.setXPos(1200);
        }

        if (player.getYPos() > 620) {
            player.setYSpeed(0);
            player.setYPos(620);
        }
        //update player's position
        player.setXPos(player.getXPos() + player.getXSpeed());
        player.setYPos(player.getYPos() + player.getYSpeed());
    }
    
    //Method - move bullets if they are active
    private void moveBullets() {
        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i).isActive()) {
                bullets.get(i).move();
            }
        }
        
        //if bullet reaches end of screen, reset the bullet
        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i).getXPos() < 0) {
                bullets.get(i).reset();
            } else if (bullets.get(i).getYPos() < 0) {
                bullets.get(i).reset();
            } else if (bullets.get(i).getXPos() > 1230) {
                bullets.get(i).reset();
            } else if (bullets.get(i).getYPos() > 670) {
                bullets.get(i).reset();
            }
        }

    }
    
    //Method - moves the zombies
    private void moveZombies() {
        double deltaX, deltaY;
        
        //if wave is not done
        if (!waveDone) {
            //if every zombies has been killed
            if (zombies.isEmpty()) {
                wave += 1; //increase wave number
                if (wave > 1) { //if it isn't the first wave
                    waveDone = true; //tells program that wave is complete
                    spawn.start(); //starts the timer which displays information to the user.
                    spawn.setRepeats(false);
                }
                if (wave == 5 || wave == 10) { //every 5 waves until wave 10, increment speed by 0.01
                    zSpeed+= 0.01;
                }
                if (wave >= 15 && zSpeed < 0.9) { //if it is past wave 15 and zombie speed is not near players
                    zSpeed += 0.01; //increase zombie speed by 0.01
                }
                spawnZombies(); //spawns the zombies
            } else { //if every zombie has not been killed
                for (int i = 0; i < zombies.size(); i++) {
                    //calculate difference between player and zombies
                    deltaX = player.getXPos() - zombies.get(i).getXPos();
                    deltaY = player.getYPos() - zombies.get(i).getYPos();
                    //direct zombie towards player
                    if (deltaX > 0) {
                        zombies.get(i).setXPos(zombies.get(i).getXPos() + zSpeed);
                    } else if (deltaX < 0) {
                        zombies.get(i).setXPos(zombies.get(i).getXPos() - zSpeed);
                    }

                    if (deltaY > 0) {
                        zombies.get(i).setYPos(zombies.get(i).getYPos() + zSpeed);
                    } else if (deltaY < 0) {
                        zombies.get(i).setYPos(zombies.get(i).getYPos() - zSpeed);
                    }
                    //sets the zombie's direction to rotate it towards the player
                    zombies.get(i).setAngle(Math.atan2(deltaY, deltaX) + Math.PI / 2);
                }
            }
        }
    }
    
    //Method - randomly spawns powerups
    private void spawnPowerUps() throws IOException {
        // 1 in 2000 chance a powerup will spawn
        int rNum = (int) (Math.random() * 2000) + 1;
        int wSpawn, wX, wY;
        
        
        if (rNum == 666) {
            //decides which type of powerup to spawn
            wSpawn = (int) (Math.random() * 3) + 1;
            // index 0 = increase fire rate, index 1 = extra life, index 2 = bigger bullets
            if (wSpawn == 1) {
                if (!buffs.get(0).isGActive() && !buffs.get(0).isPActive()) { //if buffs is not already active
                    //get random position
                    wX = (int) (Math.random() * 1210) + 70;
                    wY = (int) (Math.random() * 650) + 70;
                    //set the buffs position and set that it is active on ground
                    buffs.get(0).setXPos(wX);
                    buffs.get(0).setYPos(wY);
                    buffs.get(0).setGActive(true);

                }
            } else if (wSpawn == 2 && player.getLives() < 5) { //will only be spawned if player is not full health
                if (!buffs.get(1).isGActive() && !buffs.get(1).isPActive()) {
                    wX = (int) (Math.random() * 1210) + 70;
                    wY = (int) (Math.random() * 650) + 70;
                    buffs.get(1).setXPos(wX);
                    buffs.get(1).setYPos(wY);
                    buffs.get(1).setGActive(true);
                }
            } else if (wSpawn == 3) {
                if (!buffs.get(2).isGActive() && !buffs.get(2).isPActive()) {
                    wX = (int) (Math.random() * 1210) + 70;
                    wY = (int) (Math.random() * 650) + 70;
                    buffs.get(2).setXPos(wX);
                    buffs.get(2).setYPos(wY);
                    buffs.get(2).setGActive(true);
                }
            }
        }

    }
    
    //Method - spawns zombies
    private void spawnZombies() {
        int tempX = 0, tempY = 0;
        
        int rTemp = (int)(Math.random() * 3) + 1;
        
        //creates random number of zombies based on wave number
        for (int i = 0; i < ((wave * 3) + rTemp); i++) {
            Point zLoc = randomLocation(); //gets a random location
            if (zLoc.getX() < 0) {
                tempX = (int) zLoc.getX() - (i * 20); //moves zombie farther off screen so that each zombie doesn't arrive at exact same time
            } else if (zLoc.getX() > 0) {
                tempX = (int) zLoc.getX() + (i * 20);
            }
            if (zLoc.getY() < 0) {
                tempY = (int) zLoc.getY() - (i * 20);
            } else if (zLoc.getY() > 0) {
                tempY = (int) zLoc.getY() + (i * 20);
            }
            //System.out.println("Creating Zombie at (x,y): " + tempX + ", " + tempY);
            zombies.add(new Enemy(2, tempX, tempY));
        }
    }
    
    //Method - creates a random location off the game panel
    private Point randomLocation() {
        int xLoc2 = 0, yLoc2 = 0;
        //decides if x or y coordinate is off the screen
        double xLoc1 = (Math.random() * 2);
        double yLoc1 = (Math.random() * 2);
        
        //y coordinates are off the screen
        if (yLoc1 >= 1) {
            yLoc2 = (int) ((Math.random() * 720) + 1);
            if (yLoc2 < 360) {
                yLoc2 = -45;
                xLoc2 = (int) ((Math.random() * 1280) + 1);
            } else if (yLoc2 >= 360) {
                yLoc2 = 765;
                xLoc2 = (int) ((Math.random() * 1280) + 1);
            }
        } else if (yLoc1 < 1) { //x coordinates are off the screen
            yLoc2 = (int) ((Math.random() * 720) + 1);
            if (xLoc1 > 1) {
                xLoc2 = -45;
            } else if (xLoc1 < 1) {
                xLoc2 = 1325;
            }
        }

        //returns a Point object with the random x and y coordinates
        return new Point(xLoc2, yLoc2);
    }
    
    //Method - updates the angles used throughout the program
    public void updateAngles() {
        //gets location of Mouse on screen
        Point mouseLoc = getPointerInfo().getLocation();
        //gets location of window on screen
        int wScreenRelative = AfterPlanet.windowX;
        int hScreenRelative = AfterPlanet.windowY;

        double relativeX = 0, relativeY = 0;
        //sets the relative X coordinate based on if the x-coordinate of the mouse is off screen or on screen
        //so that speeds will not change based on distance of mouse from player and converts
        //mouse coordinates from based on the screen to based on the JPanel (so player can move mouse off the window
        //and angles will still update
        if (mouseLoc.getX() < wScreenRelative) {
            relativeX = 0;
        } else if (mouseLoc.getX() > 1280 && mouseLoc.getX() < 1280 + wScreenRelative) {
            relativeX = mouseLoc.getX() - wScreenRelative;
        } else if (mouseLoc.getX() > 1280 + wScreenRelative) {
            relativeX = 1280;
        } else if (mouseLoc.getX() > wScreenRelative && mouseLoc.getX() < 1280 + wScreenRelative) {
            relativeX = mouseLoc.getX() - wScreenRelative;
        }
        
        //sets the relative y coordinate based on if the Y-coordinate of the mouse is off screen or on screen
        //so that speeds will not change based on distance of mouse from player
        if (mouseLoc.getY() < hScreenRelative) {
            relativeY = 0;
        } else if (mouseLoc.getY() > 720 && mouseLoc.getY() < 720 + hScreenRelative) {
            relativeY = mouseLoc.getY() - hScreenRelative;
        } else if (mouseLoc.getY() > 720 + hScreenRelative) {
            relativeY = 720;
        } else if (mouseLoc.getY() > hScreenRelative && mouseLoc.getY() < 720 + hScreenRelative) {
            relativeY = mouseLoc.getY() - hScreenRelative;
        }
        
        //sets the angles
        pAngle = Math.atan2(player.getCenter().y - relativeY, player.getCenter().x - relativeX) - Math.PI / 2;
        angle = Math.atan2(player.getYPos() - relativeY, player.getXPos() - relativeX) + Math.PI;
    }
    
    //Method - performs hit detection
    private void hitDetection() {
        //checks if a bullet is hitting a zombie if wave is not completed
        if (!waveDone) {
            //loops through each zombie for each bullet
            for (int i = 0; i < bullets.size(); i++) {
                for (int j = 0; j < zombies.size(); j++) {
                    //circular hit detection - (delta x ^ 2) + (delta y ^ 2) < (r1 + r2) ^ 2
                    if ((bullets.get(i).getXPos() + (projectile.radius / 2) - zombies.get(j).getCenter().x) * (bullets.get(i).getXPos() + (projectile.radius / 2) - zombies.get(j).getCenter().x) + (bullets.get(i).getYPos() + (projectile.radius / 2) - zombies.get(j).getCenter().y) * (bullets.get(i).getYPos() + (projectile.radius / 2) - zombies.get(j).getCenter().y) <= (23 + (projectile.radius / 2)) * (23 + (projectile.radius / 2))) {
                        zombies.get(j).setHealth(zombies.get(j).getHealth() - 1);
                        if (zombies.get(j).getHealth() == 0) { //if zombie is dead
                            zombies.remove(j); //removes zombie if zombie is dead
                            score += 1; //increments score by 1 (zombie has been killed)
                        }
                    }
                }
            }
        }
        //checks if zombie is hitting player
        for (int j = 0; j < zombies.size(); j++) {
            //circular hit detection
            if ((player.getCenter().x - zombies.get(j).getCenter().x) * (player.getCenter().x - zombies.get(j).getCenter().x) + (player.getCenter().y - zombies.get(j).getCenter().y) * (player.getCenter().y - zombies.get(j).getCenter().y) <= 50 * 50) {
                player.setLives(player.getLives() - 1); //if player is hit, lower health
                zombies.remove(j); //remove the zombie (score is not increased)
                b.start(); //starts timer to show damage
                drawDamaged = true; //tells repaint to draw red overlay
                if (player.getLives() == 0) { //if player is now out of lives, tells program that game is over
                    gameOver = true;
                }

            }
        }
        
        //checks if player is walking over buff
        if (!buffs.isEmpty()) { //only if buffs exist
            for (int k = 0; k < 3; k++) {
                if (buffs.get(k).isGActive()) { //if the buff is active on the ground
                    //circular hit detection
                    if ((player.getCenter().x - buffs.get(k).getXPos() + 20) * (player.getCenter().x - buffs.get(k).getXPos() + 20) + (player.getCenter().y - buffs.get(k).getYPos() + 20) * (player.getCenter().y - buffs.get(k).getYPos() + 20) <= 62 * 62) {
                        if (k != 1) { //if buff is not extraLife, set it not active on Ground and active on player
                            buffs.get(k).setGActive(false);
                            buffs.get(k).setPActive(true);
                        }
                        if (k == 0) { //start timer for increased fire rate to be active
                            incRate = new javax.swing.Timer(5000, new incFireRateActive());
                            incRate.start();
                            incRate.setRepeats(false);
                        } else if (k == 1) { //disables extra life on ground and increases player's health
                            buffs.get(k).setGActive(false);
                            player.setLives(player.getLives() + 1);
                        } else if (k == 2) { //start timer for bigger bullets to be active
                            bigBull = new javax.swing.Timer(5000, new biggerBulletsActive());
                            bigBull.start();
                            bigBull.setRepeats(false);
                        }
                    }
                }
            }
        }
    }
    
    //Method - every time the timer ticks or an action is performed
    @Override
    public void actionPerformed(ActionEvent e) {
        //performs tasks only if game is not over
        if (!gameOver) {
            hitDetection();
            updateAngles();
            movePlayer();
            moveBullets();
            moveZombies();
            try {
                spawnPowerUps();
            } catch (IOException o) {
                System.out.println("Error: " + o);
            }

            repaint();
        } else {

        }
    }
    
    //Nested Class - used to detect player key input
    class keyInput extends KeyAdapter {
        
        //if a key is pressed, gets key code and performs corresponding event
        @Override
        public void keyPressed(KeyEvent e) {
            int c = e.getKeyCode();
            //W = up, A = right, S = down, D = left
            if (c == KeyEvent.VK_A) {
                player.setXSpeed(-1);
                leftPressed = true; //booleans used to keep track if two keys are pressed at same time
            }

            if (c == KeyEvent.VK_W) {
                player.setYSpeed(-1);
                upPressed = true;
            }

            if (c == KeyEvent.VK_D) {
                player.setXSpeed(1);
                rightPressed = true;
            }

            if (c == KeyEvent.VK_S) {
                player.setYSpeed(1);
                downPressed = true;
            }
        }
        
        //if a key is released, gets the key code and disables movement in that direction
        @Override
        public void keyReleased(KeyEvent e) {
            int c = e.getKeyCode();

            if (c == KeyEvent.VK_D) {
                rightPressed = false;
                if (!leftPressed) {
                    player.setXSpeed(0);
                }
            }

            if (c == KeyEvent.VK_A) {
                leftPressed = false;
                if (!rightPressed) {
                    player.setXSpeed(0);
                }
            }

            if (c == KeyEvent.VK_W) {
                upPressed = false;
                if (!downPressed) {
                    player.setYSpeed(0);
                }
            }

            if (c == KeyEvent.VK_S) {
                downPressed = false;
                if (!upPressed) {
                    player.setYSpeed(0);
                }
            }
        }
    }
    
    //Nested Class - creates, and directs bullets at set time intervals
    class directBullets extends TimerTask {

        @Override
        public void run() {
            //if there are less bullets than the bullet count
            if (bullets.size() != BULLETCOUNT) {
                //adds a new bullet with x and y coordinates at the location of gun barrel
                //the gun barrels location is calculated using the rotation matrix, using the players angle
                //the radius of the players motion, and the center of rotation
                bullets.add(new projectile(((player.getXPos() + 27) + (42 * Math.cos(-angle - OFFCENTER))), ((player.getYPos() + 42) - (42 * Math.sin(-angle - OFFCENTER))), 20, 20));
            }
            if (bullets.get(bLoc).isActive() == false) { //if bullet is not active, i.e. has reached edge of screen
                
                //adds a new bullet (see above)
                if ((angle >= 3 * Math.PI / 2 && angle <= 2 * Math.PI)) {
                    //angle is offset a different amount based on where the player is directed
                    bullets.get(bLoc).setXPos((player.getXPos() + 27) + (42 * Math.cos(-angle + OFFCENTER)));
                    bullets.get(bLoc).setYPos((player.getYPos() + 42) - (42 * Math.sin(-angle + OFFCENTER)));
                } else if ((angle >= Math.PI / 2 && angle <= Math.PI)) {
                    bullets.get(bLoc).setXPos((player.getXPos() + 27) + (42 * Math.cos(-angle - 2 * OFFCENTER)));
                    bullets.get(bLoc).setYPos((player.getYPos() + 42) - (42 * Math.sin(-angle - 2 * OFFCENTER)));
                } else {
                    bullets.get(bLoc).setXPos((player.getXPos() + 27) + (42 * Math.cos(-angle - OFFCENTER)));
                    bullets.get(bLoc).setYPos((player.getYPos() + 42) - (42 * Math.sin(-angle - OFFCENTER)));
                }
                //gets the location of the mouse on screen
                Point mouseLoc = getPointerInfo().getLocation();
                
                //gets the x and y coordinates of the mouse
                double mouseX = mouseLoc.getX();
                double mouseY = mouseLoc.getY();
                
                //used to set location of mouse on window
                double moveX = 0;
                double moveY = 0;
                //gets location of window on screen
                int wScreenRelative = AfterPlanet.windowX;
                int hScreenRelative = AfterPlanet.windowY;
                
                //see relative x and relative y above
                if (mouseX < wScreenRelative) {
                    moveX = 0;
                } else if (mouseX > 1280 && mouseX < 1280 + wScreenRelative) {
                    moveX = mouseX - wScreenRelative;
                } else if (mouseX > 1280 + wScreenRelative) {
                    moveX = 1280;
                } else if (mouseX > wScreenRelative && mouseX < 1280 + wScreenRelative) {
                    moveX = mouseX - wScreenRelative;
                }

                if (mouseY < hScreenRelative) {
                    moveY = 0;
                } else if (mouseY > 720 && mouseY < 720 + hScreenRelative) {
                    moveY = mouseY - hScreenRelative;
                } else if (mouseY > 720 + hScreenRelative) {
                    moveY = 720;
                } else if (mouseY > hScreenRelative && mouseY < 720 + hScreenRelative) {
                    moveY = mouseY - hScreenRelative;
                }
                
                //the distance that the bullet has to move
                double deltaX = moveX - bullets.get(bLoc).getXPos();
                double deltaY = moveY - bullets.get(bLoc).getYPos();
                
                //direction that the bullets have to go
                double direction = Math.atan(Math.abs(deltaY) / Math.abs(deltaX));
                
                //sets the speed of the bullets based on the mouse's location
                if (deltaX < 0) {
                    bullets.get(bLoc).setXSpeed((bSpeedX * Math.cos(direction)) * -1);
                } else {
                    bullets.get(bLoc).setXSpeed(bSpeedX * Math.cos(direction));
                }

                if (deltaY < 0) {
                    bullets.get(bLoc).setYSpeed((bSpeedY * Math.sin(direction)) * -1);
                } else {
                    bullets.get(bLoc).setYSpeed(bSpeedY * Math.sin(direction));
                }
                
                //sets that the bullet is active
                bullets.get(bLoc).setActive(true);
                
                //increments which bullet is being checked
                if (bLoc == (BULLETCOUNT - 1)) {
                    bLoc = 0;
                } else {
                    bLoc += 1;
                }
            }
        }
    }
    
    //Nested Class - sets time that red damage taken overlay is shown
    class showDamage implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            drawDamaged = false; //tells draw method to stop drawing red overlay
            b.stop(); //stops timer which updates this class
        }

    }
    
    //Nested Class - sets time that overlay between waves is shown
    class betweenWaves implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            waveDone = false;
            spawn.stop();
        }
    }
    
    //Nested Class - sets time that the fire rate is increased (powerup)
    class incFireRateActive implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!waveDone) {
                buffs.get(0).deActivate(); //deactivates powerup
                buffs.get(0).setXPos(0); //resets location
                buffs.get(0).setYPos(0);
                buffs.get(0).setGActive(false); //disables on ground
                buffs.get(0).setPActive(false); //disables on player
                incRate.stop(); //stops the timer
            }
        }
    }
    
    //Nested Class - sets time that bullets are bigger (powerup)
    class biggerBulletsActive implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!waveDone) {
                buffs.get(2).deActivate();
                buffs.get(2).setXPos(0);
                buffs.get(2).setYPos(0);
                buffs.get(2).setGActive(false);
                buffs.get(2).setPActive(false);
                bigBull.stop();
            }
        }
    }
    
    //Nested Class - receives player's mouse input at the Game Over screen
    class gameOver implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (gameOver) {
                if (e.getX() > 340 && e.getX() < 500 && e.getY() > 500 && e.getY() < 530) { //if mouse is pressed to go back to main menu
                    clip2.close(); //stops soundtrack
                    MainMenu.windowFrame3.setVisible(false); //disables game window
                    MainMenu.windowFrame.setVisible(true); //opens main menu
                } else if (e.getX() > 770 && e.getX() < 930 && e.getY() > 500 && e.getY() < 530) { //if mouse is pressed to save score
                    clip2.close();
                    MainMenu.windowFrame3.setVisible(false); //disables game window
                    MainMenu.windowFrame2.setVisible(true); //opens high score window
                    MainMenu.windowFrame2.addScore(score); //calls method in high score class which adds score to list
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }
}
