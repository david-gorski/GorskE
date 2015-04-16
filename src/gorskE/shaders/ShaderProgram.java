package gorskE.shaders;

import gorskE.util.math.Matrix4f;
import gorskE.util.math.Vector3f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

	public static final int vertexAttributeListIndex = 0;
	public static final int colorAttributeListIndex = 1;
	public static final int normalAttributeListIndex = 2;
	public static final int textureCoordinateAttributeListIndex = 3;

	private Map<String, Integer> locationCache = new HashMap<String, Integer>();

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
		glDetachShader(pId, vId);
		glDetachShader(pId, fId);

		glDeleteShader(vId);
		glDeleteShader(fId);
		glDeleteProgram(pId);
	}

	// Most of these functions have been built primarily to
	// deal with those uniform variables that we need to be
	// able to communicate with. You'll see why these
	// are handy later on in the series!
	public int getUniform(String name) {
		if (locationCache.containsKey(name)) {
			return locationCache.get(name);
		}
		int result = glGetUniformLocation(pId, name);
		if (result == -1)
			System.err
					.println("Could not find uniform variable'" + name + "'!");
		else
			locationCache.put(name, result);
		return result;
	}

	public void setUniform1i(String name, int value) {
		glUniform1i(getUniform(name), value);
	}

	public void setUniform2f(String name, float x, float y) {
		glUniform2f(getUniform(name), x, y);
	}

	public void setUniform3f(String name, Vector3f vector) {
		glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
	}

	public void setUniformMat4f(String name, Matrix4f matrix) {
		glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
	}

	// Again this is like selecting the layer
	// that we wish to use.
	public void enable() {
		glUseProgram(pId);
	}

	// whilst this deselects that very same
	// layer.
	public void disable() {
		glUseProgram(0);
	}

}
