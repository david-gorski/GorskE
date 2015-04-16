package gorskE;

import gorskE.component.Component;

import java.util.ArrayList;

public class GameObject{
	
	/**
	 * The position of the GameObject
	 */
	private float x, y, z;
	
	/** The array of all the components inside this gameobject **/
	private ArrayList<Component> components = new ArrayList<Component>();
	
	public GameObject(){
		x=0;
		y=0;
		z=0;
		GorskE.engine.currentScene.addGameObject(this);
	}
	
	public GameObject(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
		GorskE.engine.currentScene.addGameObject(this);
	}
	
	public GameObject(float x, float y, float z, ArrayList<Component> components){
		this.x = x;
		this.y = y;
		this.z = z;
		this.components = components;
		GorskE.engine.currentScene.addGameObject(this);
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
	
	/**
	 * Called when ending the life-span of a GameObject
	 * Runs through each component calls their destory method
	 */
	public void destroy(){
		for(Component component : components){
			component.destroy();
		}
	}

	public ArrayList<Component> getComponents() {
		return components;
	}
	
	public void addComponents(Component component){
		components.add(component);
	}
	
	/**
	 * Used to remove a component with a reference to that component
	 * @param component the refernce to the component you want to remove
	 * @return true if that component was successfully removed, false if this GameObject didnt contain this component
	 */
	public boolean removeComponent(Component component){
		if(components.contains(component)){
			component.destroy();
			components.remove(component);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Used to remove *ALL* components with a specific component title
	 * @param component the title of the component you want to remove
	 * @return true if that component was successfully removed, false if this GameObject didnt contain this component
	 */
	public boolean removeComponent(String component){
		ArrayList<Integer> indexsToRemove = new ArrayList<Integer>();
		boolean answer = false; //initially no instances found
		for(int i=0; i<components.size(); i++){
			Component temp = components.get(i);
			if(temp.getTitle().equals(component)){
				temp.destroy();
				indexsToRemove.add(i);
			}
		}
		for(int i=0; i<indexsToRemove.size(); i++){
			components.remove(indexsToRemove.get(i));
		}
		return answer;
	}

}
