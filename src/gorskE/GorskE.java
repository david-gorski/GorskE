package gorskE;

import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import gorskE.IO.VAO;
import gorskE.IO.load.ShaderLoader;
import gorskE.IO.load.TextureLoader;
import gorskE.gameobject.GameObject;
import gorskE.gameobject.component.RenderStatic2D;
import gorskE.physics.PhysicsEngine;

import java.io.BufferedReader;
import java.io.FileReader;
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
     * The array of all the currently drawing vertex-array-objects
     */
    private ArrayList<VAO> vaos;
    
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
        
        // Load and set up the shader programs
        setupShaders(); 
        
        //TODO load in GameScene
        //currentScene = LoadUtil.loadGameSceneFromPath(initalGameScenePath);
        
        //XXX testing the rendering with this code
        
        currentScene = new GameScene();
        
        GameObject temp = new GameObject(0,0,0);
        currentScene.addGameObject(temp);
        temp.addComponents(new RenderStatic2D(TextureLoader.loadTextureDirect("res/mage1.png")));
        
        //XXX testing!
 
        //Start physics simulation
        physicsEngine = new PhysicsEngine(currentScene);
        new Thread(physicsEngine).start(); //starts the other thread
        
        //MAIN LOGIC IN HERE
        while ( glfwWindowShouldClose(window) == GL_FALSE ) {
        	
        	if(physicsEngine.isDoneWithStep()){ //if the physics is done simulating
        		vaos.clear();
        		currentScene.pushVAO();
        	}
        	
        	render();//renders the scene
        	
            // Poll for window events. The key callback above will only be invoked during this call.
            glfwPollEvents();
        }
        end();
    }
    
    /**
     * This function is called upon exiting of the engine
     */
    private void end(){
    	currentScene.destroy();
    	
        // Delete the shaders
        GL20.glUseProgram(0);
        GL20.glDetachShader(pId, vsId);
        GL20.glDetachShader(pId, fsId);
         
        GL20.glDeleteShader(vsId);
        GL20.glDeleteShader(fsId);
        GL20.glDeleteProgram(pId);
        
        //destroy the window
        GLFW.nglfwDestroyWindow(0);
}
    
    private int vsId;
    private int fsId;
    private int pId;
    
    /**
     * This is called each frame, and is responsible with rendering the scene
     */
    private void render(){
    	 glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    	 glfwSwapBuffers(window); // swap the color buffers
  
         GL20.glUseProgram(pId);
          
    	 
    	 for(VAO vao : vaos) {
    		 // Bind the texture
    		 GL13.glActiveTexture(GL13.GL_TEXTURE0);
    		 GL11.glBindTexture(GL11.GL_TEXTURE_2D, vao.getTextureId());
    		 
             // Bind to the VAO that has all the information about the vertices
             GL30.glBindVertexArray(vao.getVaoId());
             GL20.glEnableVertexAttribArray(0);
             GL20.glEnableVertexAttribArray(1);
             GL20.glEnableVertexAttribArray(2);
              
             // Bind to the index VBO that has all the information about the order of the vertices
             GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vao.getInidicesId());
              
             // Draw the vertices
             GL11.glDrawElements(GL11.GL_TRIANGLES, vao.getIndicesCount(), GL11.GL_UNSIGNED_BYTE, 0);
              
             // Put everything back to default (deselect)
             GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
             GL20.glDisableVertexAttribArray(0);
             GL20.glDisableVertexAttribArray(1);
             GL30.glBindVertexArray(0);
    	 }
    	 GL20.glUseProgram(0);
    }
    
    private void setupShaders() {
        int errorCheckValue = GL11.glGetError();
         
        // Load the vertex shader
        vsId = ShaderLoader.loadShader("src/thequad/vertex.glsl", GL20.GL_VERTEX_SHADER);
        // Load the fragment shader
        fsId = ShaderLoader.loadShader("src/thequad/fragment.glsl", GL20.GL_FRAGMENT_SHADER);
         
        // Create a new shader program that links both shaders
        pId = GL20.glCreateProgram();
        GL20.glAttachShader(pId, vsId);
        GL20.glAttachShader(pId, fsId);
 
        // Position information will be attribute 0
        GL20.glBindAttribLocation(pId, 0, "in_Position");
        // Color information will be attribute 1
        GL20.glBindAttribLocation(pId, 1, "in_Color");
        // Normal information will be attribute 2
        GL20.glBindAttribLocation(pId, 2, "in_Normal");
        // TextureCoord information will be attribute 3
        GL20.glBindAttribLocation(pId, 3, "in_TextureCoord");
         
        GL20.glLinkProgram(pId);
        GL20.glValidateProgram(pId);
         
        errorCheckValue = GL11.glGetError();
        if (errorCheckValue != GL11.GL_NO_ERROR) {
            System.out.println("ERROR - Could not create the shaders:");
            System.exit(-1);
        }
    }
    
    public void addVAO(VAO vao){
    	vaos.add(vao);
    }
}
