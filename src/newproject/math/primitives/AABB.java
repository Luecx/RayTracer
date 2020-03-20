package newproject.math.primitives;

import newproject.math.ray.ComplexIntersectable;
import newproject.math.ray.Ray;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Luecx on 18.08.2017.
 */
public class AABB implements ComplexIntersectable{

    private Vector3f center;
    private Vector3f dimensions;

    public AABB(Vector3f center, Vector3f dimensions) {
        this.center = center;
        this.dimensions = dimensions;
    }

    public Vector3f getStartPoint() {
        return Vector3f.sub(center, (Vector3f)new Vector3f(dimensions).scale(0.5f), null);
    }

    public Vector3f getCenter() {
        return center;
    }

    public void setCenter(Vector3f center) {
        this.center = center;
    }

    public Vector3f getDimensions() {
        return dimensions;
    }

    public void setDimensions(Vector3f dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public float intersectionValues(Ray ray) {

        Vector3f dirfrac = new Vector3f();
        dirfrac.x = 1.0f / ray.getDirection().x;
        dirfrac.y = 1.0f / ray.getDirection().y;
        dirfrac.z = 1.0f / ray.getDirection().z;
        
        Vector3f lb = this.getStartPoint();
        Vector3f rt = Vector3f.add(lb, this.dimensions, null);

        float t1 = (lb.x - ray.getOrigin().x)*dirfrac.x;
        float t2 = (rt.x - ray.getOrigin().x)*dirfrac.x;
        float t3 = (lb.y - ray.getOrigin().y)*dirfrac.y;
        float t4 = (rt.y - ray.getOrigin().y)*dirfrac.y;
        float t5 = (lb.z - ray.getOrigin().z)*dirfrac.z;
        float t6 = (rt.z - ray.getOrigin().z)*dirfrac.z;

        float tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
        float tmax =Math. min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));

// if tmax < 0, ray (line) is intersecting AABB, but the whole AABB is behind us
        if (tmax < 0)
        {
            return -1;
        }

// if tmin > tmax, ray doesn't intersect AABB
        if (tmin > tmax)
        {
            return -1;
        }

        return tmin;
    }

    @Override
    public Vector3f intersectionPoint(Ray ray) {
        try {
            throw new Exception("Not yet implemented");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;    }

    @Override
    public boolean intersectable(Ray ray) {
        if(intersectionValues(ray) != -1) return true;
        return false;
    }

    @Override
    public boolean pointInside(Vector3f p) {
        Vector3f start = getStartPoint();
        return (p.x >= start.x && p.x <= start.x + dimensions.x &&
                p.y >= start.y && p.y <= start.y + dimensions.y &&
                p.z >= start.z && p.z <= start.z + dimensions.z);
    }

    @Override
    public String toString() {
        return "AABB{" +
                "center=" + center +
                ", dimensions=" + dimensions +
                '}';
    }
}
