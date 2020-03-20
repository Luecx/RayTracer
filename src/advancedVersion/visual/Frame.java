package advancedVersion.visual;

import advancedVersion.scene.Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Frame extends JFrame {

    private Panel panel;

    public Frame(){
        super();
        this.setDefaultCloseOperation(3);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(720,480));
        this.setSize(new Dimension(720,480));
        this.panel = new Panel();
        this.add(panel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void setImage(BufferedImage image){
        panel.setImage(image);
    }

    public static void main(String[] args) {
        Scene scene = new Scene();


    }
}