package gorskE.IO;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class VBOUtils {
	
	public static FloatBuffer createFloatBuffer(float[] original){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(original.length);
		buffer.put(original);
		buffer.flip();
		return buffer;
	}

	public static ByteBuffer createBtyeBuffer(byte[] original) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(original.length);
		buffer.put(original);
		buffer.flip();
		return buffer;
	}

	public static IntBuffer createIntBuffer(int[] original) {
		IntBuffer buffer = BufferUtils.createIntBuffer(original.length);
		buffer.put(original);
		buffer.flip();
		return buffer;
	}

}
