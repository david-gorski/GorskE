package gorskE.IO;

import gorskE.shaders.ShaderProgram;
import gorskE.util.VBOUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * This is the java implementation of the Vertex Array Object used to store VBO's (Vertex Buffer Objects);
 * these are used in to store the rendering data for OpenGL
 * 
 * Attribute Lists:
 * 0 is vertex position data
 * 1 is vertex color data
 * 2 is vertex normal vector data
 * 3 is texture coordinates
 * 
 * @author David
 *
 */
public class VAO {
	
	/**
	 * The openGL id for this instance of the VAO
	 */
	private int vaoId;

	/**
	 * The array of vboId's that fill each of the 16 possible attribute lists in a VAO
	 */
	private int[] vbos = new int[16];
	
	/**
	 * The amount of vertices that this VAO represents
	 */
	private int vertexCount;
	
	/**
	 * The amount of indices that this VAO represents
	 */
	private int indicesCount;
	
	/**
	 * The original position values of the VAO
	 */
	private float[] position;
	
	/**
	 * The indices values of the VAO
	 */
	private int indicesVBOID;
	
	/** 
	 * The shader program this VAO will utilize
	 */
	private ShaderProgram shader;
	
	public VAO(float[] position, byte[] indices, ShaderProgram shader){
		vaoId = GL30.glGenVertexArrays(); //creates the vertex array in OpenGL
		vertexCount = position.length/3; //3 is the 
		indicesCount = indices.length;
		this.position = position;
		this.shader = shader;
		addPositions(position);
		addIndices(indices);
	}
	
	public void addPositions(float[] data){
		position = data;
		addFloatVBO(0, data, 3);
	}
	
	public void addColors(float[] data){
		addFloatVBO(1, data, 4);
	}
	
	public void addNormals(float[] data){
		addFloatVBO(2, data, 3);
	}
	
	public void addTextureCoordinates(float[] data){
		addFloatVBO(3, data, 2);
	}
	
	/**
	 * Creates a new float buffer object in opengl and binds it to the provided attribute list in this vao
	 * Default usage is GL_STATIC_DRAW
	 * @param attributeList The index of the attributelist which this data should go into
	 * @param data The data to be put into the buffer object
	 */
	public void addFloatVBO(int attributeList, float[] data, int dataPerVertex){
		addFloatVBO(attributeList, data, GL15.GL_STATIC_DRAW, dataPerVertex);
	}
	
	/**
	 * Creates a new float buffer object in opengl and binds it to the provided attribute list in this vao
	 * @param attributeList The index of the attributelist which this data should go into
	 * @param data The data to be put into the buffer object
	 * @param usage This represents how the data should be handled, whether it will be changed a lot or not
	 * @param dataPerVertex The amount of individual data points that are used to represent each vertex
	 */
	public void addFloatVBO(int attributeList, float[] data, int usage, int dataPerVertex){
		int vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		FloatBuffer buffer = VBOUtils.createFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, usage);
		GL20.glVertexAttribPointer(attributeList, dataPerVertex, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // Deselect (bind to 0) the VBO
		vbos[attributeList] = vboId;
	}
	
	public void addIndices(byte[] data){
		// Create a new VBO for the indices and select it (bind)
		int vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
		ByteBuffer buffer = VBOUtils.createBtyeBuffer(data);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0); // Deselect (bind to 0) the VBO
		indicesVBOID = vboId;
	}
	
	public void destroy(){
		unbind();
	}
	
	public void unbind(){
		GL30.glBindVertexArray(vaoId);
		for(int i : getActiveAttributes()) {
			GL20.glDisableVertexAttribArray(i);
		}
		GL30.glBindVertexArray(0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glDeleteVertexArrays(vaoId);
		for(int vboId : vbos){
			GL15.glDeleteBuffers(vboId);
		}
	}

	public int getVaoId() {
		return vaoId;
	}
	
	public int getInidicesId(){
		return indicesVBOID; 
	}
	
	public float[] getPosition(){
		return position;
	}

	public int[] getVbos() {
		return vbos;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public int getIndicesCount() {
		return indicesCount;
	}

	public ShaderProgram getShader() {
		return shader;
	}
	
	/**
	 * To clarify this doesnt return an vbo id's, it only returns an array of the indexes of the active attribute lists
	 * @return an int array of the attribute lists that are active on this vao
	 */
	public int[] getActiveAttributes() {
		int size = 0;
		for(int i=0; i<vbos.length; i++) {
			int vboIdAtI = vbos[i];
			if(vboIdAtI != 0) { //there is a legit vbo at that attribute list
				size++;
			}
		}
		int[] actives = new int[size];
		int activesIndex = 0;
		for(int i=0; i<vbos.length && actives.length>0; i++) {
			int vboIdAtI = vbos[i];
			if(vboIdAtI != 0) { //there is a legit vbo at that attribute list
				actives[activesIndex] = i;
			}
		}
		return actives;
	}

}
