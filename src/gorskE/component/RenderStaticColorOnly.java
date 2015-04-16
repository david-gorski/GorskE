package gorskE.component;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import gorskE.GameObject;
import gorskE.renderdata.VAO;
import gorskE.shaders.ColorOnlyShader;

public class RenderStaticColorOnly extends Component{

	private VAO vao;
	
	private float oldX, oldY, oldZ;
	
	public RenderStaticColorOnly(GameObject parent, float x, float y, float z, float[] vertices, float[] colors, byte[] indices){
		super("RenderStatic2DColorOnly",parent,x,y,z);
		createVAO(vertices, colors, indices);
	}
	
	public RenderStaticColorOnly(GameObject parent, float[] vertices, float[] colors, byte[] indices){
		super("RenderStatic2DColorOnly",parent,0,0,0);
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
	public void update() {
		float newX = parent.getX() + super.x;
		float newY = parent.getY() + super.y;
		float newZ = parent.getZ() + super.z;
		if(newZ!=oldZ || newY!=oldY || newX!=oldX) { //if the gameobject has moved since last time move
			vao.updatePosition(parent, x, y, z);	 // update the VAO's world coordinates
		}
		oldZ = newZ;
		oldY = newY;
		oldX = newX;
	}

	@Override
	public void renderVAO() {
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
		}

		// Bind to the index VBO that has all the information about the order of the vertices
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vao.getInidicesId());

		GL11.glDrawElements(GL11.GL_TRIANGLES, vao.getIndicesCount(), GL11.GL_UNSIGNED_BYTE, 0);


		// Put everything back to default (deselect)
		GL20.glUseProgram(0);
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
	
	@Override
	public void renderImmediate() {
    	glMatrixMode(GL_PROJECTION);
    	glLoadIdentity();         // Reset the model-view matrix           
    	glMatrixMode(GL_MODELVIEW);
    	
    	glBegin(GL_TRIANGLES);                // Begin drawing the color triangles
    	int sizeOfColorVertex = 4;
    	int sizeOfPositionVertex = 3;
    	float[] color = vao.getColor();
    	float[] position = vao.getPosition();
    	byte[] indices = vao.getIndices();
    	for(int vertex=0; vertex<vao.getIndicesCount(); vertex++) {
    		int index = indices[vertex];
    		int colorStartIndex = 0+(index*sizeOfColorVertex);
    		glColor4f(color[colorStartIndex], color[colorStartIndex+1], color[colorStartIndex+2], color[colorStartIndex+3]);
    		int positionStartIndex = 0+(index*sizeOfPositionVertex);
            glVertex3f(position[positionStartIndex], position[positionStartIndex+1], position[positionStartIndex+2]);
    	}
    	glEnd();  // End of drawing color-cube  
     
	}
}


