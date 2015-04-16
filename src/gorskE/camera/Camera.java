package gorskE.camera;

import static org.lwjgl.opengl.GL11.*;

import gorskE.util.math.Vector3f;

public abstract class Camera{
	
	/**
	 * The position of the camera in space
	 */
	private float x=0, y=0, z=0;
    
    /**
     * The rotation of the axis. The X component stands for the rotation along the x-axis,
     * where 0 is dead ahead, 180 is backwards, and 360 is automically set to 0 (dead ahead). The value must be between
     * (including) 0 and 360. The Y component stands for the rotation along the y-axis, where 0 is looking straight
     * ahead, -90 is straight up, and 90 is straight down. The value must be between (including) -90 and 90.
     */
    private Vector3f rotation = new Vector3f(0, 0, 0);
    
    /** The minimal distance from the camera where objects are rendered. */
    private float zNear = 0.00001f;
    /** The maximal distance from the camera where objects are rendered. */
    private float zFar = 1f;  
    /** The distance where fog starts appearing. */
    private float fogNear = 25f; //IMP this dont do nothing yet fog that is
    /** The distance where the fog stops appearing (fully black here) */
    private float fogFar = 33f; //IMP this dont do nothing yet
    /** The color of the fog in rgba. */
    private float fogColorR = 1f, fogColorG = 1f, fogColorB = 1f, fogColorA = 1f;
    
    /** Defines the field of view. */
    private int fov = 80; //IMP this dont do nothing yet

	public Camera(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		init();
	}
	
	public void update(){
		
	}
	
	public void render(){
		glRotatef(rotation.x, 1, 0, 0);
        glRotatef(rotation.y, 0, 1, 0);
        glRotatef(rotation.z, 0, 0, 1);
        glTranslatef(x, y, z);
	}
	
	public void init(){
		glClearColor(fogColorR, fogColorG, fogColorB, fogColorA);
		glFrustum(0, 0, 0, 0, zNear, zFar);
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

	public float getFogNear() {
		return fogNear;
	}

	public void setFogNear(float fogNear) {
		this.fogNear = fogNear;
		init();
	}

	public float getFogFar() {
		return fogFar;
	}

	public void setFogFar(float fogFar) {
		this.fogFar = fogFar;
		init();
	}

	public float getFogColorR() {
		return fogColorR;
	}
	
	public float getFogColorG() {
		return fogColorG;
	}
	
	public float getFogColorB() {
		return fogColorB;
	}
	
	public float getFogColorA() {
		return fogColorA;
	}

	public void setFogColor(float r, float g, float b, float a) {
		fogColorR = r;
		fogColorG = g;
		fogColorB = b;
		fogColorA = a;
		init();
	}

	public int getFov() {
		return fov;
	}

	public void setFov(int fov) {
		this.fov = fov;
		init();
	}

}
