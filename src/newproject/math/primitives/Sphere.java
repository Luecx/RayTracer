package newproject.math.primitives;

import newproject.math.calculus.Maths;
import newproject.math.ray.ComplexIntersectable;
import newproject.math.ray.Ray;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Luecx on 18.08.2017.
 */
public class Sphere implements ComplexIntersectable {

    private Vector3f origin;
    private float radius;

    public Sphere(Vector3f origin, float radius) {
        this.origin = origin;
        this.radius = radius;
    }

    public Vector3f getOrigin() {
        return origin;
    }

    public void setOrigin(Vector3f origin) {
        this.origin = origin;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public Vector3f intersectionPoint(Ray ray) {
        try {
            throw new Exception("Not yet implemented");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public float intersectionValues(Ray ray) {
        try {
            throw new Exception("Not yet implemented");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean pointInside(Vector3f p) {
        return (Vector3f.sub(this.origin, p, null).length() <= radius);
    }

    @Override
    public boolean intersectable(Ray ray) {
        return radius >= Maths.distance_ray_point(ray, this.origin);
    }
}
