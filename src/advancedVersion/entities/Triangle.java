package advancedVersion.entities;

import advancedVersion.render.Ray;
import core.vector.Vector3d;

public class Triangle extends Element {


    private Vector3d vertex1, vertex2, vertex3;
    private Vector3d normal1, normal2, normal3;

    private Vector3d normal, center;
    private Vector3d min, max;

    public Triangle(Vector3d v1, Vector3d v2, Vector3d v3) {
        this.vertex1 = v1;
        this.vertex2 = v2;
        this.vertex3 = v3;
        recalculate();
    }

    public Triangle(Vector3d v1, Vector3d v2, Vector3d v3, Vector3d n1, Vector3d n2, Vector3d n3) {
        this.vertex1 = v1;
        this.vertex2 = v2;
        this.vertex3 = v3;
        this.normal1 = n1;
        this.normal2 = n2;
        this.normal3 = n3;
        recalculate();
    }

    @Override
    public void recalculate() {
        this.normal = vertex2.sub(vertex1).cross(vertex3.sub(vertex1)).self_normalise();



        this.center = vertex1.add(vertex2).self_add(vertex3).scale(1 / 3d);
        max = new Vector3d(
                Math.max(vertex1.getX(), Math.max(vertex2.getX(), vertex3.getX())),
                Math.max(vertex1.getY(), Math.max(vertex2.getY(), vertex3.getY())),
                Math.max(vertex1.getZ(), Math.max(vertex2.getZ(), vertex3.getZ())));
        min = new Vector3d(
                Math.min(vertex1.getX(), Math.min(vertex2.getX(), vertex3.getX())),
                Math.min(vertex1.getY(), Math.min(vertex2.getY(), vertex3.getY())),
                Math.min(vertex1.getZ(), Math.min(vertex2.getZ(), vertex3.getZ())));
    }

    private static final double EPSILON = 0.00001;

    public double rayIntersectsTriangle(Vector3d rayOrigin,
                                        Vector3d rayVector) {
        Vector3d edge1;
        Vector3d edge2;
        Vector3d h;
        Vector3d s;
        Vector3d q;
        double a, f, u, v;
        edge1 = vertex2.sub(vertex1);
        edge2 = vertex3.sub(vertex1);
        h = rayVector.cross(edge2);
        a = edge1.dot(h);
        if (a > -EPSILON && a < EPSILON) {
            return Double.NaN;
        }
        f = 1.0 / a;
        s = rayOrigin.sub(vertex1);
        u = f * (s.dot(h));
        if (u < 0.0 || u > 1.0) {
            return Double.NaN;
        }
        q = s.cross(edge1);
        v = f * rayVector.dot(q);
        if (v < 0.0 || u + v > 1.0) {
            return Double.NaN;
        }
        double t = f * edge2.dot(q);
        if (t > EPSILON) // ray intersection
        {
            return t;
        } else // This means that there is a line intersection but not a ray intersection.
        {
            return Double.NaN;
        }
    }

    @Override
    public double intersection(Ray ray) {
        return rayIntersectsTriangle(ray.getRoot(), ray.getDirection());
    }

    @Override
    public Vector3d getCenter() {
        return center;
    }

    public double interpolateValue(Vector3d location, double val_1, double val_2, double val_3){


        double w1 = (
                            (vertex2.getY() - vertex3.getY()) * (location.getX() - vertex3.getX()) +
                            (vertex3.getX() - vertex2.getX()) * (location.getY() - vertex3.getY())
                    ) /
                    (
                            (vertex2.getY() - vertex3.getY()) * (vertex1.getX() - vertex3.getX()) +
                            (vertex3.getX() - vertex2.getX()) * (vertex1.getY() - vertex3.getY())
                    );

        double w2 = (
                            (vertex3.getY() - vertex1.getY()) * (location.getX() - vertex3.getX()) +
                            (vertex1.getX() - vertex3.getX()) * (location.getY() - vertex3.getY())
                    ) /
                    (
                            (vertex2.getY() - vertex3.getY()) * (vertex1.getX() - vertex3.getX()) +
                            (vertex3.getX() - vertex2.getX()) * (vertex1.getY() - vertex3.getY())
                    );
        double w3 = 1-w2-w1;

//        Ray r1 = new Ray(vertex3, vertex3.sub(vertex2));
//        Ray r2 = new Ray(vertex1, vertex1.sub(vertex3));
//        Ray r3 = new Ray(vertex2, vertex2.sub(vertex1));
//
//        double p1 = Maths.distance_ray_point(r1, location) / Maths.distance_ray_point(r1, vertex1);
//        double p2 = Maths.distance_ray_point(r2, location) / Maths.distance_ray_point(r2, vertex2);
//        double p3 = Maths.distance_ray_point(r3, location) / Maths.distance_ray_point(r3, vertex3);
        return (w1 * val_1 + w2 * val_2 + w3 * val_3);
    }

    public Vector3d interpolateVector(Vector3d location, Vector3d vec1, Vector3d vec2, Vector3d vec3){
        double w1 = (
                            (vertex2.getY() - vertex3.getY()) * (location.getX() - vertex3.getX()) +
                            (vertex3.getX() - vertex2.getX()) * (location.getY() - vertex3.getY())
                    ) /
                    (
                            (vertex2.getY() - vertex3.getY()) * (vertex1.getX() - vertex3.getX()) +
                            (vertex3.getX() - vertex2.getX()) * (vertex1.getY() - vertex3.getY())
                    );

        double w2 = (
                            (vertex3.getY() - vertex1.getY()) * (location.getX() - vertex3.getX()) +
                            (vertex1.getX() - vertex3.getX()) * (location.getY() - vertex3.getY())
                    ) /
                    (
                            (vertex2.getY() - vertex3.getY()) * (vertex1.getX() - vertex3.getX()) +
                            (vertex3.getX() - vertex2.getX()) * (vertex1.getY() - vertex3.getY())
                    );
        double w3 = 1-w2-w1;



        return new Vector3d(
                (w1 * vec1.getX() + w2 * vec2.getX() + w3 * vec3.getX()),
                (w1 * vec1.getY() + w2 * vec2.getY() + w3 * vec3.getY()),
                (w1 * vec1.getZ() + w2 * vec2.getZ() + w3 * vec3.getZ())
        );
    }

    @Override
    public Vector3d getNormal(Vector3d intersection) {
        if(normal1 == null){
            return normal;
        }else{

            Vector3d outNormal = interpolateVector(intersection, normal1,normal2, normal3);
            if(Double.isNaN(outNormal.getX() * outNormal.getY() * outNormal.getZ())){
                return normal1.add(normal2).self_add(normal3).self_scale(1/3d);
            }

            return interpolateVector(intersection, normal1,normal2, normal3);
        }
    }

    @Override
    public Vector3d getElementNormal() {
        return normal;
    }

    @Override
    public Vector3d min() {
        return min;
    }

    @Override
    public Vector3d max() {
        return max;
    }

    @Override
    public String toString() {
        return "Triangle{" +
               "vertex1=" + vertex1 +
               ", vertex2=" + vertex2 +
               ", vertex3=" + vertex3 +
               '}';
    }
}
