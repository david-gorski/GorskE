package gorskE.camera;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import gorskE.GorskE;
import gorskE.util.math.Matrix4f;
import gorskE.util.math.Vector3f;

public abstract class Camera{
	
	/**
	 * The position of the camera in space
	 */
	protected float x=0, y=0, z=0;
	
	/**
	 * The rotation of the camera in space
	 */
	protected float xAngle=0, yAngle=0, zAngle=0;
	
	/**
	 * The projection matrix used in opengl render calls
	 * This matrix represents the projection out of the camera (imagine its like what the lens can see)
	 */
	protected Matrix4f projection;
	/**
	 * The view matrix used in opengl render calls
	 * This matrix represents the camera's position
	 */
	protected Matrix4f view;
	
	private float oldX=0, oldY=0, oldZ=0; //these old variables for knowing when to update the view matrix
	private float oldXAngle=0, oldYAngle=0, oldZAngle=0;
    
    /** The minimal distance from the camera where objects are rendered. */
	protected float zNear = 0.00001f;
    /** The maximal distance from the camera where objects are rendered. */
	protected float zFar = 10f;  
	
    /** Defines the field of view. **/
	protected int fov = 80;
	/** Defines the aspectRatio **/
	protected float aspectRatio = (float)GorskE.WIDTH / (float)GorskE.HEIGHT;

	public Camera(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		init();
	}
	
	/**
	 * Called every frame to update the view matrix based on the position and rotation of the camera
	 */
	public void update() {
		// Update position and rotation of view matrix //
		if(oldX!=x || oldY!=y || oldZ!=z){
			view = view.multiply(Matrix4f.translate(x-oldX, y-oldY, z-oldZ));
		}
		if(oldXAngle != xAngle){ //update x rotation if it changed
			view = view.multiply(Matrix4f.rotationalXMatrix(xAngle-oldXAngle));
		}
		if(oldYAngle != yAngle){ //update y rotation if it changed
			view = view.multiply(Matrix4f.rotationalYMatrix(yAngle-oldYAngle));
		}
		if(oldZAngle != zAngle){ //update z rotation if it changed
			view = view.multiply(Matrix4f.rotationalZMatrix(zAngle-oldZAngle));
		}
		setOld(); //sets the old variables to the current variables
	}
	
	/**
	 * Sets up the matrices (projection and view)
	 */
	public void init(){
		// Setup projection matrix in a perspective mode
		projection = Matrix4f.perspective(fov, aspectRatio, zNear, zFar);
		if(GorskE.isImmediateMode){//if immediate mode is on
			glMatrixMode(GL_PROJECTION); //update the fixed function
			glMultMatrixf(projection.toFloatBuffer()); //pipeline projection matrix
		}
		
		// Setup a new view matrix and translate it over to where it should be by translating and rotatiing it
		view = Matrix4f.translate(x, y, z);
		view = view.multiply(Matrix4f.rotationalXMatrix(xAngle));
		view = view.multiply(Matrix4f.rotationalYMatrix(yAngle));
		view = view.multiply(Matrix4f.rotationalZMatrix(zAngle));
	}
	
	private void setOld(){
		oldX = x;
		oldY = y;
		oldZ = z;
		oldZAngle = zAngle;
		oldYAngle = yAngle;
		oldXAngle = xAngle;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
		init();
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
		init();
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
		init();
	}

	public float getxAngle() {
		return xAngle;
	}

	public void setxAngle(float xAngle) {
		this.xAngle = xAngle;
		init();
	}

	public float getyAngle() {
		return yAngle;
	}

	public void setyAngle(float yAngle) {
		this.yAngle = yAngle;
		init();
	}

	public float getzAngle() {
		return zAngle;
	}

	public void setzAngle(float zAngle) {
		this.zAngle = zAngle;
		init();
	}

	public float getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
		init();
	}

	public float getzNear() {
		return zNear;
	}

	public void setzNear(float zNear) {
		this.zNear = zNear;
		init();
	}

	public float getzFar() {
		return zFar;
	}

	public void setzFar(float zFar) {
		this.zFar = zFar;
		init();
	}

	public int getFov() {
		return fov;
	}

	public void setFov(int fov) {
		this.fov = fov;
		init();
	}

	public Matrix4f getProjection() {
		return projection;
	}

	public Matrix4f getView() {
		return view;
	}

}
