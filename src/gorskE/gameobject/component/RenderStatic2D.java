package gorskE.gameobject.component;

import gorskE.Main;
import gorskE.IO.Texture;
import gorskE.IO.VAO;

public class RenderStatic2D implements Component {
	
	private float x=0,y=0,z=0,w=0;
	
	private VAO vao;
	
	public RenderStatic2D(Texture texture){
		//XXX Testing purposes really, dumby constructor
		  float[] vertices = {
	                -0.5f, 0.5f, 0f, 1f,
	                -0.5f, -0.5f, 0f, 1f,
	                0.5f, -0.5f, 0f, 1f,
	                0.5f, 0.5f, 0f, 1f
	        };
	        float[] colors = {
	                1f, 0f, 0f, 1f,
	                0f, 1f, 0f, 1f,
	                0f, 0f, 1f, 1f,
	                1f, 1f, 1f, 1f,
	        };
	        float[] normals = {
	        		0f, 0f, 0f, 0f,
	                0f, 0f, 0f, 0f,
	                0f, 0f, 0f, 0f,
	                0f, 0f, 0f, 0f,
	        };
	        float[] textureCoords = {
	        		0, 0,
	        		1, 0,
	        		0, 1,
	        		1, 1,
	        };
	        int[] indices = {
	                0, 1, 2,
	                2, 3, 0
	        };
		createVAO(vertices, colors, normals, textureCoords, indices, texture);
	}
	
	public RenderStatic2D(float x, float y, float z, float w, Texture texture, float[] vertices, float[] colors, float[] normals, float[] textureCoords, int[] indices){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		createVAO(vertices, colors, normals, textureCoords, indices, texture);
	}
	
	public RenderStatic2D(Texture texture, float[] vertices, float[] colors, float[] normals, float[] textureCoords, int[] indices){
		createVAO(vertices, colors, normals, textureCoords, indices, texture);
	}
	
	public void pushVAO(){
		Main.getEngine().addVAO(vao);
	}
	
	private void createVAO(float[] vertices, float[] colors, float[] normals, float[] textureCoords, int[] indices, Texture texture){
		vao = new VAO(vertices, colors, normals, textureCoords, indices, texture);
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
	}
	
	public VAO getVAO(){
		return vao;
	}
	
	public Texture getTexture(){
		return vao.getTexture();
	}

}
