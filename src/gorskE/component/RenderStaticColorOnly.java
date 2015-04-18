package gorskE.component;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import gorskE.GameObject;
import gorskE.GorskE;
import gorskE.renderdata.VAO;
import gorskE.shaders.ColorOnlyShader;
import gorskE.util.math.Matrix4f;

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

		vao.bind();

		GL11.glDrawElements(GL11.GL_TRIANGLES, vao.getIndicesCount(), GL11.GL_UNSIGNED_BYTE, 0);

		vao.unbind();

	}	
	
	@Override
	public void renderImmediate() {
    	glMatrixMode(GL_MODELVIEW);
    	glLoadMatrixf(vao.getModelMatrix().multiply(GorskE.engine.currentScene.camera.getView()).toFloatBuffer());
    	
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


