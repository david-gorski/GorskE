package gorskE.camera;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import gorskE.util.math.Vector3f;

public abstract class Camera{
	
	/**
	 * The position of the camera in space
	 */
	protected float x=0, y=0, z=0;
	
	private float oldX=0, oldY=0, oldZ=0;
    
    /**
     * The rotation of the axis. The X component stands for the rotation along the x-axis,
     * where 0 is dead ahead, 180 is backwards, and 360 is automically set to 0 (dead ahead). The value must be between
     * (including) 0 and 360. The Y component stands for the rotation along the y-axis, where 0 is looking straight
     * ahead, -90 is straight up, and 90 is straight down. The value must be between (including) -90 and 90.
     */
	protected Vector3f rotation = new Vector3f(0, 0, 0);
    
    /** The minimal distance from the camera where objects are rendered. */
	protected float zNear = 0.00001f;
    /** The maximal distance from the camera where objects are rendered. */
	protected float zFar = 1f;  
    /** The distance where fog starts appearing. */
	protected float fogNear = 1f; //IMP this dont do nothing yet fog that is
    /** The distance where the fog stops appearing (fully black here) */
	protected float fogFar = 33f; //IMP this dont do nothing yet
    /** The color of the fog in rgba. */
	protected float fogColorR = 1f, fogColorG = 1f, fogColorB = 1f, fogColorA = 1f;
    
    /** Defines the field of view. */
	protected int fov = 80; //IMP this dont do nothing yet

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
        glTranslatef(x-oldX, y-oldY, z-oldZ);
        oldX=x; oldY=y; oldZ=z;
	}
	
	public void init(){
		glFrustum(0, 0, 0, 0, zNear, zFar);
		
		glEnable(GL_FOG);
		FloatBuffer fogColours = BufferUtils.createFloatBuffer(4);
        fogColours.put(new float[]{fogColorR, fogColorG, fogColorB, fogColorA});
        glClearColor(fogColorR, fogColorG, fogColorB, fogColorA);
        fogColours.flip();
        glFogfv(GL_FOG_COLOR, fogColours);
        glFogi(GL_FOG_MODE, GL_LINEAR);
        glHint(GL_FOG_HINT, GL_NICEST);
        glFogf(GL_FOG_START, fogNear);
        glFogf(GL_FOG_END, fogFar);
        glFogf(GL_FOG_DENSITY, 0.005f);
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
