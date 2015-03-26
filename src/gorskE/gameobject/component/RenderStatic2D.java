package gorskE.gameobject.component;

import gorskE.Main;
import gorskE.IO.Texture;
import gorskE.IO.VAO;
import gorskE.IO.VBOUtils;
import gorskE.gameobject.GameObject;
import gorskE.shaders.StaticShader;

public class RenderStatic2D implements Component {
	
	private GameObject parent;
	
	private float x=0,y=0,z=0;
	
	private VAO vao;
	
	public RenderStatic2D(GameObject parent, Texture texture){
		//XXX Testing purposes really, dumby constructor
		this.parent = parent;
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
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.z = z;
		createVAO(vertices, colors, normals, textureCoords, indices, texture);
	}
	
	public RenderStatic2D(GameObject parent, Texture texture, float[] vertices, float[] colors, float[] normals, float[] textureCoords, byte[] indices){
		this.parent = parent;
		createVAO(vertices, colors, normals, textureCoords, indices, texture);
	}
	
	public void pushVAO(){
		vao.addPositions(VBOUtils.updatePosition(vao.getPosition(), x,y,z, parent));
		Main.getEngine().addVAO(vao);
	}
	
	private void createVAO(float[] vertices, float[] colors, float[] normals, float[] textureCoords, byte[] indices, Texture texture){
		vao = new VAO(vertices, colors, normals, textureCoords, indices, texture, new StaticShader());
		pushVAO();
	}

	@Override
	public void update() {
		//I don't think it needs to update anything
	}

	@Override
	public void physicUpdate() {
		//This render based component doesn't do anything in the physics step
	}

	@Override
	public String getTitle() {
		return "RenderStatic";
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
	
	public GameObject getParent(){
		return parent;
	}

}
