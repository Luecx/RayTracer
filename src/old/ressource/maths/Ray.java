package old.ressource.maths;


import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Luecx on 16.08.2017.
 */
public class Ray {

    private Vector3f position;
    private Vector3f direction;

    public Ray(Vector3f position, Vector3f direction) {
        this.position = position;
        this.direction = direction;
    }

    public Vector3f getLocation(float x) {
        return new Vector3f(position.x + direction.x * x,position.y + direction.y * x,position.z + direction.z * x);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }
}
