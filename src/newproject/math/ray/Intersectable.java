package newproject.math.ray;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Luecx on 18.08.2017.
 */
public interface Intersectable {

    public float intersectionValues(Ray ray);

    public Vector3f intersectionPoint(Ray ray);

    public boolean intersectable(Ray ray);

}
