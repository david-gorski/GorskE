package gorskE.gameobject.component;

import gorskE.IO.VAO;
import gorskE.gameobject.GameObject;

public abstract class RenderComponent implements Component{
	
	private GameObject parent;
	
	private float x=0,y=0,z=0;

	
	public RenderComponent(GameObject parent, float x, float y, float z) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * This is the where each rendering component will draw its vaos and such
	 */
	public abstract void render();

	@Override
	public void update() {
		//I don't think it needs to update anything
	}

	@Override
	public abstract String getTitle();
	
	public GameObject getParent(){
		return parent;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
}
