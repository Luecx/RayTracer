package newproject.math.ressource.entities;

import org.lwjgl.util.vector.Vector3f;

import javax.xml.ws.soap.MTOM;

/**
 * Created by finne on 25.08.2017.
 */
public class Material {

    public Texture texture_map;
    public Texture normal_map;

    public float reflectivity = 0.1f;
    public float refractivity = 0.2f;

    public Material(Texture texture_map, Texture normal_map) {
        this.texture_map = texture_map;
        this.normal_map = normal_map;
    }

    public Material(Texture texture_map) {
        this.texture_map = texture_map;
    }

    public Texture getTexture_map() {
        return texture_map;
    }

    public void setTexture_map(Texture texture_map) {
        this.texture_map = texture_map;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public float getRefractivity() {
        return refractivity;
    }

    public void setRefractivity(float refractivity) {
        this.refractivity = refractivity;
    }

    public Vector3f getMixValues() {
        float t = reflectivity + refractivity;
        return new Vector3f(reflectivity / Math.max(t,1), refractivity / Math.max(t,1), 1- Math.min(t, 1));
    }

    public Texture getNormal_map() {
        return normal_map;
    }

    public void setNormal_map(Texture normal_map) {
        this.normal_map = normal_map;
    }
}
