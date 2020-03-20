package newproject.math.ray;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Luecx on 18.08.2017.
 */
public interface ComplexIntersectable extends Intersectable {

    public boolean pointInside(Vector3f p);

}
