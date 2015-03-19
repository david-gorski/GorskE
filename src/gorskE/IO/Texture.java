package gorskE.IO;

public class Texture {
	
	/** The GL target type */
	private int	target;

	/** The GL texture ID */
	private int	textureID;

	/** The height of the texture (in pixels)*/
	private int height;

	/** The width of the texture (in pixels)*/
	private int width;
	
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
	
}
