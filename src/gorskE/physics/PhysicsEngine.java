package gorskE.physics;

import gorskE.GameScene;

public class PhysicsEngine implements Runnable{
	
    /**
     * The GameScene before the physics step
     */
    private GameScene initialScene;
    
    /**
     * The GameScene that is currently being simulated
     */
    private GameScene currentScene;

    /** This is only turned false when the physics engine needs to stop **/
    private boolean running = true;
    
    public PhysicsEngine(GameScene initialScene){
    	this.initialScene = initialScene;
    }
    
    public GameScene getInitialScene(){
    	return initialScene;
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
			currentScene = initialScene.clone(); //creates a clone of the initial scene to do the physics step on
			
			//IMP physcis stuff
			
			initialScene = currentScene; //after the physics step is done the old scene is replaced with the updated one 
			//ALT maybe include a maunal garbage collection call onto the old scene?
		}
	}
    
    
    
}
