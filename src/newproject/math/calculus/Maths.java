package newproject.math.calculus;

import newproject.math.ray.Ray;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Luecx on 18.08.2017.
 */
public class Maths {

    public static float distance_ray_point(Ray r, Vector3f point) {
        return Vector3f.cross(r.getDirection(), Vector3f.sub(point, r.getOrigin(),null), null).length();
    }

    public static Vector3f rotateVectorAroundAxis(Vector3d vec, Vector3d axis, float theta){
        float x, y, z;
        float u, v, w;
        x=vec.getX();y=vec.getY();z=vec.getZ();
        u=axis.getX();v=axis.getY();w=axis.getZ();
        double xPrime = u*(u*x + v*y + w*z)*(1d - Math.cos(theta))
                + x*Math.cos(theta)
                + (-w*y + v*z)*Math.sin(theta);
        double yPrime = v*(u*x + v*y + w*z)*(1d - Math.cos(theta))
                + y*Math.cos(theta)
                + (w*x - u*z)*Math.sin(theta);
        double zPrime = w*(u*x + v*y + w*z)*(1d - Math.cos(theta))
                + z*Math.cos(theta)
                + (-v*x + u*y)*Math.sin(theta);
        return new Vector3f((float)xPrime, (float)yPrime, (float)zPrime);
    }

}
