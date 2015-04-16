package gorskE.renderdata;

import gorskE.util.load.TextureLoader;

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
	
	/** The array of all the VAOs currently using this texture
	 * 	This is used to determine when a texture can be safely destroyed
	 */
	private ArrayList<VAO> VAOsUsingThisTexture = new ArrayList<VAO>();
	
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
	
	public void destroy(VAO comp) {
		if(VAOsUsingThisTexture.contains(comp)) {
			VAOsUsingThisTexture.remove(comp);
		}
		if(VAOsUsingThisTexture.isEmpty()) {
			GL11.glDeleteTextures(textureID);
			TextureLoader.removeAlreadyLoadedTexture(this);
		}
	}
	
}
