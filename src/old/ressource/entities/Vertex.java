package old.ressource.entities;


import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Luecx on 09.08.2017.
 */
public class Vertex {
    private Vector[] buffer = new Vector[16];

    public Vertex(Vector3f position, Vector2f tex, Vector3f normal) {
        buffer[0] = position;
        buffer[1] = tex;
        buffer[2] = normal;
    }

    public Vector3f getPosition() {return (Vector3f)buffer[0];}

    public Vector3f getTexCoords() {return (Vector3f)buffer[1];}

    public Vector3f getNormal() {return (Vector3f)buffer[2];}

    public void setData(int index, Vector vec) {
        if(index > 0 && index < 16)
            this.buffer[index] = vec;
    }

    public Vector getData(int index) {
        if(index > 0 && index < 16)
            return buffer[index];
        return null;
    }
}
