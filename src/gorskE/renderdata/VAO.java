package gorskE.renderdata;

import gorskE.GameObject;
import gorskE.GorskE;
import gorskE.shaders.ShaderProgram;
import gorskE.util.VAOUtils;
import gorskE.util.math.Matrix4f;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * This is the java implementation of the Vertex Array Object used to store VBO's (Vertex Buffer Objects);
 * these are used in to store the rendering data for OpenGL
 * 
 * Attribute Lists:
 * 0 is vertex position data
 * 1 is vertex color data
 * 2 is vertex normal vector data
 * 3 is texture coordinates
 * 4-16 are yet to be assigned
 * 
 * 
 * There is a variable amount of data per vertex for each type of data
 * For position there is three pieces so that there is a x, y, and z value for each vertex
 * 3 for position
 * 4 for color
 * 3 for normal
 * 2 for texture coordinates
 * 
 * @author David
 *
 */
public class VAO {
	
	public static final int positionIndex = 0;
	public static final int colorIndex = 1;
	public static final int normalIndex = 2;
	public static final int textureCoordIndex = 3;
	
	public static final int dataPerPositionVertex = 3;
	public static final int dataPerColorVertex = 4;
	public static final int dataPerNormalVertex = 3;
	public static final int dataPerTextureCoordVertex = 2;
	
	/**
	 * The openGL id for this instance of the VAO
	 */
	private int vaoId;
	
	/**
	 * The refernce to the Texture of this VAO, null if there is no texture
	 */
	private Texture texture;

	/**
	 * The array of vboId's that fill each of the 16 possible attribute lists in a VAO
	 */
	private int[] vbos = new int[16];
	
	/**
	 * This array contains all the information for verticies in this VAO.
	 * This coorisponds to the attribute lists
	 * For example vertex position, color, normal, or indicies.
	 * The data is at specific index values
	 * 0 is vertex position data
	 * 1 is vertex color data
	 * 2 is vertex normal vector data
	 * 3 is texture coordinates
	 * 4-16 are yet to be assigned
	 */
	private float[][] vertexData = new float[16][];
	
	/**
	 * This is the array of indices used by this VAO
	 */
	private byte[] indices;
	
	/**
	 * This is the array of positions of vertices that represents this in local space
	 */
	private float[] localCoordinatesPosition;
	
	/**
	 * The amount of vertices that this VAO represents
	 */
	private int vertexCount;
	
	/**
	 * The indices values of the VAO
	 */
	private int indicesVBOID;
	
	/** 
	 * The shader program this VAO will utilize
	 */
	private ShaderProgram shader;
	
	/**
	 * The matrix used in the model-view-projection multiplication scheme to get real world coordinates
	 */
	private Matrix4f modelMatrix = new Matrix4f();
	
	public VAO(float[] position, byte[] indices, ShaderProgram shader){
		if(!isImmediateMode()) { //VAO enabled stuff here
			vaoId = glGenVertexArrays(); //creates the vertex array in OpenGL
		}
		vertexCount = position.length/dataPerPositionVertex;
		this.shader = shader;
		addPositions(position);
		addIndices(indices);
	}
	
	public void addTexture(Texture newTexture){
		if(texture!=null){
			texture.destroy(this);
		}
		texture = newTexture;
	}
	
	public void addPositions(float[] data){
		if(!isImmediateMode()) { //VAOs enabled
			if(isVBOEnabled(positionIndex)) {
				updateFloatVBO(getPositionVBOId(), positionIndex, data, dataPerPositionVertex);
			}
			addFloatVBO(positionIndex, data, dataPerPositionVertex);
		}
		vertexData[positionIndex] = data;
		localCoordinatesPosition = data;
	}
	
	public void addColors(float[] data){
		if(!isImmediateMode()) { //VAOs enabled
			if(isVBOEnabled(colorIndex)) {
				updateFloatVBO(getColorVBOId(), colorIndex, data, dataPerColorVertex);
			}
			addFloatVBO(colorIndex, data, dataPerColorVertex);
		}
		vertexData[colorIndex] = data;
	}
	
	public void addNormals(float[] data){
		if(!isImmediateMode()) { //VAOs enabled
			if(isVBOEnabled(normalIndex)) {
				updateFloatVBO(getNormalVBOId(), normalIndex, data, dataPerNormalVertex);
			}
			addFloatVBO(normalIndex, data, dataPerNormalVertex);
		}
		vertexData[normalIndex] = data;
	}
	
	public void addTextureCoordinates(float[] data){
		if(!isImmediateMode()) { //VAOs enabled
			if(isVBOEnabled(textureCoordIndex)) {
				updateFloatVBO(getTextureCoordinatesVBOId(), textureCoordIndex, data, dataPerTextureCoordVertex);
			}
			addFloatVBO(textureCoordIndex, data, dataPerTextureCoordVertex);
		}
		vertexData[textureCoordIndex] = data;
	}
	
	public void addIndices(byte[] data) {
		this.indices = data;
		addIndicesVBO(data);
	}
	
	/**
	 * Creates a new float buffer object in opengl and binds it to the provided attribute list in this vao
	 * Default usage is GL_STATIC_DRAW
	 * @param attributeList The index of the attributelist which this data should go into
	 * @param data The data to be put into the buffer object
	 */
	public void addFloatVBO(int attributeList, float[] data, int dataPerVertex){
		addFloatVBO(attributeList, data, GL_STATIC_DRAW, dataPerVertex);
	}
	
	public void updateFloatVBO(int vboId, int attributeList, float[] data, int dataPerVertex){
		updateFloatVBO(vboId, attributeList, data, GL_STATIC_DRAW, dataPerVertex);
	}
	
	/**
	 * Creates a new float buffer object in opengl and binds it to the provided attribute list in this vao
	 * @param attributeList The index of the attributelist which this data should go into
	 * @param data The data to be put into the buffer object
	 * @param usage This represents how the data should be handled, whether it will be changed a lot or not
	 * @param dataPerVertex The amount of individual data points that are used to represent each vertex
	 */
	public void addFloatVBO(int attributeList, float[] data, int usage, int dataPerVertex){
		if(!isImmediateMode()) { //VAOs enabled
			glBindVertexArray(vaoId);
			int vboId = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			FloatBuffer buffer = VAOUtils.createFloatBuffer(data);
			glBufferData(GL_ARRAY_BUFFER, buffer, usage);
			glVertexAttribPointer(attributeList, dataPerVertex, GL_FLOAT, false, 0, 0);
			glEnableVertexAttribArray(attributeList); //XXX
			glBindBuffer(GL_ARRAY_BUFFER, 0); // Deselect (bind to 0) the VBO
			vbos[attributeList] = vboId;
			glBindVertexArray(0);
		}
	}
	
	public void addIndicesVBO(byte[] data){
		if(!isImmediateMode()) { //VAOs enabled
			// Create a new VBO for the indices and select it (bind)
			int vboId = glGenBuffers();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
			ByteBuffer buffer = VAOUtils.createBtyeBuffer(data);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0); // Deselect (bind to 0) the VBO
			indicesVBOID = vboId;
		}
	}
	
	public void updateFloatVBO(int vboId, int attributeList, float[] data, int usage, int dataPerVertex) {
		if(!isImmediateMode()) { //VAOs enabled
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			FloatBuffer buffer = VAOUtils.createFloatBuffer(data);
			glBufferData(GL_ARRAY_BUFFER, buffer, usage);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}
	}
	
	public void updateIndicesVBO(int vboId, int attributeList, byte[] data, int dataPerVertex) {
		if(!isImmediateMode()) { //VAOs enabled
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
			ByteBuffer buffer = VAOUtils.createBtyeBuffer(data);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0); // Deselect (bind to 0) the VBO
		}
	}
	
	public void updatePosition(GameObject go, float x, float y, float z) {
		VAOUtils.updateWorldPosition(this, go, x, y, z);
	}
	
	public void destroy(){
		if(!isImmediateMode()){
			glBindVertexArray(vaoId);
			for(int i : getActiveAttributes()) {
				glDisableVertexAttribArray(i);
			}
			glBindVertexArray(0);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glDeleteVertexArrays(vaoId);
			for(int vboId : vbos){
				glDeleteBuffers(vboId);
			}
		}
		if(texture!=null){
			texture.destroy(this);
		}	
	}
	
	public int getVaoId() {
		return vaoId;
	}
	
	public int getInidicesId(){
		return indicesVBOID; 
	}

	public int[] getVbos() {
		return vbos;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public int getIndicesCount() {
		return indices.length;
	}

	public ShaderProgram getShader() {
		return shader;
	}
	
	public boolean isImmediateMode() {
		return GorskE.isImmediateMode;
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
	
	/**
	 * This method uploads the current Model-View-Projection matrix to the shader program
	 */
	public void uploadMVP() {
		//sets the current program to be used
		GL20.glUseProgram(shader.getpId());
		
		//-- Upload matrices
		// Grab matrices
		Matrix4f viewMatrix = GorskE.engine.currentScene.camera.getView();
		Matrix4f projectionMatrix = GorskE.engine.currentScene.camera.getProjection();
		Matrix4f modelMatrix = getModelMatrix();
		FloatBuffer MVP = projectionMatrix.multiply(viewMatrix).multiply(modelMatrix).toFloatBuffer();

		// Upload matrices to the uniform variables
		glUniformMatrix4fv(shader.getUniform("MVP"), false, MVP);
		
		//sets the current program to be used
		GL20.glUseProgram(0);
	}
	
	/**
	 * A helpful method to automatically bind the shaderId, texture, indices VBO, and the vaoId, along with enabling the attributes
	 * This method also uploads the MVP matrix to the shader program
	 */
	public void bind() {
		if(!isImmediateMode()) { //VAO rendering mode is enabled
			uploadMVP(); //uploads current MVP matrix to shader
			
			//sets the current program to be used
			GL20.glUseProgram(shader.getpId());
	
			// Bind to the VAO that has all the information about the vertices
			GL30.glBindVertexArray(getVaoId());
			
			//enable all attribute lists
			enableAttributes();
			
			//bind the indices buffer
			glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, getInidicesId());
			
			if(texture!=null) {
				texture.bind();
			}
		}
	}
	
	/**
	 * A helpful method to automatically unbind the shaderId, texture, indices VBO, and the vaoId, along with disabling the attributes
	 */
	public void unbind() {
		if(!isImmediateMode()) { //VAO rendering mode is enabled
			GL20.glUseProgram(0);
	
			GL30.glBindVertexArray(0);
			
			disableAttributes();
			
			glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
			
			if(texture!=null) {
				texture.unbind();
			}
		}
	}
	
	public void enableAttributes() {
		//active all the currently active attribute lists on this vao
		int[] activeAttributeLists = getActiveAttributes();
		for(int i : activeAttributeLists) {
			glEnableVertexAttribArray(i);
		}
	}
	
	public void disableAttributes() {
		//active all the currently active attribute lists on this vao
		int[] activeAttributeLists = getActiveAttributes();
		for(int i : activeAttributeLists) {
			glDisableVertexAttribArray(i);
		}
	}
	
	private boolean isVBOEnabled(int index) {
		if(vbos[index] != 0) {
			return true;
		}
		return false;
	}
	
	public Texture getTexture() {
		return texture;
	}

	public float[] getPosition() {
		return vertexData[positionIndex];
	}
	
	public int getPositionVBOId() {
		return vbos[positionIndex];
	}
	
	public float[] getColor() {
		return vertexData[colorIndex];
	}
	
	public int getColorVBOId() {
		return vbos[colorIndex];
	}
	
	public float[] getNormal() {
		return vertexData[normalIndex];
	}
	
	public int getNormalVBOId() {
		return vbos[normalIndex];
	}
	
	public float[] getTextureCoordinates() {
		return vertexData[textureCoordIndex];
	}
	
	public int getTextureCoordinatesVBOId() {
		return vbos[textureCoordIndex];
	}

	public byte[] getIndices() {
		return indices;
	}
	
	public float[][] getAllVertexData() {
		return vertexData;
	}

	public float[] getLocalCoordinatesPosition() {
		return localCoordinatesPosition;
	}

	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}

	public void setModelMatrix(Matrix4f model) {
		this.modelMatrix = model;
	}
	
}
