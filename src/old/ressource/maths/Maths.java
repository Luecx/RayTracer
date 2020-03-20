package old.ressource.maths;

import org.lwjgl.util.vector.Vector3f;
import old.ressource.entities.Triangle;
/**
 * Created by Luecx on 16.08.2017.
 */
public class Maths {


    public static final float SMALL_NUM = 0.00000001f;

    public static Vector3f cross(Vector3f a, Vector3f b) {
        return new Vector3f(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z,a.x * b.y - a.y * b.x);
    }

    public static float distanceRayPoint(Ray r, Vector3f p) {
        return cross(r.getDirection(), Vector3f.sub(p, r.getPosition(),null)).length();
    }

    public static float dot(Vector3f a, Vector3f b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public static Vector3f intersectRayTriangle(Ray R, Triangle T) {

        if(R.getDirection().length() != 1) {
            R.getDirection().normalise();
        }

        Vector3f e1 = new Vector3f(
                T.getV2().getPosition().x - T.getV1().getPosition().x,
                T.getV2().getPosition().y - T.getV1().getPosition().y,
                T.getV2().getPosition().z - T.getV1().getPosition().z);
        Vector3f e2 = new Vector3f(
                T.getV3().getPosition().x - T.getV1().getPosition().x,
                T.getV3().getPosition().y - T.getV1().getPosition().y,
                T.getV3().getPosition().z - T.getV1().getPosition().z);

        Vector3f pvec = cross(R.getDirection(), e2);

        float det = dot(e1, pvec);

        if (det < SMALL_NUM && det > -SMALL_NUM) {
            return null;
        }

        float inv_det = 1 / det;
        Vector3f tvec = new Vector3f(
                R.getPosition().x - T.getV1().getPosition().x,
                R.getPosition().y - T.getV1().getPosition().y,
                R.getPosition().z - T.getV1().getPosition().z);
        float u = dot(tvec, pvec) * inv_det;
        if(u < 0 || u > 1) {
            return null;
        }

        Vector3f qvec = cross(tvec, e1);
        float v = dot(R.getDirection(), qvec) * inv_det;
        if (v < 0 || u + v > 1) {
            return null;
        }
        float dist = dot(e2, qvec) * inv_det;
        if(dist <= 0) return null;
        return R.getLocation(dist);

    }

    public static boolean intersectRayBoundingBox(Ray r, float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
        double tmin = Float.NEGATIVE_INFINITY, tmax = Float.POSITIVE_INFINITY;
        double t1, t2;

        t1 = (minX - r.getPosition().getX()) * r.getDirection().getX();
        t2 = (maxX - r.getPosition().getX()) * r.getDirection().getX();

        tmin = Math.max(tmin, Math.min(t1, t2));
        tmax = Math.min(tmax, Math.max(t1, t2));

        t1 = (minY - r.getPosition().getY()) * r.getDirection().getY();
        t2 = (maxY - r.getPosition().getY()) * r.getDirection().getY();

        tmin = Math.max(tmin, Math.min(t1, t2));
        tmax = Math.min(tmax, Math.max(t1, t2));

        t1 = (minZ - r.getPosition().getZ()) * r.getDirection().getZ();
        t2 = (maxZ - r.getPosition().getZ()) * r.getDirection().getZ();

        tmin = Math.max(tmin, Math.min(t1, t2));
        tmax = Math.min(tmax, Math.max(t1, t2));

        return tmax > Math.max(tmin, 0.0);
    }

    public static float[] intersectRayBoundingBoxInAndOut(Ray r, float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
        float tmin = Float.NEGATIVE_INFINITY, tmax = Float.POSITIVE_INFINITY;
        float t1, t2;

        t1 = (minX - r.getPosition().getX()) * r.getDirection().getX();
        t2 = (maxX - r.getPosition().getX()) * r.getDirection().getX();

        tmin = Math.max(tmin, Math.min(t1, t2));
        tmax = Math.min(tmax, Math.max(t1, t2));

        t1 = (minY - r.getPosition().getY()) * r.getDirection().getY();
        t2 = (maxY - r.getPosition().getY()) * r.getDirection().getY();

        tmin = Math.max(tmin, Math.min(t1, t2));
        tmax = Math.min(tmax, Math.max(t1, t2));

        t1 = (minZ - r.getPosition().getZ()) * r.getDirection().getZ();
        t2 = (maxZ - r.getPosition().getZ()) * r.getDirection().getZ();

        tmin = Math.max(tmin, Math.min(t1, t2));
        tmax = Math.min(tmax, Math.max(t1, t2));

        System.out.println(tmin + "   " + tmax);

        if (tmax > Math.max(tmin, 0.0)){
            return new float[]{tmin, tmax};
        }else{
            return null;
        }
    }

}
