package newproject.math.ressource.components;

import org.lwjgl.util.vector.Vector3f;

public class Group extends GroupableGameObject {

	/**
	 * Empty constructor. Every values are set to 0 except the scale which is set to 1.
	 */
	public Group() {
		super();
	}

	/**
	 *
	 * @param position      -position
	 * @param rotation      -rotation
	 * @param scale         -scale
	 */
	public Group(Vector3f position, Vector3f rotation, Vector3f scale) {
		super(position, rotation, scale);
	}

	/**
	 *
	 * @param position      -position
	 * @param rotation      -rotation
	 */
	public Group(Vector3f position, Vector3f rotation) {
		super(position, rotation);
	}

	/**
	 *
	 * @param position      -position
	 */
	public Group(Vector3f position) {
		super(position);
	}

	/**
	 *
	 * @param x     -position_x
	 * @param y     -position_y
	 * @param z     -position_z
	 */
	public Group(float x, float y, float z) {
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
	public Group(float x, float y, float z, float rx, float ry, float rz, float sx, float sy, float sz) {
		super(x, y, z, rx, ry, rz, sx, sy, sz);
	}

	@Override
	protected void absoluteDataChangedNotification() {

	}
}
