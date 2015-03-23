package gorskE.IO;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

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
 * 4 is the indices data
 * 
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
	 * The texture used for this VAO
	 */
	private Texture texture;
	
	/**
	 * The amount of individual pieces of data stored for every vertex
	 * 4 in most cases because things like position are actually (x,y,z,w) and color is (r,g,b,a)
	 * 
	 * Although texture coords are only 2 per vertex, go figure
	 */
	private static final int dataPerVertex = 4; 
	
	/**
	 * The attribute list in which the indices are stored in
	 */
	private static final int indiciesAttributeList = 4;

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
	
	public VAO(float[] position, float[] color, float[] normal, float[] textureCoord, int[] indices, Texture texture){
		vaoId = GL30.glGenVertexArrays(); //creates the vertex array in OpenGL
		vertexCount = position.length/dataPerVertex; //gets the amount of vertices there are
		indicesCount = indices.length;
		this.position = position;
		addPositions(position);
		addColors(color);
		addNormals(normal);
		addTextureCoordinates(textureCoord);
		addIndices(indices);
	}
	
	public void addIndices(int[] data){
		// Create a new VBO for the indices and select it (bind)
		int vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
		IntBuffer buffer = VBOUtils.createIntBuffer(data);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(indiciesAttributeList, dataPerVertex, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0); // Deselect (bind to 0) the VBO
		
		vbos[indiciesAttributeList] = vboId; //sets the vbo for the third position in the attribute lists in vbos
	}
	
	public void addPositions(float[] data){
		position = data;
		addFloatVBO(0, data);
	}
	
	public void addColors(float[] data){
		addFloatVBO(1, data);
	}
	
	public void addNormals(float[] data){
		addFloatVBO(2, data);
	}
	
	public void addTextureCoordinates(float[] data){
		addFloatVBO(3, data);
	}
	
	public void addFloatVBO(int attributeList, float[] data){
		addFloatVBO(attributeList, data, GL15.GL_STATIC_DRAW);
	}
	
	public void addFloatVBO(int attributeList, float[] data, int usage){
		int vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		FloatBuffer buffer = VBOUtils.createFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, usage);
		GL20.glVertexAttribPointer(attributeList, dataPerVertex, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // Deselect (bind to 0) the VBO
		vbos[attributeList] = vboId;
	}
	
	public void destroy(){
		unbind();
	}
	
	public void unbind(){
		GL30.glDeleteVertexArrays(vaoId);
		for(int vboId : vbos){
			GL15.glDeleteBuffers(vboId);
		}
	}

	public int getVaoId() {
		return vaoId;
	}
	
	public int getInidicesId(){
		return vbos[indiciesAttributeList]; //returns the fourth attribute list vbo id because that is where the indices are supposed to be stored
	}

	public int getDataPerVertex() {
		return dataPerVertex;
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
	
	public int getTextureId(){
		return texture.getTextureID();
	}
	
	public Texture getTexture(){
		return texture;
	}
	
	public void setTexture(Texture newTexture){
		texture = newTexture;
	}
	
}