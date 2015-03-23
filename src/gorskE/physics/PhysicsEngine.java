package gorskE.physics;

import gorskE.GameScene;

public class PhysicsEngine implements Runnable{
    
    /**
     * The GameScene that is currently being simulated
     */
    private GameScene currentScene;

    /** This is only turned false when the physics engine needs to stop **/
    private boolean running = true;
    
    /**
     * This will be true after a full step through time in the physics has been completed
     */
    private boolean finishedStep = false;
    
    public PhysicsEngine(GameScene initialScene){
    	this.currentScene = initialScene;
    }
    
    public GameScene getCurrentScene(){
    	return currentScene;
    }
    
    public boolean isDoneWithStep(){
    	if(finishedStep){
    		finishedStep = false;
    		return true;
    	}
    	return false;
    }
    
    public void stopPhysicsEngine(){
    	running = false;
    }

	/**
	 * Starts the physics engine thread
	 * Also contains the loop for the main logic of the PhysicsEngine
	 */
	public void run() {
		while(running){
			
			
			//IMP physcis stuff
			
			finishedStep = true;
		}
	}
    
    
    
}
