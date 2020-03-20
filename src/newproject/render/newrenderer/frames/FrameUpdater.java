package newproject.render.newrenderer.frames;

import javax.swing.*;

/**
 * Created by Luecx on 23.08.2017.
 */
public class FrameUpdater extends Thread{

    private JFrame frame;
    private int millis = 100;

    public FrameUpdater(JFrame frame) {
        this.frame = frame;
        this.start();
    }

    public FrameUpdater(JFrame frame, int millis) {
        this.frame = frame;
        this.millis = millis;
        this.start();
    }

    public void run(){
        try{
            while(this.isInterrupted() == false) {
                frame.repaint();
                frame.revalidate();
                Thread.sleep(millis);
            }
        }catch (Exception e) {
            this.interrupt();
        }
    }

    public static void main(String[] args) {
        TimeFrame f = new TimeFrame();
        f.setVisible(true);
    }
}