package old.ressource.entities;

import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import old.ressource.maths.Maths;
import old.ressource.maths.Ray;

/**
 * Created by Luecx on 09.08.2017.
 */
public class Triangle {

    private Vertex v1,v2,v3;
    private Texture texture;

    public Triangle(Vertex v1, Vertex v2, Vertex v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    private float interpolateValue(Vector3f location, float val_1, float val_2, float val_3){
        Ray r1 = new Ray(v3.getPosition(), Vector3f.sub(v3.getPosition(),v2.getPosition(), null));
        Ray r2 = new Ray(v1.getPosition(), Vector3f.sub(v1.getPosition(),v3.getPosition(), null));
        Ray r3 = new Ray(v2.getPosition(), Vector3f.sub(v2.getPosition(),v1.getPosition(), null));
        float p1 = Maths.distanceRayPoint(r1, location) / Maths.distanceRayPoint(r1, v1.getPosition());
        float p2 = Maths.distanceRayPoint(r2, location) / Maths.distanceRayPoint(r2, v2.getPosition());
        float p3 = Maths.distanceRayPoint(r3, location) / Maths.distanceRayPoint(r3, v3.getPosition());
        return (p1 * val_1 + p2 * val_2 + p3 * val_3) / (p1 + p2 + p3);
    }

    public Vector interpolate(Vector3f loc, int bufferIndex) {
        if(v1.getData(bufferIndex) instanceof Vector3f) {
            return new Vector3f(
                    interpolateValue(loc, ((Vector3f) v1.getData(bufferIndex)).getX(), ((Vector3f) v2.getData(bufferIndex)).getX(), ((Vector3f)v3.getData(bufferIndex)).getX()),
                    interpolateValue(loc, ((Vector3f) v1.getData(bufferIndex)).getY(), ((Vector3f) v2.getData(bufferIndex)).getY(), ((Vector3f)v3.getData(bufferIndex)).getY()),
                    interpolateValue(loc, ((Vector3f) v1.getData(bufferIndex)).getZ(), ((Vector3f) v2.getData(bufferIndex)).getZ(), ((Vector3f)v3.getData(bufferIndex)).getZ()));
        }else if(v1.getData(bufferIndex) instanceof Vector2f){
            return new Vector2f(
                    interpolateValue(loc, ((Vector3f) v1.getData(bufferIndex)).getX(), ((Vector3f) v2.getData(bufferIndex)).getX(), ((Vector3f)v3.getData(bufferIndex)).getX()),
                    interpolateValue(loc, ((Vector3f) v1.getData(bufferIndex)).getY(), ((Vector3f) v2.getData(bufferIndex)).getY(), ((Vector3f)v3.getData(bufferIndex)).getY()));
        }
        return null;
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

    public void setV2(Vertex v2) {
        this.v2 = v2;
    }

    public Vertex getV3() {
        return v3;
    }

    public void setV3(Vertex v3) {
        this.v3 = v3;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }


    boolean processed;
    public boolean isProcessed() {
        return processed;
    }
    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
