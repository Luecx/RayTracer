package advancedVersion.render;

import advancedVersion.render.Ray;
import core.vector.Vector3d;

public class Raster {

    private Ray[][] rays;
    private int w,h;

    public Raster(int w, int h){
        rays = new Ray[w][h];
        this.w = w;
        this.h = h;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public Ray[][] getRays() {
        return rays;
    }


}
