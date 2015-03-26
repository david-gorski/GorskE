package gorskE.shaders;

import gorskE.IO.load.ShaderLoader;

/**
 * A shader program that contains the most basic possible shaders
 * @author David
 *
 */
public class StaticShader extends ShaderProgram{
	
	public StaticShader() {
		String[] attributeNames = {"in_Position", "in_Color", "in_Normal", "in_TextureCoord"};
		int[] ids = ShaderLoader.setupShaderProgram("src/gorskE/shaders/vertex.glsl", "src/gorskE/shaders/fragment.glsl", attributeNames);
		pId = ids[0];
		vId = ids[1];
		fId = ids[2];
	}
	

}
