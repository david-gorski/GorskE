package gorskE.camera;

import gorskE.input.Input;
import gorskE.util.math.Matrix4f;

public class MoveableCamera extends Camera{

	public MoveableCamera(float x, float y, float z) {
		super(x, y, z);
	}
	
	@Override
	public void update() {
		if(Input.isKeyDown(Input.getForward()))
			super.y -= 0.01f;
		
		if(Input.isKeyDown(Input.getLeft()))
			super.x += 0.01f;
		
		if(Input.isKeyDown(Input.getRight()))
			super.x -= 0.01f;
		
		if(Input.isKeyDown(Input.getBackward()))
			super.y += 0.01f;
		
		if(Input.isKeyDown(Input.getUp()))
			super.z -= 0.01f;
		
		if(Input.isKeyDown(Input.getDown()))
			super.z += 0.01f;
		super.update();
	}
	
}
