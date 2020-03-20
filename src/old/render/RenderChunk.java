package old.render;

import old.ressource.entities.Triangle;

import java.util.ArrayList;

/**
 * Created by Luecx on 18.08.2017.
 */
public class RenderChunk {
    private ArrayList<Triangle> triangles;

    public RenderChunk(ArrayList<Triangle> triangles) {
        this.triangles = triangles;
    }

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }

    public void setTriangles(ArrayList<Triangle> triangles) {
        this.triangles = triangles;
    }
}

