package advancedVersion.maths;

import advancedVersion.render.Ray;
import core.vector.Vector3d;

import java.util.ArrayList;

public class Tools {


    /**
     * generates rays from the root to a disc given by its center and a radius.
     * @return
     */
    public static ArrayList<Ray> generateRandomRaysToDisc(Vector3d root, Vector3d target, double radius, int count){
        Vector3d connection = target.sub(root).self_normalise();
        Vector3d x1 = new Vector3d(-connection.getY(), connection.getX(), 0).self_normalise();

        ArrayList<Ray> rays = new ArrayList<>();
        for(int i = 0; i < count; i++){
            Vector3d radial = target.add(x1.scale(Math.random() * radius));
            Vector3d circular = RotationMatrix.rotateVectorAroundAxis(radial, connection, Math.random() * 2*Math.PI);
            rays.add(new Ray(root, circular.sub(root)));
        }
        return rays;
    }

}
