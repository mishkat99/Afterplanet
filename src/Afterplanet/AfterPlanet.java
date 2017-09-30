package afterplanet;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class AfterPlanet extends JFrame {

    //gets the dimensions of the users monitor
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    //gets the width and height of the users monitor
    final public static int WIDTH = (int) screenSize.getWidth();
    final public static int HEIGHT = (int) screenSize.getHeight();

    //class variables
    public static int windowX;
    public static int windowY;
    
    //starts new thread which will update windows location if window is moved
    //so game will work no matter where the window is
    public Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            addComponentListener(new ComponentListener() {
                @Override
                public void componentResized(ComponentEvent e) {
                }

                @Override
                public void componentMoved(ComponentEvent e) { //detects if window has been moved
                    windowX = getLocation().x; //updates window location
                    windowY = getLocation().y;
                    System.out.println(windowX);
                }

                @Override
                public void componentShown(ComponentEvent e) {
                }

                @Override
                public void componentHidden(ComponentEvent e) {
                }
            });
        }
    ;

    });
    
    public AfterPlanet() throws IOException {
        initUI();
    }

    //initializes JFrame
    private void initUI() throws IOException {
        add(new Game());
        setTitle("AfterPlanet");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocation((WIDTH - 1280) / 2, (HEIGHT - 720) / 2);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        //gets windows position on screen
        windowX = getLocation().x;
        windowY = getLocation().y;
        t.start(); //starts the thread
    }

}
