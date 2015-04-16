package gorskE.camera;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import gorskE.GorskE;
import gorskE.util.math.Matrix4f;
import gorskE.util.math.Vector3f;

public abstract class Camera{
	
	/**
	 * The position of the camera in space
	 */
	protected float x=0, y=0, z=0;
	
	protected Matrix4f projection;
	protected Matrix4f view;
	
	private float oldX=0, oldY=0, oldZ=0;
    
    /**
     * The rotation of the axis. The X component stands for the rotation along the x-axis,
     * where 0 is dead ahead, 180 is backwards, and 360 is automically set to 0 (dead ahead). The value must be between
     * (including) 0 and 360. The Y component stands for the rotation along the y-axis, where 0 is looking straight
     * ahead, -90 is straight up, and 90 is straight down. The value must be between (including) -90 and 90.
     */
	protected Vector3f rotation = new Vector3f(0, 0, 0);
    
    /** The minimal distance from the camera where objects are rendered. */
	protected float zNear = 0.1f;
    /** The maximal distance from the camera where objects are rendered. */
	protected float zFar = 10f;  
	
    /** Defines the field of view. */
	protected int fov = 80; //IMP this dont do nothing yet
	protected float aspectRatio = (float)GorskE.WIDTH / (float)GorskE.HEIGHT;

	public Camera(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		init();
	}
	
	public void update() {
		view = Matrix4f.translate(x, y, z); //translate the camera
		//TODO add the rotation of the view matrix here
	}
	
	public void render(){
		
	}
	
	public void init(){
		// Setup projection matrix in a perspective mode
		projection = Matrix4f.perspective(fov, aspectRatio, zNear, zFar);
		// Setup a new view matrix and translate it over to where it should be XXX maybe wrong
		view = Matrix4f.translate(x, y, z);
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

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
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

	public float getOldX() {
		return oldX;
	}

	public float getOldY() {
		return oldY;
	}

	public float getOldZ() {
		return oldZ;
	}

}
