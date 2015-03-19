package gorskE;

import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import gorskE.IO.VBO;
import gorskE.physics.PhysicsEngine;

import java.nio.ByteBuffer;
 

import java.util.ArrayList;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class GorskE {
    private static final String initalGameScenePath = "res/scenes/main.scene";
	
	// We need to strongly reference callback instances.
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    // The window handle
    private long window;
    
    /**
     * The current physics engine that is running in another thread
     */
    private PhysicsEngine physicsEngine;
    
    /**
     * The current GameScene being played
     */
    private GameScene currentScene;
 
    /**
     * The array of all the currently drawing vertex-buffer-objects
     */
    private ArrayList<VBO> vbos;
    
    public void run() {
        System.out.println("Hello LWJGL " + Sys.getVersion() + "!");
        try {
            init();
            loop();
            end();
 
            // Release window and window callbacks
            glfwDestroyWindow(window);
            keyCallback.release();
        } finally {
            // Terminate GLFW and release the GLFWerrorfun
            glfwTerminate();
            errorCallback.release();
        }
    }
 
    /**
     * This is the function responsible initializing glfw and OpenGL
     */
    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
        
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( glfwInit() != GL11.GL_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");
 
        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
 
        int WIDTH = 300;
        int HEIGHT = 300;
 
        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");
 
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, GL_TRUE); // We will detect this in our rendering loop
            }
        });
 
        // Get the resolution of the primary monitor
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
            window,
            (GLFWvidmode.width(vidmode) - WIDTH) / 2,
            (GLFWvidmode.height(vidmode) - HEIGHT) / 2
        );
 
        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);
 
        // Make the window visible
        glfwShowWindow(window);
        
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the ContextCapabilities instance and makes the OpenGL
        // bindings available for use.
        GLContext.createFromCurrent();
    }
 
    /**
     * This is the main looping function responsible for running the game
     */
    private void loop() {
        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        
        //TODO load in GameScene
        //currentScene = LoadUtil.loadGameSceneFromPath(initalGameScenePath);
 
        //Start physics simulation
        physicsEngine = new PhysicsEngine(currentScene);
        new Thread(physicsEngine).start(); //starts the other thread
        
        //MAIN LOGIC IN HERE
        while ( glfwWindowShouldClose(window) == GL_FALSE ) {
        	
        	if(!physicsEngine.getInitialScene().equals(currentScene)){ //if the physics and render scene are out of sync
        		currentScene = physicsEngine.getInitialScene(); //set the render scene to the initial physics scene
        	}
        	
        	render();//renders the scene
        	
            // Poll for window events. The key callback above will only be invoked during this call.
            glfwPollEvents();
        }
    }
    
    /**
     * This function is called upon exiting of the engine
     */
    private void end(){
    	
    }
    
    /**
     * This is called each frame, and is responsible with rendering the scene
     */
    private void render(){
    	 glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    	 glfwSwapBuffers(window); // swap the color buffers
    	 
    	 for(VBO vbo : vbos) {
    		 //TODO render the stuff
    	 }
    	 
    	 
    }
}
