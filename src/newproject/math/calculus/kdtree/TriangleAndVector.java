package newproject.math.calculus.kdtree;

import newproject.math.ressource.entities.Triangle;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Luecx on 19.08.2017.
 */

public class TriangleAndVector {
    Triangle triangle;
    Vector3f vector;

    public TriangleAndVector(Triangle triangle) {
        this.triangle = triangle;
        this.vector = (Vector3f)Vector3f.add(Vector3f.add(triangle.getV1().getPosition(), triangle.getV2().getPosition(), null), triangle.getV3().getPosition(), null).scale(1f / 3f);
    }

    public TriangleAndVector(Triangle triangle, Vector3f center) {
        this.triangle = triangle;
        this.vector = center;
    }

    public Triangle getTriangle() {
        return triangle;
    }

    public void setTriangle(Triangle triangle) {
        this.triangle = triangle;
    }

    public Vector3f getVector() {
        return vector;
    }

    public void setVector(Vector3f vector) {
        this.vector = vector;
    }

    @Override
    public String toString() {
        return "TriangleCenter{" +
                "triangle=" + triangle +
                ", vector=" + vector +
                '}';
    }
}

