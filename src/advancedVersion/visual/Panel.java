package advancedVersion.visual;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Panel extends JPanel {

    private BufferedImage image;

    private class Updater extends Thread{

        private Panel panel;

        public Updater(Panel panel) {
            this.panel = panel;
            this.start();
        }

        @Override
        public void run() {
            while(!this.isInterrupted()){
                try {
                    Thread.sleep(100);
                    if(panel != null){
                        panel.repaint();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    this.interrupt();
                }
            }
        }
    }

    public Panel() {
        new Updater(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        if(image == null){
            return;
        }

        g.clearRect(0,0,this.getWidth(), this.getHeight());
        g.drawImage(image, 0,0,this.getWidth(), this.getHeight(),this);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
