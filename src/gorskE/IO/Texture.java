package gorskE.IO;

import gorskE.IO.load.TextureLoader;
import gorskE.gameobject.component.Component;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

public class Texture {
	
	/** The GL target type */
	private int	target;

	/** The GL texture ID */
	private int	textureID;

	/** The height of the texture (in pixels)*/
	private int height;

	/** The width of the texture (in pixels)*/
	private int width;
	
	/** The array of all the components currently using this texture
	 * 	This is used to determine when a texture can be safely destroyed
	 */
	private ArrayList<Component> componentsUsingThisTexture = new ArrayList<Component>();
	
	public Texture(int target, int textureID, int height, int width){
		this.target = target;
		this.textureID = textureID;
		this.height = height;
		this.width = width;
	}

	public int getTarget() {
		return target;
	}

	public int getTextureID() {
		return textureID;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	public void destroy(Component comp) {
		if(componentsUsingThisTexture.contains(comp)) {
			componentsUsingThisTexture.remove(comp);
		}
		if(componentsUsingThisTexture.isEmpty()) {
			GL11.glDeleteTextures(textureID);
			TextureLoader.removeAlreadyLoadedTexture(this);
		}
	}
	
}
