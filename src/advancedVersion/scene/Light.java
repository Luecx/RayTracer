package advancedVersion.scene;

import core.vector.Vector;
import core.vector.Vector3d;

public class Light extends Vector3d {

    private double radius;

    public Light(double radius) {
        this.radius = radius;
    }

    public Light(double x, double y, double z, double radius) {
        super(x, y, z);
        this.radius = radius;
    }

    public Light(Vector<?> other, double radius) {
        super(other);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
