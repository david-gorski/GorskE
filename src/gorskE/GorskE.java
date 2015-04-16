package gorskE;

import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import gorskE.component.RenderStatic2DColorOnly;
import gorskE.physics.PhysicsEngine;

import java.nio.ByteBuffer;
import java.util.Random;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

public class GorskE {
    private static final String initalGameScenePath = "res/scenes/main.scene";
    
    public static final boolean isImmediateMode = true;
	
	// We need to strongly reference callback instances.
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    // The window handle
    private long window;
    
    /**
     * The current physics engine that is running in another thread
     */
    public PhysicsEngine physicsEngine;
    
    /**
     * The current GameScene being played
     */
    public GameScene currentScene;
    
    /** The reference to the active engine **/
	public static GorskE engine;

	public static void main(String[] args) {
		engine = new GorskE();
		engine.run();
	}
 
    public void run() {
        System.out.println("Hello LWJGL " + Sys.getVersion() + "!");
        try {
            initOpenGL(); //initalize opengl
            initGameScene(); //start intial game stuff 
            loop(); //run the main game loop
            end(); //end the game
 
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
     * This is the function responsible for initializing glfw and OpenGL
     */
    private void initOpenGL() {
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
        window = glfwCreateWindow(WIDTH, HEIGHT, "GorskE", NULL, NULL);
        
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
        
     
        // Now set the projection matrix to orthographic projection
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        // Now set the modelview matrix
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
		
        // Clears color buffers and gives us a nice color background.
		glClearColor(0.1f, 0.3f, 0.5f, 0.0f);
		// Enables depth testing which will be important to make sure
		// triangles are not rendering in front of each other when they
		// shouldn't be.
		glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glEnable(GL_ALPHA_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
       // glEnable(GL_CULL_FACE);
       // glCullFace(GL_BACK);
        glEnable(GL_FOG);
		// Prints out the current OpenGL version to the console.
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
    }
 
    /**
     * This is the function responsible for initializing the GameScene and other game specific stuff
     */
    private void initGameScene(){

        //TODO load in GameScene
        //currentScene = LoadUtil.loadGameSceneFromPath(initalGameScenePath);
        
        //XXX testing the rendering with this code
        
        currentScene = new GameScene();
        
		  float[] vertices = {
	                -0.01f, 0f, 0.01f,
	                -0.01f, 0f, -0.01f,
	                0.01f, 0f, 0.01f,
	                0.01f, 0f, -0.01f,
	                0f, 0.02f, 0f,
	        };
	        float[] colors = {
	                0f, 0f, 1f, 1f,
	                0f, 0f, 1f, 1f,
	                0f, 0f, 1f, 1f,
	                0f, 0f, 1f, 1f,
	                0f, 0f, 1f, 1f,
	        };
	        float[] normals = {
					1f, 1f, 1f,
					1f, 1f, 1f,
					1f, 1f, 1f,
					1f, 1f, 1f,
			};
			float[] textureCoords = {
					0, 0,
					1, 0,
					1, 1,
					0, 1,
			};
	        byte[] indices = {
	                0, 1, 3,
	                3, 2, 0,
	                0, 1, 4,
	                1, 3, 4,
	                2, 3, 4,
	                0, 2, 4,
	        };
        
        GameObject temp = new GameObject(0,0,0);
        currentScene.addGameObject(temp);
        temp.addComponents(new RenderStatic2DColorOnly(temp,vertices, colors, indices));
        
        Random rand = new Random();
        for(int i=0; i<100; i++){
        	GameObject tempT = new GameObject((rand.nextFloat()-0.5f)+(rand.nextFloat()-0.5f),(rand.nextFloat()-0.5f)+(rand.nextFloat()-0.5f),(rand.nextFloat()-0.5f)+(rand.nextFloat()-0.5f));
            currentScene.addGameObject(tempT);
            tempT.addComponents(new RenderStatic2DColorOnly(tempT, vertices, colors, indices));
        }
        //XXX testing!
 
        //Start physics simulation
        physicsEngine = new PhysicsEngine(currentScene);
        //new Thread(physicsEngine).start(); //starts the other thread XXX
        
        
    }
    
    
    
    /**
     * This is the main looping function responsible for running the game
     */
    private void loop() {
        //MAIN LOGIC IN HERE
        while ( glfwWindowShouldClose(window) == GL_FALSE ) {
        	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); //clear the display buffer
        		
        	currentScene.update(); //calls the update function on the whole scene
        	
        	currentScene.render(); //calls the update function on all the renderComponents in the scene
        		
        	//testDraw(); XXX
        	GL11.glRotatef(2f, -1f, -1f, 1f);//XXX test draw to see if working engine!
        	//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);//XXX
        	
        	calcFPS(); //Shows the FPS and number of GameObjects in the window's title        	
            glfwPollEvents(); // Poll for window events. The key callback above will only be invoked during this call.
            glfwSwapBuffers(window); //swap buffers to put new buffer to display
        }
    }
    
    /**
     * This function is called upon exiting of the engine
     */
    private void end(){
    	currentScene.destroy();
        
    	physicsEngine.end();
    	
        //destroy the window
        GLFW.nglfwDestroyWindow(0);
    }
    
    // USED IN CALCFPS!
    // Static values which only get initialised the first time the function runs 
	static double t0Value       = glfwGetTime(); // Set the initial time to now
	static int    fpsFrameCount = 0;             // Set the initial FPS frame count to 0
	static double fps           = 0.0;           // Set the initial FPS value to 0.0
    private double calcFPS()
    {
    	String theWindowTitle = "GorskE";
    	double theTimeInterval = 1.0;

    	// Get the current time in seconds since the program started (non-static, so executed every time)
    	double currentTime = glfwGetTime();

    	// Ensure the time interval between FPS checks is sane (low cap = 0.1s, high-cap = 10.0s)
    	// Negative numbers are invalid, 10 fps checks per second at most, 1 every 10 secs at least.
    	if (theTimeInterval < 0.1)
    	{
    		theTimeInterval = 0.1;
    	}
    	if (theTimeInterval > 10.0)
    	{
    		theTimeInterval = 10.0;
    	}

    	// Calculate and display the FPS every specified time interval
    	if ((currentTime - t0Value) > theTimeInterval)
    	{
    		// Calculate the FPS as the number of frames divided by the interval in seconds
    		fps = (double)fpsFrameCount / (currentTime - t0Value);

    		// If the user specified a window title to append the FPS value to...
    		if (theWindowTitle != "NONE")
    		{
    			// Convert the fps value into a string using an output stringstream
    			String fpsString = Double.toString(fps);

    			// Append the FPS value to the window title details
    			theWindowTitle += (" | FPS: " + fpsString + " | GameObjects: " + currentScene.getGameObjects().size());

    			// Convert the new window title to a c_str and set it
    			GLFW.glfwSetWindowTitle(window, theWindowTitle);
    		}

    		// Reset the FPS frame counter and set the initial time to be now
    		fpsFrameCount = 0;
    		t0Value = glfwGetTime();
    	}
    	else // FPS calculation time interval hasn't elapsed yet? Simply increment the FPS frame counter
    	{
    		fpsFrameCount++;
    	}

    	// Return the current FPS - doesn't have to be used if you don't want it!
    	return fps;
    }
}
