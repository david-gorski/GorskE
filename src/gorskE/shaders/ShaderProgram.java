package gorskE.shaders;

import org.lwjgl.opengl.GL20;

public abstract class ShaderProgram {
	
	/** The id for the complete shader program **/
	protected int pId;
	
	/** The id for the vertex shader program **/
	protected int vId;
	
	/** The id for the fragment shader program **/
	protected int fId;

	public int getpId() {
		return pId;
	}

	public int getvId() {
		return vId;
	}

	public int getfId() {
		return fId;
	}
	
	public void destroy() {
		GL20.glDetachShader(pId, vId);
        GL20.glDetachShader(pId, fId);
         
        GL20.glDeleteShader(vId);
        GL20.glDeleteShader(fId);
        GL20.glDeleteProgram(pId);
	}
	
	
	
}
