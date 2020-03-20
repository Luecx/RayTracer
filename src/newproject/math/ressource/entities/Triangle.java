package newproject.math.ressource.entities;

import newproject.math.ray.Intersectable;
import newproject.math.ray.Ray;
import old.ressource.maths.Maths;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Luecx on 09.08.2017.
 */
public class Triangle implements Intersectable{

    public Vertex v1,v2,v3;
    public Material material;

    public Triangle(Vertex v1, Vertex v2, Vertex v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public float interpolateValue(Vector3f location, float val_1, float val_2, float val_3){
        old.ressource.maths.Ray r1 = new old.ressource.maths.Ray(v3.getPosition(), Vector3f.sub(v3.getPosition(),v2.getPosition(), null));
        old.ressource.maths.Ray r2 = new old.ressource.maths.Ray(v1.getPosition(), Vector3f.sub(v1.getPosition(),v3.getPosition(), null));
        old.ressource.maths.Ray r3 = new old.ressource.maths.Ray(v2.getPosition(), Vector3f.sub(v2.getPosition(),v1.getPosition(), null));
        float p1 = Maths.distanceRayPoint(r1, location) / Maths.distanceRayPoint(r1, v1.getPosition());
        float p2 = Maths.distanceRayPoint(r2, location) / Maths.distanceRayPoint(r2, v2.getPosition());
        float p3 = Maths.distanceRayPoint(r3, location) / Maths.distanceRayPoint(r3, v3.getPosition());
        return (p1 * val_1 + p2 * val_2 + p3 * val_3) / (p1 + p2 + p3);
    }

    public Vector2f interpolate(Vector3f location, Vector2f val_1, Vector2f val_2, Vector2f val_3) {
        return new Vector2f(
                interpolateValue(location, val_1.x, val_2.x, val_3.x),
                interpolateValue(location, val_1.y, val_2.y, val_3.y));
    }

    public Vector3f interpolate(Vector3f location, Vector3f val_1, Vector3f val_2, Vector3f val_3) {
        return new Vector3f(
                interpolateValue(location, val_1.x, val_2.x, val_3.x),
                interpolateValue(location, val_1.y, val_2.y, val_3.y),
                interpolateValue(location, val_1.z, val_2.z, val_3.z));
    }

    public Vector3f interpolateNormal(Vector3f location) {
        return interpolate(location, v1.getNormal(), v2.getNormal(), v3.getNormal());
    }

    private Vector3f interpolateTexture(Vector3f location, BufferedImage img) {
        Vector2f texCoords = interpolate(location, v1.getTextureCoords(), v2.getTextureCoords(), v3.getTextureCoords());
        Color c = new Color(img.getRGB((int)(texCoords.x * img.getWidth()), (int)(texCoords.y * img.getHeight())));
        return new Vector3f((float)c.getRed() / 256f, (float)c.getGreen()/ 256f, (float)c.getBlue()/ 256f);
    }

    public Vector3f interpolateTexture(Vector3f location) {
        if(material == null) return new Vector3f(0.8f,0.8f,0.8f);
        return interpolateTexture(location, material.getTexture_map().getBufferedImage());
    }

    public Vertex getV1() {
        return v1;
    }

    public void setV1(Vertex v1) {
        this.v1 = v1;
    }

    public Vertex getV2() {
        return v2;
    }

    public Vertex getV3() {
        return v3;
    }

    public void setV3(Vertex v3) {
        this.v3 = v3;
    }

    public void setV2(Vertex v2) {
        this.v2 = v2;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "v1=" + v1 +
                ", v2=" + v2 +
                ", v3=" + v3 +
                '}';
    }

    @Override
    public Vector3f intersectionPoint(Ray ray) {

        if(ray.getDirection().length() != 1) {
            ray.getDirection().normalise();
        }

        Vector3f e1 = new Vector3f(
                v2.getPosition().x - v1.getPosition().x,
                v2.getPosition().y - v1.getPosition().y,
                v2.getPosition().z - v1.getPosition().z);
        Vector3f e2 = new Vector3f(
                v3.getPosition().x - v1.getPosition().x,
                v3.getPosition().y - v1.getPosition().y,
                v3.getPosition().z - v1.getPosition().z);

        Vector3f pvec = Vector3f.cross(ray.getDirection(), e2,null);

        float det = Vector3f.dot(e1, pvec);

        if (det < 0.0001f && det > -0.0001f) {
            return null;
        }

        float inv_det = 1 / det;
        Vector3f tvec = new Vector3f(
                ray.getOrigin().x - v1.getPosition().x,
                ray.getOrigin().y - v1.getPosition().y,
                ray.getOrigin().z - v1.getPosition().z);
        float u = Vector3f.dot(tvec, pvec) * inv_det;
        if(u < 0 || u > 1) {
            return null;
        }

        Vector3f qvec = Vector3f.cross(tvec, e1,null);
        float v = Vector3f.dot(ray.getDirection(), qvec) * inv_det;
        if (v < 0 || u + v > 1) {
            return null;
        }
        float dist = Vector3f.dot(e2, qvec) * inv_det;
        if(dist <= 0.001) return null;
        return ray.calculatePosition(dist);
    }

    @Override
    public float intersectionValues(Ray ray) {
        return -1;
    }

    @Override
    public boolean intersectable(Ray ray) {
        return intersectionPoint(ray) != null;
    }
}
