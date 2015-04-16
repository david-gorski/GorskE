package gorskE.input;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import gorskE.GorskE;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * This is the input handler for the entire game engine.
 * All getting of input should be done through this class statically
 * @author David
 *
 */
public class Input extends GLFWKeyCallback{
	
	private static int forward = GLFW.GLFW_KEY_UP, backward = GLFW.GLFW_KEY_DOWN, left = GLFW.GLFW_KEY_LEFT, right = GLFW.GLFW_KEY_RIGHT, up  = GLFW.GLFW_KEY_U, down  = GLFW.GLFW_KEY_D;
	private static int space = GLFW.GLFW_KEY_SPACE;
	private static int action1 = GLFW.GLFW_KEY_1, action2 = GLFW.GLFW_KEY_2, action3 = GLFW.GLFW_KEY_3, action4 = GLFW.GLFW_KEY_4;
	
    // a boolean array of all our keys.
    public static boolean[] keys = new boolean[65535];
 
    // Overrides GLFW's own implementation of the Invoke method
    // This gets called everytime a key is pressed.
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
    	if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) { //if the key pressed was one that should close the window 
            glfwSetWindowShouldClose(window, GL_TRUE); // We will detect this in our rendering loop
    	}else {
        keys[key] = action != GLFW.GLFW_RELEASE;
    	}
    }
 
    public static boolean isKeyDown(int keycode) {
        return keys[keycode];
    }
 
    public static boolean isKeyUp(int keycode){
        return keys[keycode];
    }
	public static int getUp() {
		return up;
	}

	public static void setUp(int up) {
		Input.up = up;
	}

	public static int getDown() {
		return down;
	}

	public static void setDown(int down) {
		Input.down = down;
	}

	public static int getLeft() {
		return left;
	}

	public static void setLeft(int left) {
		Input.left = left;
	}

	public static int getRight() {
		return right;
	}

	public static void setRight(int right) {
		Input.right = right;
	}

	public static int getSpace() {
		return space;
	}

	public static void setSpace(int space) {
		Input.space = space;
	}

	public static int getAction1() {
		return action1;
	}

	public static void setAction1(int action1) {
		Input.action1 = action1;
	}

	public static int getAction2() {
		return action2;
	}

	public static void setAction2(int action2) {
		Input.action2 = action2;
	}

	public static int getAction3() {
		return action3;
	}

	public static void setAction3(int action3) {
		Input.action3 = action3;
	}

	public static int getAction4() {
		return action4;
	}

	public static void setAction4(int action4) {
		Input.action4 = action4;
	}

	public static int getForward() {
		return forward;
	}

	public static void setForward(int forward) {
		Input.forward = forward;
	}

	public static int getBackward() {
		return backward;
	}

	public static void setBackward(int backward) {
		Input.backward = backward;
	}

}
