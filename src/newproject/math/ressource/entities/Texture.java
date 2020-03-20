package newproject.math.ressource.entities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Luecx on 09.08.2017.
 */
public class Texture {

    public BufferedImage bufferedImage;

    public Texture(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public Texture(String file) {
        try{
            bufferedImage = ImageIO.read(new File(file));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }
}
