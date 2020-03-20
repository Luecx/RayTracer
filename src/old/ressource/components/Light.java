package old.ressource.components;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.lwjgl.util.vector.Vector3f;


public class Light extends GroupableGameObject{
    
	private Vector3f color = new Vector3f(1,1,1);
	private Vector3f attenuation = new Vector3f(1,0,0);
	
    public Light(Vector3f position, Vector3f color) {
        super(position, new Vector3f(0,0,0), new Vector3f(1,1,1));
        this.color = color;
    }
    
    public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
        super(position, new Vector3f(0,0,0), new Vector3f(1,1,1));
        this.color = color;
        this.attenuation = attenuation;
    }
    
    public Light(float x, float y, float z){
    	super(x,y,z);
    }

    /**
     * Returns the Attenuation of the light.
     * The attenuation is a intensity fallof.
     * The light intensity will be calculated like this:
     * f(distance) = 1 / (att.x + att.y * distance + att.z * distance * distance).
     * att.x has the biggest effect on small distances.
     * However att.z has the biggest effect on large distances.
     * The default attenuation is (1,0,0). This means no fallof at all.
     *
     * @return      -attenuation (vector form)
     */
    public Vector3f getAttenuation() {
		return attenuation;
	}

    /**
     * Sets the Attenuation of the light.
     * The attenuation is a intensity fallof.
     * The light intensity will be calculated like this:
     * f(distance) = 1 / (att.x + att.y * distance + att.z * distance * distance).
     * att.x has the biggest effect on small distances.
     * However att.z has the biggest effect on large distances.
     * The default attenuation is (1,0,0). This means no fallof at all.
     *
     */
	public void setAttenuation(Vector3f attenuation) {
		this.attenuation = attenuation;
	}

    /**
     * Returns the color of the light.
     * The light values is giving as rgba values clamped between 1 and 0.
     * (1,1,1) is total white and e.g. (1,0,0) would be red.
     * @return      -the color
     */
	public Vector3f getColor() {
        return color;
    }

    /**
     * Sets the color of the light.
     * The light values is giving as rgba values clamped between 1 and 0.
     * (1,1,1) is total white and e.g. (1,0,0) would be red.
     */
    public void setColor(Vector3f color) {
        this.color = color;
    }

    @Override
    protected void absoluteDataChangedNotification() {

    }
}
