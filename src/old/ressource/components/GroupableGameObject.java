package old.ressource.components;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

/**
 * Created by Luecx on 13.01.2017.
 */
public abstract class GroupableGameObject extends ComplexGameObject{

    private GroupableGameObject parent = null;
    protected ArrayList<GroupableGameObject> childs = new ArrayList<>();

    private Matrix4f absoluteTransformationMatrix = new Matrix4f();
    private Vector3f absolutePosition = new Vector3f();
    private boolean absoluteOutdated = true;

    protected abstract void absoluteDataChangedNotification();

    /**
     * Empty constructor. Every values are set to 0 except the scale which is set to 1.
     */
    public GroupableGameObject() {
        super();
    }

    /**
     *
     * @param position      -position
     * @param rotation      -rotation
     * @param scale         -scale
     */
    public GroupableGameObject(Vector3f position, Vector3f rotation, Vector3f scale) {
        super(position, rotation, scale);
    }

    /**
     *
     * @param position      -position
     * @param rotation      -rotation
     */
    public GroupableGameObject(Vector3f position, Vector3f rotation) {
        super(position, rotation);
    }

    /**
     *
     * @param position      -position
     */
    public GroupableGameObject(Vector3f position) {
        super(position);
    }

    /**
     *
     * @param x     -position_x
     * @param y     -position_y
     * @param z     -position_z
     */
    public GroupableGameObject(float x, float y, float z) {
        super(x, y, z);
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
    public GroupableGameObject(float x, float y, float z, float rx, float ry, float rz, float sx, float sy, float sz) {
        super(x, y, z, rx, ry, rz, sx, sy, sz);
    }

    /**
     * Returns the absolute transformation matrix.
     * !!! This method will not return a new matrix.
     * If this matrix is being changed, this object will not
     * get notified.
     * @return      -the absolute transformation matrix
     */
    public Matrix4f getAbsoluteTransformationMatrix() {
        if(this.absoluteOutdated){
            refreshAbsoluteTransformationMatrix();
        }
        return this.absoluteTransformationMatrix;
    }

    /**
     * Returns a copy of the absolute transformation matrix.
     * This method will return a new vector.
     * Any change on this matrix has no effect on the real transformation
     * of the object
     * @return      -the cloned absolute transformation matrix
     */
    public Matrix4f getClonedAbsoluteTransformationMatrix() {
        if(this.absoluteOutdated){
            refreshAbsoluteTransformationMatrix();
        }
        return new Matrix4f().load(this.absoluteTransformationMatrix);
    }

    /**
     * Returns the absolute position in vector form.
     * !!! This method will not return a new vector.
     * If this vector is being changed, this object will not
     * get notified.
     * @return      -the absolute position (vector form)
     */
    public Vector3f getAbsolutePosition() {
        if(this.absoluteOutdated) {
            refreshAbsoluteTransformationMatrix();
        }
        return absolutePosition;
    }

    /**
     * refreshes the absolute transformation matrix of this object
     * and of the parent and if needed of the parents parent...
     */
    private void refreshAbsoluteTransformationMatrix() {
        if(this.parent == null &&  this.absoluteOutdated){
            this.absoluteTransformationMatrix = this.getTransformationMatrix();
            this.absolutePosition = this.getPosition();
            this.absoluteOutdated = false;
        }else{
            if(this.parent.absoluteOutdated){
                this.parent.refreshAbsoluteTransformationMatrix();
            }
            Matrix4f.mul(this.parent.absoluteTransformationMatrix, this.getTransformationMatrix(), this.absoluteTransformationMatrix);
            this.absolutePosition.x = this.absoluteTransformationMatrix.m30;
            this.absolutePosition.y = this.absoluteTransformationMatrix.m31;
            this.absolutePosition.z = this.absoluteTransformationMatrix.m32;
            this.absoluteOutdated = false;

        }
    }

    /**
     * Sets this object to absolute outdated.
     * Next time when the absolute transformation matrix or absolute position
     * is asked, it forces this object to recalculate.
     * Furthermore every will also recalculate its data.
     */
    public void setAbsoluteOutdated() {
        this.absoluteOutdated = true;
        absoluteDataChangedNotification();
        for(GroupableGameObject ch:childs) {
            ch.setAbsoluteOutdated();
        }
    }

    /**
     * Returns true if this object has a parent.
     * The returned value is equal to (parent != null).
     *
     * @return      -indicates the existence of a parent
     */
    public boolean hasParent(){
        return(this.parent != null);
    }

    /**
     * It adds this object as a child to the object.
     * The object will become the parent of this child object.
     * The method will return false if the object is already the
     * child of this parent.
     * This method is equal to parent.addChild(child).
     *
     * @param parent    -the parent to be set
     * @return          -indicates if it worked
     */
    public boolean setParent(GroupableGameObject parent) {
        if(this.parent == parent || parent.childs.contains(this)) return false;
        this.parent = parent;
        this.parent.childs.add(this);
        this.absoluteOutdated = true;
        return true;
    }

    /**
     * Removes the parent from this object.
     * The method will return false if there is no parent
     * to be removed.
     * removeChild(child) is equal to child.removeParent()
     * @return      -indicates if it worked
     */
    public boolean removeParent(){
        if(this.parent == null){
            return false;
        }
        this.parent.childs.remove(this);
        this.parent = null;
        this.absoluteOutdated = true;
        return true;
    }

    /**
     * It adds the object as a child to this object.
     * This object will become the parent of the child object.
     * The method will return false if the object is already the
     * parent of this child.
     * This method is equal to child.setParent().
     *
     * @param child     -the child to be added
     * @return          -indicates if it worked
     */
    public boolean addChild(GroupableGameObject child){
        return child.setParent(this);
    }

    /**
     * Removes the child from this object.
     * The method will return false if there is no such child
     * to be removed.
     * removeChild(child) is equal to child.removeParent()
     *
     * @param child     -the child to be removed
     * @return          -indicates if it worked
     */
    public boolean removeChild(GroupableGameObject child) {
        return child.removeParent();
    }

    public void printStatus() {

    }

    @Override
    public String toString() {
        return "GroupableGameObject{" +
                "\n   parent=" + (parent == null ? "false":"true") +
                ",\n   absolutePosition=" + absolutePosition +
                ",\n   absoluteOutdated=" + absoluteOutdated +
                ",\n   outdated=" + this.isOutdated() +
                ",\n   position=" + this.getPosition() +
                ",\n   rotation=" + this.getRotation() +
                ",\n   scale=" + this.getScale() +
                '}';
    }

    /**
     * Prints the object and every child in the console.
     * It also prints every child of the childs until there is no child left
     */
    public void printTree() {
        this.printSubTree(this, 0);
    }

    /**
     * Print on object and its childs.
     * @param object    -the root object
     */
    private void printSubTree(GroupableGameObject object, int spacesLeft){
        printData(spacesLeft,object);
        for(GroupableGameObject child:object.childs){
            printSubTree(child, spacesLeft + 3);
        }
    }

    /**
     * Prints the name of the object in the console.
     * On the left side will be (spacesLeft) spaces
     * @param spacesLeft    -the amount of spaces
     */
    private void printData(int spacesLeft, GroupableGameObject object){
        for(int i = 0; i < spacesLeft; i++){
            System.out.print(" ");
        }
        System.out.println("-"+object.getClass().getName() + "   -@"+ object.hashCode());
    }

    /**
     * Called when any data like the relative position or rotation have been changed.
     * This method will set this object outdated as long as the absolute transformation matrix
     * will be refreshed.
     */
    @Override
    protected void dataChangedNotification() {
        this.setAbsoluteOutdated();
    }

    /**
     * Returns the X-Axis of this object. It should be normalised.
     * @return      the X-Axis
     */
    public synchronized Vector3f getXAxis() {
        if(this.absoluteOutdated) this.refreshAbsoluteTransformationMatrix();
        return new Vector3f(absoluteTransformationMatrix.m00, absoluteTransformationMatrix.m01, absoluteTransformationMatrix.m02);
    }

    /**
     * Returns the Y-Axis of this object. It should be normalised.
     * @return      the Y-Axis
     */
    public synchronized Vector3f getYAxis() {
        if(this.absoluteOutdated) this.refreshAbsoluteTransformationMatrix();
        return new Vector3f(absoluteTransformationMatrix.m10, absoluteTransformationMatrix.m11, absoluteTransformationMatrix.m12);
    }

    /**
     * Returns the Z-Axis of this object. It should be normalised.
     * @return      the Z-Axis
     */
    public synchronized Vector3f getZAxis() {
        if(this.absoluteOutdated) this.refreshAbsoluteTransformationMatrix();
        return new Vector3f(absoluteTransformationMatrix.m20, absoluteTransformationMatrix.m21, absoluteTransformationMatrix.m22);
    }
}
