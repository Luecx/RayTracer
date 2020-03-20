package newproject.math.ressource.components;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Luecx on 13.01.2017.
 */
public abstract class ComplexGameObject {


    private Vector3f position = new Vector3f();
    private Vector3f rotation = new Vector3f();
    private Vector3f scale = new Vector3f(1,1,1);

    private boolean outdated = true;
    private Matrix4f transformationMatrix = new Matrix4f();

    /**
     * Empty constructor. Every values are set to 0 except the scale which is set to 1.
     */
    public ComplexGameObject() {
    }

    /**
     *
     * @param position      -position
     * @param rotation      -rotation
     * @param scale         -scale
     */
    public ComplexGameObject(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    /**
     *
     * @param position      -position
     * @param rotation      -rotation
     */
    public ComplexGameObject(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    /**
     *
     * @param position      -position
     */
    public ComplexGameObject(Vector3f position) {
        this.position = position;
    }

    /**
     *
     * @param x     -position_x
     * @param y     -position_y
     * @param z     -position_z
     */
    public ComplexGameObject(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    /**
     *
     * @param x     -position_x
     * @param y     -position_y
     * @param z     -position_z
     * @param rx    -rotation_x
     * @param ry    -rotation_y
     * @param rz    -rotation_z
     * @param sx    -scale_x
     * @param sy    -scale_y
     * @param sz    -scale_z
     */
    public ComplexGameObject(float x, float y, float z, float rx, float ry, float rz, float sx, float sy, float sz) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
        this.rotation.x = rx;
        this.rotation.y = ry;
        this.rotation.z = rz;
        this.scale.x = sx;
        this.scale.y = sy;
        this.scale.z = sz;
    }

    /**
     * Forces the object to refresh next time when asked for the transformation matrix
     */
    public void setOutdated() {
        this.outdated = true;
        dataChangedNotification();
    }

    /**
     * beeing called when any attributes of this object have changed
     */
    protected abstract void dataChangedNotification();

    /**
     * Returns the position in vector form.
     * !!! The position is not cloned. The transformation matrix will NOT
     * be updated if this vector is changed unless setOutdated() is being called.
     * @return      -position in vector form
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Sets the position to a specific vector.
     * @param position     -position in vector form
     */
    public void setPosition(Vector3f position) {
        this.position = position;
        setOutdated();
    }

    /**
     * Sets the position to specific values
     * @param x     -position_x
     * @param y     -position_y
     * @param z     -position_z
     */
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
        setOutdated();
    }

    /**
     * Returns the rotation in vector form.
     * !!! The rotation is not cloned. The transformation matrix will NOT
     * be updated if this vector is changed unless setOutdated() is being called.
     * @return      -rotation in vector form
     */
    public Vector3f getRotation() {
        return rotation;
    }

    /**
     * Sets the rotation to a specific vector.
     * @param rotation     -rotation in vector form
     */
    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
        setOutdated();
    }

    /**
     * Sets the rotation to specific values
     * @param x     -rotation_x
     * @param y     -rotation_y
     * @param z     -rotation_z
     */
    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
        setOutdated();
    }

    /**
     * Returns the scale in vector form.
     * !!! The scale is not cloned. The transformation matrix will NOT
     * be updated if this vector is changed unless setOutdated() is being called.
     * @return      -scale in vector form
     */
    public Vector3f getScale() {
        return scale;
    }

    /**
     * Sets the scale to a specific vector.
     * @param scalation     -scale in vector form
     */
    public void setScale(Vector3f scalation) {
        this.scale = scalation;
        setOutdated();
    }

    /**
     * Sets the scale to specific values
     * @param x     -scale_x
     * @param y     -scale_y
     * @param z     -scale_z
     */
    public void setScale(float x, float y, float z) {
        this.scale.x = x;
        this.scale.y = y;
        this.scale.z = z;
        setOutdated();
    }

    /**
     * Increases the position of the object.
     * @param a     oldPosition = oldPosition + a
     */
    public void increasePosition(Vector3f a) {
        this.position.x += a.x;
        this.position.y += a.y;
        this.position.z += a.z;
        setOutdated();
    }

    /**
     * Increases the rotation of the object.
     * @param a     oldRotation = oldRotation + a
     */
    public void increaseRotation(Vector3f a) {
        this.rotation.x += a.x;
        this.rotation.y += a.y;
        this.rotation.z += a.z;
        setOutdated();
    }

    /**
     * Increases the scale of the object.
     * @param a     oldScale = oldScale + a
     */
    public void increaseScale(Vector3f a) {
        this.scale.x += a.x;
        this.scale.y += a.y;
        this.scale.z += a.z;
        setOutdated();
    }

    /**
     * Increases the position of the object.
     * @param x   oldPosition.x = oldPosition.x + x
     * @param y   oldPosition.y = oldPosition.y + y
     * @param z   oldPosition.z = oldPosition.z + z
     */
    public void increasePosition(float x, float y, float z) {
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
        setOutdated();
    }

    /**
     * Increases the rotation of the object.
     * @param x   oldRotation.x = oldRotation.x + x
     * @param y   oldRotation.y = oldRotation.y + y
     * @param z   oldRotation.z = oldRotation.z + z
     */
    public void increaseRotation(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
        setOutdated();
    }

    /**
     * Increases the scale of the object.
     * @param x   oldScale.x = oldScale.x + x
     * @param y   oldScale.y = oldScale.y + y
     * @param z   oldScale.z = oldScale.z + z
     */
    public void increaseScale(float x, float y, float z) {
        this.scale.x += x;
        this.scale.y += y;
        this.scale.z += z;
        setOutdated();
    }

    /**
     * Returns the current transformation matrix. The matrix is updated if any values have been changed
     * or setOutdated() has been called.
     * @return      -the transformation matrix
     */
    public Matrix4f getTransformationMatrix() {
        if (outdated) {
            outdated = false;
            this.transformationMatrix = this.createTransformationMatrix();
        }
        return this.transformationMatrix;
    }

    /**
     * Returns a cloned version of the current transformation matrix. The matrix is updated if any values have been changed
     * or setOutdated() has been called.
     * @return      -the cloned transformation matrix
     */
    public Matrix4f getClonedTransformationMatrix() {
        if (outdated) {
            outdated = false;
            this.transformationMatrix = this.createTransformationMatrix();
        }
        return new Matrix4f().load(this.transformationMatrix);
    }

    /**
     * Creates the transformation matrix by using the position, rotation and scale values.
     *
     * @return      -the transformation matrix
     */
    private Matrix4f createTransformationMatrix() {
        Matrix4f m = new Matrix4f();
        m.setIdentity();

        Matrix4f.translate(this.position, m, m);

        Matrix4f.rotate((float) Math.toRadians((this.rotation.x)), new Vector3f(1, 0, 0), m, m);
        Matrix4f.rotate((float) Math.toRadians((this.rotation.y)), new Vector3f(0, 1, 0), m, m);
        Matrix4f.rotate((float) Math.toRadians((this.rotation.z)), new Vector3f(0, 0, 1), m, m);

        Matrix4f.scale(this.scale, m, m);
        return m;
    }

    /**
     * Rotates the object around a specific axis.
     * It rotates according to the right hand rotation principle.
     *
     * @param axis      -the rotation axis
     * @param value     -the degrees to rotate
     */
    public void increaseRotation(Vector3f axis, float value) {
        try {

            axis.normalise();
            Matrix4f neu = new Matrix4f();
            Matrix4f rot = new Matrix4f();
            rot.setIdentity();
            rot.rotate((float) Math.toRadians(value), axis);
            Matrix4f.mul(rot, this.createTransformationMatrix(), neu);
            Vector3f newRot = matrixToAngles(neu);
            this.setRotation(newRot);
        } catch (Exception e) {
            System.err.println(this.createTransformationMatrix());
        }
        outdated = true;
    }

    /**
     * returns the rotation of the object by giving its transformation matrix
     * @param m     -the transformation matrix of the object
     * @return      -the rotation as a vector
     */
    public static Vector3f matrixToAngles(Matrix4f m) {

        double cN1 = 0;
        double aN1 = 0;
        double bN1 = 0;

        if (m.m02 != 1 && m.m02 != -1) {
            bN1 = -Math.asin(m.m02);
            aN1 = Math.atan2(m.m12 / Math.cos(bN1), m.m22 / Math.cos(bN1));
            cN1 = Math.atan2(m.m01 / Math.cos(bN1), m.m00 / Math.cos(bN1));
        } else {

            if (m.m02 == -1) {
                bN1 = Math.PI / 2;
                aN1 = cN1 + Math.atan2(m.m10, m.m20);
            } else {
                bN1 = -Math.PI / 2;
                aN1 = -cN1 + Math.atan2(-m.m10, -m.m20);
            }
        }
        Vector3f p1 = new Vector3f((float) Math.toDegrees(aN1), (float) Math.toDegrees(bN1),
                (float) Math.toDegrees(cN1));
        return p1;
    }

    /**
     * returns the position of the object by giving its transformation matrix
     * @param m     -the transformation matrix of the object
     * @return      -the position as a vector
     */
    public static Vector3f matrixToPosition(Matrix4f m) {
        Vector3f v = new Vector3f();
        v.x = m.m30;
        v.y = m.m31;
        v.z = m.m32;
        return v;
    }

    @Override
    public String toString() {
        return "ComplexGameObject{" +
                "\n   position=" + position +
                ",\n   rotation=" + rotation +
                ",\n   scale=" + scale +
                ",\n   outdated=" + outdated +
                ",\n   transformationMatrix=\n" + transformationMatrix +
                '}';
    }

    /**
     * Returns true if this objects values or its transformation is outdated.
     * @return      -outdated ?
     */
    public boolean isOutdated() {
        return this.outdated;
    }

}
