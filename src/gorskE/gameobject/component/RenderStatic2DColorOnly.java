package gorskE.gameobject.component;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import gorskE.IO.VAO;
import gorskE.gameobject.GameObject;
import gorskE.shaders.ColorOnlyShader;

public class RenderStatic2DColorOnly extends RenderComponent{

	private VAO vao;
	
	public RenderStatic2DColorOnly(GameObject parent){
		super(parent,0,0,0);
		//XXX Testing purposes really, dumby constructor
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
	        byte[] indices = {
	                0, 1, 2,
	                2, 3, 0
	        };
		createVAO(vertices, colors, indices);
	}
	
	public RenderStatic2DColorOnly(GameObject parent, float x, float y, float z, float[] vertices, float[] colors, byte[] indices){
		super(parent,x,y,z);
		createVAO(vertices, colors, indices);
	}
	
	public RenderStatic2DColorOnly(GameObject parent, float[] vertices, float[] colors, byte[] indices){
		super(parent,0,0,0);
		createVAO(vertices, colors, indices);
	}
	
	
	private void createVAO(float[] vertices, float[] colors, byte[] indices){
		vao = new VAO(vertices, indices, new ColorOnlyShader());
		vao.addColors(colors);
	}

	@Override
	public String getTitle() {
		return "RenderStatic2DColorOnly";
	}

	@Override
	public void destroy() {
		vao.destroy();
	}
	
	public VAO getVAO(){
		return vao;
	}

	@Override
	public void render() {
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

		// Bind to the VAO that has all the information about the vertices
		GL30.glBindVertexArray(vao.getVaoId());
		
		//active all the currently active attribute lists on this vao
		int[] activeAttributeLists = vao.getActiveAttributes();
		for(int i : activeAttributeLists) {
			GL20.glEnableVertexAttribArray(i);
			System.out.println(i);
		}


		// Bind to the index VBO that has all the information about the order of the vertices
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vao.getInidicesId());

		GL11.glDrawElements(GL11.GL_TRIANGLES, vao.getIndicesCount(), GL11.GL_UNSIGNED_BYTE, 0);


		// Put everything back to default (deselect)
		GL20.glUseProgram(vao.getShader().getpId());
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
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


