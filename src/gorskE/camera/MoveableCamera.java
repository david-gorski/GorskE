package gorskE.camera;

import gorskE.input.Input;

public class MoveableCamera extends Camera{

	public MoveableCamera(float x, float y, float z) {
		super(x, y, z);
	}
	
	@Override
	public void update() {
		if(Input.isKeyDown(Input.getForward()))
			super.z += 0.001f;
		
		if(Input.isKeyDown(Input.getLeft()))
			super.x -= 0.001f;
		
		if(Input.isKeyDown(Input.getRight()))
			super.x += 0.001f;
		
		if(Input.isKeyDown(Input.getBackward()))
			super.z -= 0.001f;
		
		if(Input.isKeyDown(Input.getUp()))
			super.y += 0.001f;
		
		if(Input.isKeyDown(Input.getDown()))
			super.y -= 0.001f;
	}

}
