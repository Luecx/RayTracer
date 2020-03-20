package newproject.math.ressource.components;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.ByteBuffer;

public class PerspectiveCamera extends Camera {

	private float FOV = 75;
	private float NEAR_PLANE = 0.1f;
	private float FAR_PLANE = 50000f;

	
	public PerspectiveCamera() {
		super();
		// TODO Auto-generated constructor stub
	}

	public float getFOV() {
		return FOV;
	}

	public float getNEAR_PLANE() {
		return NEAR_PLANE;
	}

	public float getFAR_PLANE() {
		return FAR_PLANE;
	}

	public void setFOV(float fOV) {
		FOV = fOV;
		this.setProjectionOutdated();
	}

	public void setNEAR_PLANE(float nEAR_PLANE) {
		NEAR_PLANE = nEAR_PLANE;
		this.setProjectionOutdated();
	}

	public void setFAR_PLANE(float fAR_PLANE) {
		FAR_PLANE = fAR_PLANE;
		this.setProjectionOutdated();
	}

	public PerspectiveCamera(float x, float y, float z, float rx, float ry, float rz) {
		super(x, y, z, rx, ry, rz);
		// TODO Auto-generated constructor stub
	}

	public PerspectiveCamera(float x, float y, float z) {
		super(x, y, z);
		// TODO Auto-generated constructor stub
	}

	public PerspectiveCamera(Vector3f position, Vector3f rotation) {
		super(position, rotation);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void refreshProjectionMatrix() {
		projectionMatrix = new Matrix4f();
		float aspectRatio = (float) Display.getDisplayMode().getWidth() / (float) Display.getDisplayMode().getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;		
	}

	public void move() {

		if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
			this.setFOV(this.getFOV()+0.1f);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E)){
			this.setFOV(this.getFOV()- 0.1f);
		}
		super.move();
	}

	public float calculateDistance(int x, int y) {

		ByteBuffer b = BufferUtils.createByteBuffer(4);
		GL11.glReadPixels(x, y, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, b);
		float z_b = b.getFloat();

		float dist = (2 * FAR_PLANE * NEAR_PLANE) / (FAR_PLANE + NEAR_PLANE - (FAR_PLANE - NEAR_PLANE) * (2 * z_b - 1));
		return dist;

	}

}
