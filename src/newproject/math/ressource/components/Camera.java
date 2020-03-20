package newproject.math.ressource.components;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Luec
 */
public abstract class Camera extends GroupableGameObject {

	protected Matrix4f projectionMatrix = new Matrix4f();
	protected boolean projectionOutdated = true;

	private Matrix4f viewMatrix = new Matrix4f();
	private boolean viewMatrixOutdated = true;


	/**
	 * Empty constructor. Every values are set to 0 except the scale which is set to 1.
	 */
	public Camera() {
		super();
	}

	/**
	 *
	 * @param position      -position
	 * @param rotation      -rotation
	 */
	public Camera(Vector3f position, Vector3f rotation) {
		super(position, rotation);
	}

	/**
	 *
	 * @param position      -position
	 */
	public Camera(Vector3f position) {
		super(position);
	}

	/**
	 *
	 * @param x     -position_x
	 * @param y     -position_y
	 * @param z     -position_z
	 */
	public Camera(float x, float y, float z) {
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
	 */
	public Camera(float x, float y, float z, float rx, float ry, float rz) {
		super(x, y, z, rx, ry, rz, 1,1,1);
	}

	protected abstract void refreshProjectionMatrix();

	/**
     * Returns the projection matrix
	 * @return
     */
	public Matrix4f getProjectionMatrix(){
		if(this.projectionOutdated){
			this.refreshProjectionMatrix();
		}
		return this.projectionMatrix;
	}

	/**
	 * Marks the projection matrix as outdated and forces a recalculation.
	 */
	protected void setProjectionOutdated(){
		this.projectionOutdated = true;
	}

	/**
	 * Eeturns true if the projection matrix is outdated.
	 * @return
	 */
	public boolean isProjectionOutdated() {
		return projectionOutdated;
	}

	/**
	 * Returns the view matrix.
	 * @return		-the view matrix
	 */
	public Matrix4f getViewMatrix() {
		if(this.viewMatrixOutdated){
			this.viewMatrix = (Matrix4f) getClonedAbsoluteTransformationMatrix().invert();
			this.viewMatrixOutdated = false;
		}
		return this.viewMatrix;
	}

	/**
	 * Eeturns true if the view matrix is outdated.
	 * @return
	 */
	public boolean isViewMatrixOutdated() {
		return viewMatrixOutdated;
	}

	/**
	 * Marks the view matrix as outdated and forces a recalculation.
	 */
	public void setViewMatrixOutdated() {
		this.viewMatrixOutdated = true;
	}

	/**
     * Notifies this object when the absolute data and values have changed.
	 */
	@Override
	protected void absoluteDataChangedNotification() {
		setViewMatrixOutdated();
	}

	/**
	 * moves the camera. control:
	 * W - forward
	 * S - backward
	 * D - right
	 * A - left
	 * UP - Look down
	 * DOWN - Look up
	 * RIGHT - Look right
	 * LEFT - Look left
	 */
	public void move() {

		float velo = 3f;

		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			Vector3f dir = this.getZAxis();
			dir.negate();
			dir.normalise();
			dir.x = dir.x * velo/8;
			dir.y = dir.y * velo/8;
			dir.z = dir.z * velo/8;
			this.increasePosition(dir);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			Vector3f dir = this.getZAxis();
			dir.normalise();
			dir.x = dir.x* velo / 8;
			dir.y = dir.y* velo / 8;
			dir.z = dir.z* velo / 8;
			this.increasePosition(dir);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			Vector3f dir = this.getXAxis();
			dir.normalise();
			dir.x = dir.x* velo / 8;
			dir.y = dir.y* velo / 8;
			dir.z = dir.z* velo / 8;
			this.increasePosition(dir);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			Vector3f dir = this.getXAxis();
			dir.normalise();
			dir.negate();
			dir.x = dir.x* velo / 8;
			dir.y = dir.y* velo / 8;
			dir.z = dir.z* velo / 8;
			this.increasePosition(dir);
		}
		
		//this.increaseRotation(new Vector3f(Mouse.getDY()/20f, -Mouse.getDX()/20f, 0));
		
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			this.increaseRotation(new Vector3f(1,0,0));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			this.increaseRotation(new Vector3f(-1,0,0));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			this.increaseRotation(new Vector3f(0,1,0));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			this.increaseRotation(new Vector3f(0,-1,0));
		}
	
	}

	/**
	 * Returns a lookat-viewmatrix with the cameras position.
	 * Note: connection-vector between camera position and target must not
	 * be equal with the up-vector
	 *
	 * @param target position beeing looked at
	 * @param up	 up-vector of the camera. Default should be (0,1,0)
	 * @return
	 */
	public Matrix4f lookAtViewMatrix(Vector3f target, Vector3f up){
		this.setOutdated();
		Vector3f position = this.getAbsolutePosition();
		Vector3f con = new Vector3f();
		Vector3f.sub(target, position, con);
		con.normalise();
		Vector3f cross = new Vector3f();
		Vector3f.cross(con, up, cross);
		cross.normalise();
		Vector3f u = new Vector3f();
		Vector3f.cross(cross, con, u);
		u.normalise();

		Matrix4f mat = new Matrix4f();

		mat.m00 = cross.x;
		mat.m10 = cross.y;
		mat.m20 = cross.z;

		mat.m01 = u.x;
		mat.m11 = u.y;
		mat.m21 = u.z;

		mat.m02 = -con.x;
		mat.m12 = -con.y;
		mat.m22 = -con.z;

		mat.m03 = 0.0f;
		mat.m13 = 0.0f;
		mat.m23 = 0.0f;

		mat.m30 = -Vector3f.dot(cross, position);
		mat.m31 = -Vector3f.dot(u, position);
		mat.m32 = Vector3f.dot(con, position);
		mat.m33 = 1.0f;
		return mat;
	}





}
