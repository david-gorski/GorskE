package gorskE.util;

import gorskE.GameObject;
import gorskE.renderdata.VAO;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class VAOUtils {
	
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
	
	public static void updateWorldPosition(VAO vao, GameObject go, float compX, float compY, float compZ) {
		float[] temp = vao.getPosition(); //excludes .clone() because i want to update this in case of immediate mode so that it uses the right positions
		int counter = 1;
		for(int i=0; i<temp.length; i++) {
			if(counter==1) { //its the first element so the X
				temp[i] = temp[i] + compX + go.getX();
			}else if(counter==2) { //its the second element so the Y
				temp[i] = temp[i] + compY + go.getY();
			}else if(counter==3) { //its the first element so the Z
				temp[i] = temp[i] + compZ + go.getZ();
			}
			counter++;
			if(counter>3) {
				counter=1;
			}
		}
		vao.updateFloatVBO(vao.getVbos()[vao.positionIndex], vao.positionIndex, temp, vao.dataPerPositionVertex);
	}

}
