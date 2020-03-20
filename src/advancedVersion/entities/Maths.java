package advancedVersion.entities;


import advancedVersion.render.Ray;
import core.vector.Vector3d;

/**
 * Created by Luecx on 18.08.2017.
 */
public class Maths {

    public static double distance_ray_point(Ray r, Vector3d point) {
        return r.getDirection().cross(point.sub(r.getRoot())).length();
    }

//    public static Vector3f rotateVectorCC(Vector3f vec, Vector3f axis, float theta){
//        float x, y, z;
//        float u, v, w;
//        x=vec.getX();y=vec.getY();z=vec.getZ();
//        u=axis.getX();v=axis.getY();w=axis.getZ();
//        double xPrime = u*(u*x + v*y + w*z)*(1d - Tools.cos(theta))
//                + x*Tools.cos(theta)
//                + (-w*y + v*z)*Tools.sin(theta);
//        double yPrime = v*(u*x + v*y + w*z)*(1d - Tools.cos(theta))
//                + y*Tools.cos(theta)
//                + (w*x - u*z)*Tools.sin(theta);
//        double zPrime = w*(u*x + v*y + w*z)*(1d - Tools.cos(theta))
//                + z*Tools.cos(theta)
//                + (-v*x + u*y)*Tools.sin(theta);
//        return new Vector3f((float)xPrime, (float)yPrime, (float)zPrime);
//    }

}
