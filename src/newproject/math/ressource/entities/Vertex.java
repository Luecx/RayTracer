package newproject.math.ressource.entities;


import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Luecx on 09.08.2017.
 */
public class Vertex {

    private Vector3f position;
    private Vector2f textureCoords;
    private Vector3f normal;
    private Vector3f tangent;
    private Vector3f bitangent;

    public Vertex(Vector3f position, Vector2f textureCoords, Vector3f normal, Vector3f tangent, Vector3f bitangent) {
        this.position = position;
        this.textureCoords = textureCoords;
        this.normal = normal;
        this.tangent = tangent;
        this.bitangent = bitangent;
    }

    public Vertex(Vector3f position, Vector2f textureCoords, Vector3f normal) {
        this.position = position;
        this.textureCoords = textureCoords;
        this.normal = normal;
    }

    public boolean supportsNormalMapping() {
        return tangent != null && bitangent != null;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector2f getTextureCoords() {
        return textureCoords;
    }

    public void setTextureCoords(Vector2f textureCoords) {
        this.textureCoords = textureCoords;
    }

    public Vector3f getNormal() {
        return normal;
    }

    public void setNormal(Vector3f normal) {
        this.normal = normal;
    }

    public Vector3f getTangent() {
        return tangent;
    }

    public void setTangent(Vector3f tangent) {
        this.tangent = tangent;
    }

    public Vector3f getBitangent() {
        return bitangent;
    }

    public void setBitangent(Vector3f bitangent) {
        this.bitangent = bitangent;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "position=" + position +
                ", textureCoords=" + textureCoords +
                ", normal=" + normal +
                ", tangent=" + tangent +
                ", bitangent=" + bitangent +
                '}';
    }
}
