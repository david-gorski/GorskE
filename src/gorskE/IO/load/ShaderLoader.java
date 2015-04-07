package gorskE.IO.load;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import gorskE.IO.Texture;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class ShaderLoader {
	
	/**
	 * The hashmap of all the individual loaded shaders based on their filepaths
	 */
	private static HashMap<String, Integer> loadedShaders = new HashMap<String, Integer>();
	
	/**
	 * The hashmap of all the completed shader programs based on an array of their individual shader id's
	 */
	private static HashMap<Integer[], Integer> loadedShaderPrograms = new HashMap<Integer[], Integer>();
    
    public static int loadShader(String filename, int type) {
    	int shaderID = 0;
    	if(!loadedShaders.containsKey(filename)) {
	        StringBuilder shaderSource = new StringBuilder();
	         
	        try {
	            BufferedReader reader = new BufferedReader(new FileReader(filename));
	            String line;
	            while ((line = reader.readLine()) != null) {
	                shaderSource.append(line).append("\n");
	            }
	            reader.close();
	        } catch (Exception e) {
	            System.err.println("Could not read file.");
	            e.printStackTrace();
	            System.exit(-1);
	        }
	         
	        shaderID = GL20.glCreateShader(type);
	    	System.out.println(shaderSource);
	        GL20.glShaderSource(shaderID, shaderSource);
	        GL20.glCompileShader(shaderID);
	         
	        return shaderID;
	   }
    return loadedShaders.get(filename);
    }
    
    /**
     * Creates a complete shader program by linking a vertex and a fragment shader.
     * @param vertexFilepath the filepath for the vertex shader
     * @param fragmentFilepath the filepath for the fragment shader
     * @param attributeNames an array of the names of the shader program variables that should be associated with the attribute list at that index. 
     * Example: attribute list 0 contains position data so the variable name in the shader might be called "in_Position", so the 0th index should contain "in_Position"
     * @return Index 0 is the program shader id
     * index 1 is the vertex shader id
     * index 2 is the fragment shader id
     */
    public static int[] setupShaderProgram(String vertexFilepath, String fragmentFilepath, String[] attributeNames) {
    	int vId, fId, pId;
        int errorCheckValue = GL11.glGetError();
        
        // Load the vertex shader
        vId = ShaderLoader.loadShader(vertexFilepath, GL20.GL_VERTEX_SHADER);
        // Load the fragment shader
        fId = ShaderLoader.loadShader(fragmentFilepath, GL20.GL_FRAGMENT_SHADER);
        int[] bothIds = {vId, fId};
    	if(!loadedShaderPrograms.containsKey(bothIds)) { //checks if the program has already been made that puts these two shaders into one program
	        // Create a new shader program that links both shaders
	        pId = GL20.glCreateProgram();
	        GL20.glAttachShader(pId, vId);
	        GL20.glAttachShader(pId, fId);
	        
	        for(int i=0; i<attributeNames.length; i++) {
	        	//iterates through all the supplied attribute names for each attribute list
		        GL20.glBindAttribLocation(pId, i, attributeNames[i]); //sets the attribute at index i to the string in attributeNames
		        
		        // HARDCODED EXAMPLE!
		        // Position information will be attribute 0
		        // GL20.glBindAttribLocation(pId, 0, "in_Position");
	        }
	        
	        //creates the program
	        // glBindFragDataLocation(pId, 0, "fragColor");
	        GL20.glLinkProgram(pId);
	        GL20.glValidateProgram(pId); 
	         
	        errorCheckValue = GL11.glGetError();
	        if (errorCheckValue != GL11.GL_NO_ERROR) {
	            System.out.println("ERROR - Could not create the shaders:");
	            System.exit(-1);
	        }
	        
	        int status = glGetShaderi(vId, GL_COMPILE_STATUS);
	        if (status != GL_TRUE) {
	            throw new RuntimeException(glGetShaderInfoLog(vId));
	        }
	        status = glGetShaderi(fId, GL_COMPILE_STATUS);
	        if (status != GL_TRUE) {
	            throw new RuntimeException(glGetShaderInfoLog(fId));
	        }
    	}else {
    		pId = loadedShaderPrograms.get(bothIds); //get the already made shaderprogram id
    	}
    	int[] answer = {pId, vId, fId};
        return answer;
    }
    
}
