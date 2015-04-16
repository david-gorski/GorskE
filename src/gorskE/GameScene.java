package gorskE;

import gorskE.camera.Camera;
import gorskE.camera.MoveableCamera;
import gorskE.component.Component;

import java.util.ArrayList;

public class GameScene{
	
	/** The array of all the objects in the scene **/
	private ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	
	/** The reference to the camera looking at the game scene currently **/
	public Camera camera;
	
	//Fundamental Constants of the scene 
	//ALT add more values as physics engine develops
	private float gravity = 9.81f;
	private float speedOfLight = 299792458;
	private float drag = 1.2f;
	
	public GameScene(Camera camera){
		this.camera = camera;
	}
	
	public void setCamera(Camera camera){
		this.camera = camera;
	}
	
	public float getGravity() {
		return gravity;
	}
	public void setGravity(float gravity) {
		this.gravity = gravity;
	}
	public float getSpeedOfLight() {
		return speedOfLight;
	}
	public void setSpeedOfLight(float speedOfLight) {
		this.speedOfLight = speedOfLight;
	}
	public float getDrag() {
		return drag;
	}
	public void setDrag(float drag) {
		this.drag = drag;
	}
	
	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}
	
	public void addGameObject(GameObject gameobject){
		if(!gameObjects.contains(gameobject)){
			gameObjects.add(gameobject);
		}
	}
	
	/**
	 * Used to remove a GameObject with a reference to that GameObject
	 * @param gameobject the refernce to the GameObject you want to remove
	 * @return true if that GameObject was successfully removed, false if this Scene didnt contain this GameObject
	 */
	public boolean removeGameObject(GameObject gameobject){
		gameobject.destroy();
		if(gameObjects.contains(gameobject)){
			gameObjects.remove(gameobject);
			return true;
		}else{
			return false;
		}
	}

	public void update() {
		for(GameObject go : gameObjects){
			for(Component comp : go.getComponents()){
				comp.update();
			}
		}
		camera.update();
	}
	
	public void render() {
		for(GameObject go : gameObjects){
			for(Component comp : go.getComponents()){
				if(GorskE.isImmediateMode) {
					comp.renderImmediate();
				}else {
					comp.renderVAO();
				}
			}
		}
		camera.render();
	}
	
	public void destroy() {
		for(GameObject go : gameObjects){
			go.destroy();
		}
	}

	public void physicsUpdate() {
		for(GameObject go : gameObjects){
			for(Component comp : go.getComponents()){
				comp.physicsUpdate();
			}
		}
	}

}
