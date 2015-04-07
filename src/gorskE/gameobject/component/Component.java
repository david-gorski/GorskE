package gorskE.gameobject.component;

import gorskE.gameobject.GameObject;

public interface Component{
		
	/**
	 * This method is called every frame of rendering
	 * This is where the majority of logic for the component is executed
	 */
	public void update();
	
	/**
	 * This function returns the title of the component
	 */
	public String getTitle();

	/**
	 * This function is called when the Component is going to be destroyed
	 */
	public void destroy();
	
	/**
	 * Used to retrieve the reference to the gameobject that contains this component
	 * @return the parent gameobject
	 */
	public GameObject getParent();
}
