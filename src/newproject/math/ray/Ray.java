package newproject.math.ray;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Luecx on 18.08.2017.
 */
public class Ray {

    public Vector3f origin;
    public Vector3f direction;

    public Ray(Vector3f origin, Vector3f direction) {
        this.origin = origin;
        this.direction = direction;
        this.normalizeDirection();
    }

    public static Ray RayFromTwoPoints(Vector3f a, Vector3f b) {
        return new Ray(new Vector3f(a), new Vector3f(b.x - a.x, b.y - a.y, b.z - a.z));
    }

    public void loadFromTwoPoints(Vector3f a, Vector3f b) {
        this.origin = new Vector3f(a);
        this.direction = new Vector3f(b.x - a.x, b.y - a.y, b.z - a.z);
    }

    public void normalizeDirection() {
        this.direction.normalise();
    }

    public Vector3f calculatePosition(float x) {
        return new Vector3f(origin.x + x * direction.x, origin.y + x * direction.y, origin.z + x * direction.z);
    }

    public Vector3f getOrigin() {
        return origin;
    }

    public void setOrigin(Vector3f origin) {
        this.origin = origin;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }
}
