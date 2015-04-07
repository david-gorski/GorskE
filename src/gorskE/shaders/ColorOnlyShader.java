package gorskE.shaders;

import gorskE.IO.load.ShaderLoader;

public class ColorOnlyShader extends ShaderProgram{
	
	public ColorOnlyShader() {
		String[] attributeNames = {"in_Position", "in_Color"};
		int[] ids = ShaderLoader.setupShaderProgram("src/gorskE/shaders/vertexColorOnly.glsl", "src/gorskE/shaders/fragmentColorOnly.glsl", attributeNames);
		pId = ids[0];
		vId = ids[1];
		fId = ids[2];
	}
	

}
