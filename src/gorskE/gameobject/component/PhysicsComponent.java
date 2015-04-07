package gorskE.gameobject.component;

import gorskE.gameobject.GameObject;

public abstract class PhysicsComponent implements Component{
	
	GameObject parent;
	
	/**
	 * This method is called every physics step
	 * This is where the any physics related logic for the component is executed
	 */
	public abstract void physicUpdate();
	
	public PhysicsComponent(GameObject parent) {
		this.parent = parent;
	}

}
