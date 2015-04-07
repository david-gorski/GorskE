package gorskE;

import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import gorskE.IO.VAO;
import gorskE.IO.load.TextureLoader;
import gorskE.gameobject.GameObject;
import gorskE.gameobject.component.RenderStatic2D;
import gorskE.gameobject.component.RenderStatic2DColorOnly;
import gorskE.physics.PhysicsEngine;
import gorskE.util.VBOUtils;

import java.nio.ByteBuffer;
 
import java.util.ArrayList;

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
        
        
     // First set the clear color of the screen
        glClearColor(0, 0, 0, 0);
     
        // Now set the projection matrix to orthographic projection
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 100, 100, 0, 0.01f, 100f);
        // Now set the modelview matrix
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
		
        // Clears color buffers and gives us a nice color background.
		glClearColor(0.1f, 0.3f, 0.5f, 0.0f);
		// Enables depth testing which will be important to make sure
		// triangles are not rendering in front of each other when they
		// shouldn't be.
		glEnable(GL_DEPTH_TEST);
		// Prints out the current OpenGL version to the console.
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
    }
 
    /**
     * This is the main looping function responsible for running the game
     */
    private void loop() {
        
        //TODO load in GameScene
        //currentScene = LoadUtil.loadGameSceneFromPath(initalGameScenePath);
        
        //XXX testing the rendering with this code
        
        currentScene = new GameScene();
        
        GameObject temp = new GameObject(0,0,0);
        currentScene.addGameObject(temp);
        temp.addComponents(new RenderStatic2DColorOnly(temp));
        
        //XXX testing!
 
        //Start physics simulation
      //  physicsEngine = new PhysicsEngine(currentScene);
      //  new Thread(physicsEngine).start(); //starts the other thread
        
        //MAIN LOGIC IN HERE
        while ( glfwWindowShouldClose(window) == GL_FALSE ) {
        	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); //clear the display buffer
        		
        	currentScene.update(); //calls the update function on the whole scene
        	
        	currentScene.render(); //calls the update function on all the renderComponents in the scene
        		
        	testDraw(); //XXX test draw to see if working engine!
        	        	
            glfwPollEvents(); // Poll for window events. The key callback above will only be invoked during this call.
            glfwSwapBuffers(window); //swap buffers to put new buffer to display
        }
        end();
    }
    
    /**
     * This function is called upon exiting of the engine
     */
    private void end(){
    	currentScene.destroy();
        
        //destroy the window
        GLFW.nglfwDestroyWindow(0);
}
    
    public void testDraw() {
    	glMatrixMode(GL_PROJECTION);
    	glLoadIdentity();         // Reset the model-view matrix           
    	glMatrixMode(GL_MODELVIEW);
    	
    	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    	glBegin(GL_QUADS);                // Begin drawing the color cube with 6 quads
        // Top face (y = 1.0f)
        // Define vertices in counter-clockwise (CCW) order with normal pointing out
        glColor3f(0.0f, 1.0f, 0.0f);     // Green
        glVertex3f( 0.2f, 0.2f, -0.2f);
        glVertex3f(-0.2f, 0.2f, -0.2f);
        glVertex3f(-0.2f, 0.2f,  0.2f);
        glVertex3f( 0.2f, 0.2f,  0.2f);
    
        // Bottom face (y = -1.0f)
        glColor3f(1.0f, 0.5f, 0.0f);     // Orange
        glVertex3f( 0.2f, -0.2f,  0.2f);
        glVertex3f(-0.2f, -0.2f,  0.2f);
        glVertex3f(-0.2f, -0.2f, -0.2f);
        glVertex3f( 0.2f, -0.2f, -0.2f);
    
        // Front face  (z = 1.0f)
        glColor3f(1.0f, 0.0f, 0.0f);     // Red
        glVertex3f( 0.2f,  0.2f, 0.2f);
        glVertex3f(-0.2f,  0.2f, 0.2f);
        glVertex3f(-0.2f, -0.2f, 0.2f);
        glVertex3f( 0.2f, -0.2f, 0.2f);
    
        // Back face (z = -1.0f)
        glColor3f(1.0f, 1.0f, 0.0f);     // Yellow
        glVertex3f( 0.2f, -0.2f, -0.2f);
        glVertex3f(-0.2f, -0.2f, -0.2f);
        glVertex3f(-0.2f,  0.2f, -0.2f);
        glVertex3f( 0.2f,  0.2f, -0.2f);
    
        // Left face (x = -1.0f)
        glColor3f(0.0f, 0.0f, 1.0f);     // Blue
        glVertex3f(-0.2f,  0.2f,  0.2f);
        glVertex3f(-0.2f,  0.2f, -0.2f);
        glVertex3f(-0.2f, -0.2f, -0.2f);
        glVertex3f(-0.2f, -0.2f,  0.2f);
    
        // Right face (x = 1.0f)
        glColor3f(1.0f, 0.0f, 1.0f);     // Magenta
        glVertex3f(0.2f,  0.2f, -0.2f);
        glVertex3f(0.2f,  0.2f,  0.2f);
        glVertex3f(0.2f, -0.2f,  0.2f);
        glVertex3f(0.2f, -0.2f, -0.2f);
     glEnd();  // End of drawing color-cube   
     
     GL11.glRotatef(2f, 1f, 1f, 1f);
    }
}
