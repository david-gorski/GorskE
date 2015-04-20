package gorskE.camera;

import gorskE.input.Input;

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
		if(Input.isKeyDown(Input.getAction1()))
			super.xAngle += 1f;
		if(Input.isKeyDown(Input.getAction2()))
			super.yAngle += 1f;
		if(Input.isKeyDown(Input.getAction3()))
			super.zAngle += 1f;
		if(Input.isKeyDown(Input.getAction4())) {
			//Maybe add in something fun here
		}
		super.update();
	}
	
}
