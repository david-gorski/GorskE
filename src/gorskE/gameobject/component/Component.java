package gorskE.gameobject.component;

import gorskE.gameobject.GameObject;

public abstract class Component{
	
	private String title = "Component"; 
	
	private float x=0,y=0,z=0;

	private GameObject parent;
	
	public Component(String title, GameObject parent, float x, float y, float z) {
		this.title = title;
		this.parent = parent;
		this.x =x;
		this.y =y;
		this.z =z;
	}
		
	/**
	 * This method is called every frame of rendering
	 * This is where the majority of logic for the component is executed
	 */
	public void update() {
		
	}
	
	/**
	 * This method is called when the frame is ready to be rendered
	 * This is where the all the render logic should go
	 */
	public void renderImmediate() {
		
	}
	
	/**
	 * This method is called when the frame is ready to be rendered
	 * This is where the all the render logic should go
	 * This is what
	 */
	public void renderVAO() {
		
	}
	
	/**
	 * This method is called every physics step
	 * This is where the all physics related logic should take place,
	 * Or anything that you dont necessarily need to update every frame
	 */
	public void physicsUpdate() {
		
	}
	
	/**
	 * This function returns the title of the component
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * This function is called when the Component is going to be destroyed
	 */
	public void destroy() {
		//nothing by default
	}
	
	/**
	 * Used to retrieve the reference to the gameobject that contains this component
	 * @return the parent gameobject
	 */
	public GameObject getParent() {
		return parent;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}
}
