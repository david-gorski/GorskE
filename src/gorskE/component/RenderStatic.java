package gorskE.component;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import gorskE.GameObject;
import gorskE.renderdata.Texture;
import gorskE.renderdata.VAO;
import gorskE.shaders.StaticShader;

public class RenderStatic extends Component {
	
	public VAO vao;
	
	public RenderStatic(GameObject parent, VAO vao, float x, float y, float z) {
		super("RenderStatic2D",parent,x,y,z);
		this.vao = vao;
	}

	public RenderStatic(GameObject parent, float x, float y, float z, Texture texture, float[] vertices, float[] colors, float[] normals, float[] textureCoords, byte[] indices){
		super("RenderStatic2D",parent,x,y,z);
		createVAO(vertices, colors, normals, textureCoords, indices, texture);
	}

	public RenderStatic(GameObject parent, Texture texture, float[] vertices, float[] colors, float[] normals, float[] textureCoords, byte[] indices){
		super("RenderStatic2D",parent,0,0,0);
		createVAO(vertices, colors, normals, textureCoords, indices, texture);
	}

	private void createVAO(float[] vertices, float[] colors, float[] normals, float[] textureCoords, byte[] indices, Texture texture){
		vao = new VAO(vertices, indices, new StaticShader());
		vao.addTexture(texture);
		vao.addColors(colors);
		vao.addNormals(normals);
		vao.addTextureCoordinates(textureCoords);
	}

	@Override
	public void destroy() {
		vao.destroy();
	}

	public VAO getVAO(){
		return vao;
	}

	public Texture getTexture(){
		return vao.getTexture();
	}

	public int getTextureId(){
		return vao.getTexture().getTextureID();
	}

	/**
	 * This is called each frame, and is responsible with rendering the scene
	 */
	public void render(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();         // Reset the model-view matrix           
		glMatrixMode(GL_MODELVIEW);

		int errorCheckValue;
		//error checking
		while ((errorCheckValue = GL11.glGetError()) != GL11.GL_NO_ERROR) {
			System.out.println("before rendering error in opengl: " + errorCheckValue);
			//System.exit(-1);
		}

		//sets the current program to be used
		GL20.glUseProgram(vao.getShader().getpId());

		// Bind the texture
		int loc = GL20.glGetUniformLocation(vao.getShader().getpId(), "texture");
		GL20.glUniform1i(loc, 0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0); //set the current texture to be binded to texture active 0
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, vao.getTexture().getTextureID()); //bind a texture to texture active 0

		// Bind to the VAO that has all the information about the vertices
		GL30.glBindVertexArray(vao.getVaoId());
		
		//active all the currently active attribute lists on this vao
		int[] activeAttributeLists = vao.getActiveAttributes();
		for(int i : activeAttributeLists) {
			GL20.glEnableVertexAttribArray(i);
		}


		// Bind to the index VBO that has all the information about the order of the vertices
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vao.getInidicesId());

		GL11.glDrawElements(GL11.GL_TRIANGLES, vao.getIndicesCount(), GL11.GL_UNSIGNED_BYTE, 0);


		// Put everything back to default (deselect)
		GL20.glUseProgram(vao.getShader().getpId());
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		for(int i : activeAttributeLists) {
			GL20.glDisableVertexAttribArray(i);
		}
		GL30.glBindVertexArray(0);

		//error checking
		while ((errorCheckValue = GL11.glGetError()) != GL11.GL_NO_ERROR) {
			System.out.println("rendering error in opengl: " + errorCheckValue);
			//System.exit(-1);
		}
		GL20.glUseProgram(0);
	}
	
	@Override
	public void renderImmediate() {
    	glMatrixMode(GL_PROJECTION);
    	glLoadIdentity();         // Reset the model-view matrix           
    	glMatrixMode(GL_MODELVIEW);
    	
    	glBegin(GL_TRIANGLES);                // Begin drawing the color triangles
    	int sizeOfColorVertex = 4;
    	int sizeOfPositionVertex = 3;
    	int sizeOfTexCoordVertex = 2;
    	int sizeOfNormalVertex = 3;
    	float[] color = vao.getColor();
    	float[] position = vao.getPosition();
    	float[] texCoord = vao.getTextureCoordinates();
    	float[] normal = vao.getNormal();
    	byte[] indices = vao.getIndices();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, vao.getTexture().getTextureID()); //bind a texture to texture active 0
    	for(int vertex=0; vertex<vao.getIndicesCount(); vertex++) {
    		int index = indices[vertex];
    		int colorStartIndex = 0+(index*sizeOfColorVertex);
    		GL11.glColor4f(color[colorStartIndex], color[colorStartIndex+1], color[colorStartIndex+2], color[colorStartIndex+3]);
    		int positionStartIndex = 0+(index*sizeOfPositionVertex);
            GL11.glVertex3f(position[positionStartIndex]+parent.getX(), position[positionStartIndex+1]+parent.getY(), position[positionStartIndex+2]+parent.getZ());
    		int texCoordStartIndex = 0+(index*sizeOfTexCoordVertex);
            GL11.glTexCoord2f(texCoord[texCoordStartIndex], texCoord[texCoordStartIndex+1]);
            int normalStartIndex = 0+(index*sizeOfNormalVertex);
            GL11.glNormal3f(normal[normalStartIndex], normal[normalStartIndex+1], normal[normalStartIndex+2]);
        }
    	glEnd();  // End of drawing color-cube  
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0); //bind null to texture active 0

	}
}
