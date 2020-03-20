package advancedVersion.render;

import core.vector.Vector3d;

public class Ray {

    private Vector3d root;
    private Vector3d direction;

    public Ray(Vector3d root, Vector3d direction) {
        this.root = root;
        this.direction = direction.self_normalise();
    }

    public Vector3d getRoot() {
        return root;
    }

    public void setRoot(Vector3d root) {
        this.root = root;
    }

    public Vector3d getDirection() {
        return direction;
    }

    public void setDirection(Vector3d direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Ray{" +
               "root=" + root +
               ", direction=" + direction +
               '}';
    }
}
