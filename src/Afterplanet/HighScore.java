package afterplanet;

import static afterplanet.MainMenu.windowFrame;
import static afterplanet.MainMenu.windowFrame2;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class HighScore extends JFrame implements MouseListener {

    
    public static final int canvasX = 618;
    public static final int canvasY = 590;
    //Makes the new arrays for storing the highscores 
    static int highScores[] = new int[10];

    static int dataFileList[] = new int[10];

    static int dataCounter = 0;
    
    //Creates a new draw panel 
    DrawPanel drawPanel = new DrawPanel();

    public HighScore() {
        //Intializes the compents of the window 
        initScores();
        add(drawPanel);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(canvasX, canvasY);
        setLocationRelativeTo(null);
        setVisible(false);
        drawPanel.addMouseListener(this);
        addMouseListener(this);

    }

    private void initScores() {
        //Places the scores in an array 
        for (int i = 0; i < 10; i++) {
            highScores[i] = 0;
        }
    }

    private void drawScores(Graphics g) {
        //Calls the sort method 
        sort(dataFileList, 0, 9);
        for (int i = 0; i < 10; i++) {
            //Draws the sorted score 
            String s = (i + 1) + ": " + dataFileList[i];
            g.drawString(s, 200, 140 + (30 * i));
        }
    }

    public void addScore(int score) {
        
        //Writes the highscores to a data file 
        Writer writer = null;
        try {
            //Creates a new writer 
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("highScore.txt"), "utf-8"));
            writer.write(score + "\n");
        } catch (IOException ex) {
            // report
        } finally {
            try {
                //Closes the wirter 
                writer.close();
            } catch (Exception ex) {/*ignore*/

            }
        }

        //Reads the highscore data file 
        try {
            FileReader fr = new FileReader("highScore.txt");
            BufferedReader br = new BufferedReader(fr);

            boolean eof = false;
            
            
            //Loop runs while the file has not ended 
            while (!eof) {
                dataCounter++;
                String hold = br.readLine();

                System.out.println(hold);
                

                if(hold == null){
                    eof = true; 
                }else{
                    //Puts sorted list into the array 
                    dataFileList[dataCounter] = Integer.parseInt(hold);
                }
            }

        } catch (IOException e) {
            System.out.println("Error : " + e);
        }
    }

    //Sort method 
    public static int[] sort(int[] a, int low, int high) {
        int i = low;
        int j = high;
        int temp;
        //Makes the middle 
        int middle = a[(low + high) / 2];

        while (i < j) {
            while (a[i] > middle) {
                i++;
            }
            while (a[j] < middle) {
                j--;
            }
            //If the i is < the j it makes the swap
            if (j >= i) {
                temp = a[i];
                a[i] = a[j];
                a[j] = temp;
                i++;
                j--;
            }
        }
        //recursively sorts two subparts
        if (low < j) {
            sort(a, low, j);
        }
        //recursively sorts two subparts
        if (i < high) {
            sort(a, i, high);
        }

        return a;
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
        //COnverts the mouse x and y to screen locations 
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        SwingUtilities.convertPointFromScreen(b, this);
        //Puts mouse x and y into variables 
        int x = (int) b.getX();
        int y = (int) b.getY();

        if (x > 0 && x < 100 && y > 545 && y < 590) {
            //Opens and closes windows 
            windowFrame.setVisible(true);
            windowFrame2.setVisible(false);
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
            //Imports image as background
            Image image = new ImageIcon(this.getClass().getResource("/Images/HighScore.png")).getImage();

            g.drawImage(image, 0, 0, null);

            g.setColor(Color.GRAY);
            //Draws the buttons and window components 
            g.fill3DRect(200, 100, 200, 320, true);

            g.fill3DRect(0, 515, 100, 40, true);

            g.setColor(Color.black);

            g.drawString("High Score", 270, 120);

            g.drawString("Back", 30, 540);

            drawScores(g);

            // sort();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(canvasX, canvasY);

        }
    }

}
