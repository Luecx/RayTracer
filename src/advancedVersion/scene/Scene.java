package advancedVersion.scene;

import advancedVersion.entities.Triangle;
import core.vector.Vector3d;

import java.util.ArrayList;
import java.util.Vector;

public class Scene {

    private ArrayList<Triangle> triangles = new ArrayList<>();
    private ArrayList<Light> lights = new ArrayList<>();
    private Camera camera = new Camera(new Vector3d(), new Vector3d());

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }

    public void setTriangles(ArrayList<Triangle> triangles) {
        this.triangles = triangles;
    }

    public ArrayList<Light> getLights() {
        return lights;
    }

    public void setLights(ArrayList<Light> lights) {
        this.lights = lights;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
