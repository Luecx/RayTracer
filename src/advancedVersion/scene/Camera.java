package advancedVersion.scene;

import advancedVersion.maths.RotationMatrix;
import advancedVersion.render.Raster;
import advancedVersion.render.Ray;
import core.vector.Vector3d;

public class Camera {

    private Vector3d rotation;
    private Vector3d position;

    public Camera(Vector3d position, Vector3d rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Vector3d getRotation() {
        return rotation;
    }

    public void setRotation(Vector3d rotation) {
        this.rotation = rotation;
    }

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public Raster createRaster(int w, int h, double hFOV){
        RotationMatrix rotationMatrix = RotationMatrix.rotate(rotation.getX(), rotation.getY(), rotation.getZ());

        double h_fov = hFOV;
        double v_fov = h_fov * h / w;

        double B = 2 * Math.sin(h_fov/2);
        double H = 2 * Math.sin(v_fov/2);

        Raster raster = new Raster(w,h);


        for(int i = 0; i < w; i++){
            for(int n = 0; n < h; n++){

                double x = ((double)i / (w-1) - 0.5) * B;
                double y = -((double)n / (h-1) - 0.5) * H;

                Vector3d unrotated = new Vector3d(x,y,-1);
                Vector3d rotated = new Vector3d(rotationMatrix.mul(unrotated));

                raster.getRays()[i][n] = new Ray(getPosition().copy(), rotated);
            }
        }
        return raster;

    }


}
