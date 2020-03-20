package old.ressource.components;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class OrthographicCamera extends Camera{

	private float LEFT = -10,RIGHT = 10,TOP = 10,BOTTOM = -10,NEAR = 10f,FAR = 1000f;

	public OrthographicCamera() {

	}
	public OrthographicCamera(float LEFT, float RIGHT, float TOP, float BOTTOM, float NEAR, float FAR) {
		this.LEFT = LEFT;
		this.RIGHT = RIGHT;
		this.TOP = TOP;
		this.BOTTOM = BOTTOM;
		this.NEAR = NEAR;
		this.FAR = FAR;
	}

	public OrthographicCamera(Vector3f position) {
		super(position);
	}

	public OrthographicCamera(float x, float y, float z) {
		super(x, y, z);
	}

	public OrthographicCamera(Vector3f position, Vector3f rotation) {
		super(position, rotation);
	}

	public OrthographicCamera(float x, float y, float z, float rx, float ry, float rz, float LEFT, float RIGHT, float TOP, float BOTTOM, float NEAR, float FAR) {
		super(x, y, z, rx, ry, rz);
		this.LEFT = LEFT;
		this.RIGHT = RIGHT;
		this.TOP = TOP;
		this.BOTTOM = BOTTOM;
		this.NEAR = NEAR;
		this.FAR = FAR;
	}

	public OrthographicCamera(float x, float y, float z, float LEFT, float RIGHT, float TOP, float BOTTOM, float NEAR, float FAR) {
		super(x, y, z);
		this.LEFT = LEFT;
		this.RIGHT = RIGHT;
		this.TOP = TOP;
		this.BOTTOM = BOTTOM;
		this.NEAR = NEAR;
		this.FAR = FAR;
	}

	public OrthographicCamera(Vector3f position, Vector3f rotation, float LEFT, float RIGHT, float TOP, float BOTTOM, float NEAR, float FAR) {
		super(position, rotation);
		this.LEFT = LEFT;
		this.RIGHT = RIGHT;
		this.TOP = TOP;
		this.BOTTOM = BOTTOM;
		this.NEAR = NEAR;
		this.FAR = FAR;
	}

	public float getLEFT() {
		return LEFT;
	}

	public void setLEFT(float LEFT) {
		this.LEFT = LEFT;
		this.setProjectionOutdated();
	}

	public float getRIGHT() {
		return RIGHT;
	}

	public void setRIGHT(float RIGHT) {
		this.RIGHT = RIGHT;
		this.setProjectionOutdated();
	}

	public float getTOP() {
		return TOP;
	}

	public void setTOP(float TOP) {
		this.TOP = TOP;
		this.setProjectionOutdated();
	}

	public float getBOTTOM() {
		return BOTTOM;
	}

	public void setBOTTOM(float BOTTOM) {
		this.BOTTOM = BOTTOM;
		this.setProjectionOutdated();
	}

	public float getNEAR() {
		return NEAR;
	}

	public void setNEAR(float NEAR) {
		this.NEAR = NEAR;
		this.setProjectionOutdated();
	}

	public float getFAR() {
		return FAR;
	}

	public void setFAR(float FAR) {
		this.FAR = FAR;
		this.setProjectionOutdated();
	}

	@Override
	protected void refreshProjectionMatrix() {
		Matrix4f m = new Matrix4f();
		m.m00 = 2.0f / (LEFT-RIGHT);
		m.m11 = 2.0f / (BOTTOM-TOP);
		m.m22 = 2.0f / (NEAR-FAR);
		m.m30 = -(RIGHT + LEFT) / (RIGHT - LEFT);
		m.m31 = -(TOP + BOTTOM) / (TOP - BOTTOM);
		m.m32 = -(FAR + NEAR) / (FAR - NEAR);
		m.m33 = 1;
		this.projectionMatrix = m;
	}

}
