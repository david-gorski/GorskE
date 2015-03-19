package gorskE.gameobject.component;

public interface Component extends Cloneable{
		
	/**
	 * This method is called every frame of rendering
	 * This is where the majority of logic for the component is executed
	 */
	public void update();
	
	/**
	 * This method is called every physics step
	 * This is where the any physics related logic for the component is executed
	 */
	public void physicUpdate();
	
	/**
	 * This function returns the title of the component
	 */
	public String getTitle();

	/**
	 * This function is called when the Component is going to be destroyed
	 */
	public void destroy();

	public Component clone();
}
