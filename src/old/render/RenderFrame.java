package old.render;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Luecx on 16.08.2017.
 */
public class RenderFrame extends JFrame{

    public static BufferedImage image;

    public RenderFrame() throws HeadlessException {
        this.setDefaultCloseOperation(3);
        this.setSize(720,480);
        this.setTitle("Rendered Image");

        this.setLayout(new BorderLayout());

        JPanel p = new JPanel(){



            @Override
            protected void paintComponent(Graphics g) {
                g.clearRect(0,0,10000,10000);
                g.drawImage(RenderFrame.image, 0,0,null);
            }
        };

        this.getContentPane().add(p, BorderLayout.CENTER);
    }
}
