package gorskE.util.math;

import gorskE.GameObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class VBOUtils {
	
	public static FloatBuffer createFloatBuffer(float[] original){
 		FloatBuffer buffer = BufferUtils.createFloatBuffer(original.length);
 		buffer.put(original);
		//Wbuffer.rewind();
		buffer.flip();
		return buffer;
	}

	public static ByteBuffer createBtyeBuffer(byte[] original) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(original.length);
		buffer.put(original);
		//buffer.rewind();
		buffer.flip();
		return buffer;
	}

	public static IntBuffer createIntBuffer(int[] original) {
		IntBuffer buffer = BufferUtils.createIntBuffer(original.length);
		buffer.put(original);
		//buffer.rewind();
		buffer.flip();
		return buffer;
	}
	
	public static float[] updatePosition(float[] original, float compX, float compY, float compZ, GameObject go) {
		float[] temp = original.clone();
		int counter = 1;
		for(int i=0; i<original.length; i++) {
			if(counter==1) { //its the first element so the X
				original[i] = original[i] + compX + go.getX();
			}else if(counter==2) { //its the second element so the Y
				original[i] = original[i] + compY + go.getY();
			}else if(counter==3) { //its the first element so the Z
				original[i] = original[i] + compZ + go.getZ();
			}
			counter++;
			if(counter>3) {
				counter=1;
			}
		}
		return (temp);
	}

}
