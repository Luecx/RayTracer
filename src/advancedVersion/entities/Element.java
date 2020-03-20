package advancedVersion.entities;

import advancedVersion.render.Ray;
import core.vector.Vector3d;

public abstract class Element {

    public abstract double   intersection(Ray ray);

    public abstract Vector3d getCenter();

    public abstract Vector3d getNormal(Vector3d intersection);

    public abstract Vector3d getElementNormal();

    public abstract void     recalculate(); //called if something moved

    public abstract Vector3d min();

    public abstract Vector3d max();

}
