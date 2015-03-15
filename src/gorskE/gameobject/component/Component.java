package gorskE.gameobject.component;

public interface Component {
		
	/**
	 * This is where the logic for the component is executed
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
}
