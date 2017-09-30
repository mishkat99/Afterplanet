package afterplanet;

import java.awt.*;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MainMenu extends JFrame implements MouseListener {

    private static final int canvasX = 1092;
    private static final int canvasY = 700;
    static MainMenu windowFrame = new MainMenu();
    static HighScore windowFrame2 = new HighScore();
    static AfterPlanet windowFrame3;
    public static int windowX;
    public static int windowY;
    static boolean gameOn = false;
    static Clip clip1;
    static Clip clip2;
    static InputStream input;
    AudioInputStream audioIn;

    DrawPanel drawPanel = new DrawPanel();

    public MainMenu() {
        //Initializaes the components of the window 
        add(drawPanel);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        drawPanel.addMouseListener(this);
        addMouseListener(this);
        windowX = getLocation().x;
        windowY = getLocation().y;

        try {
            //Plays the music for the main screen 
            playClip1();
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }

    }

    public void mouseMoved(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //Converts the mouse x and y to screen locations 
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        SwingUtilities.convertPointFromScreen(b, this);
        //Puts mouse x and y into variables 
        int x = (int) b.getX();
        int y = (int) b.getY();
        //Checks if the buttons are clicked 
        if (x > 500 && x < 625 && y > 330 && y < 360) {
            //Changes what window is visable 
            windowFrame.setVisible(false);
            try {
                windowFrame3 = new AfterPlanet();
            } catch (IOException ex) {
                System.out.println("Error: " + ex);
            }
            //Changes what window is visable 
            windowFrame3.setVisible(true);
            gameOn = true;
            clip1.close();
            try {
                playClip2();
            } catch (Exception ex) {
                System.out.println("Error: " + ex);
            }
            //Checks if the buttons are clicked 
        } else if (x > 500 && x < 625 && y > 380 && y < 410) {
            //Changes what window is visable 
            windowFrame.setVisible(false);
            windowFrame2.setVisible(true);
            //Checks if the buttons are clicked
        } else if (x > 500 && x < 625 && y > 430 && y < 460) {
            System.exit(0);
        }

    }

    public void playClip2() throws Exception {
        //Plays the game music 
        try {
            //Cretaes file path 
            InputStream input = MainMenu.class.getResource("/Sound/GameAudio.wav")
                    .openStream();
            audioIn = AudioSystem.getAudioInputStream(input);
            clip2 = AudioSystem.getClip();
            clip2.open(audioIn);
            clip2.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (IOException r) {
            r.printStackTrace();
        }

    }

    public void playClip1() throws Exception {
        //Playes the music for the main menu
        try {
            //Creates the file path 
            input = MainMenu.class.getResource("/Sound/MainMenu.wav")
                    .openStream();
            audioIn = AudioSystem.getAudioInputStream(input);
            clip1 = AudioSystem.getClip();
            clip1.open(audioIn);
            clip1.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (IOException r) {
            r.printStackTrace();
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

    private class DrawPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            //Paints the components of the window 
            super.paintComponent(g);
            //Sets the image for th ebackground 
            Image image = new ImageIcon(this.getClass().getResource("/Images/V1Background.png")).getImage();
            //Draws all of the components of the window 
            g.drawImage(image, 0, 0, null);

            g.fill3DRect(500, 300, 125, 30, true);

            g.fill3DRect(500, 350, 125, 30, true);

            g.fill3DRect(500, 400, 125, 30, true);

            g.setColor(Color.red);
            g.drawString("Play", 550, 320);
            g.drawString("High Score", 535, 370);
            g.drawString("Exit", 550, 420);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(canvasX, canvasY);
        }
    }

    public static void main(String[] args) {
        // Open an input stream  to the audio file.

        SwingUtilities.invokeLater(
                new Runnable() {

                    @Override
                    public void run() {
                        //make sure it can be seen
                        windowFrame2.setVisible(false);
                        windowFrame.setVisible(true);
                        
                        
                    }
                }
        );

    }
}
