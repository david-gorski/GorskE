package gorskE.gameobject.component;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import gorskE.IO.Texture;
import gorskE.IO.VAO;
import gorskE.gameobject.GameObject;
import gorskE.shaders.StaticShader;

public class RenderStatic2D extends Component {
	
	public VAO vao;
	
	public RenderStatic2D(GameObject parent, VAO vao, float x, float y, float z) {
		super("RenderStatic2D",parent,x,y,z);
		this.vao = vao;
	}

	public RenderStatic2D(GameObject parent, Texture texture){
		//XXX Testing purposes really, dumby constructor
		super("RenderStatic2D",parent,0,0,0);
		float[] vertices = {
				-0.5f, 0.5f, 0f,
				-0.5f, -0.5f, 0f,
				0.5f, -0.5f, 0f,
				0.5f, 0.5f, 0f,
		};
		float[] colors = {
				1f, 1f, 1f, 1f,
				0f, 1f, 0f, 1f,
				0f, 0f, 1f, 1f,
				1f, 1f, 1f, 1f,
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
				0, 1, 2,
				2, 3, 0
		};
		createVAO(vertices, colors, normals, textureCoords, indices, texture);
	}

	public RenderStatic2D(GameObject parent, float x, float y, float z, Texture texture, float[] vertices, float[] colors, float[] normals, float[] textureCoords, byte[] indices){
		super("RenderStatic2D",parent,x,y,z);
		createVAO(vertices, colors, normals, textureCoords, indices, texture);
	}

	public RenderStatic2D(GameObject parent, Texture texture, float[] vertices, float[] colors, float[] normals, float[] textureCoords, byte[] indices){
		super("RenderStatic2D",parent,0,0,0);
		createVAO(vertices, colors, normals, textureCoords, indices, texture);
	}

	private void createVAO(float[] vertices, float[] colors, float[] normals, float[] textureCoords, byte[] indices, Texture texture){
		vao = new VAO(vertices, indices, new StaticShader());
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
}
