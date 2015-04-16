package gorskE.util.load;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import gorskE.GorskE;
import gorskE.renderdata.Texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class TextureLoader {

	private static HashMap<String, Texture> alreadyLoadedTextures = new HashMap<String, Texture>();

	public static Texture loadTexture(String filename) {
		if(!alreadyLoadedTextures.containsKey(filename)) {
			ByteBuffer buf = null;
			int tWidth = 0;
			int tHeight = 0;
			
			try {
				// Open the PNG file as an InputStream
				InputStream in = new FileInputStream(filename);
				// Link the PNG decoder to this stream
				PNGDecoder decoder = new PNGDecoder(in);
				
				// Get the width and height of the texture
				tWidth = decoder.getWidth();
				tHeight = decoder.getHeight();
				
				
				// Decode the PNG file in a ByteBuffer
				buf = ByteBuffer.allocateDirect(
						4 * decoder.getWidth() * decoder.getHeight());
				decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
				buf.flip();
				
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
			// Create a new texture object in memory and bind it
			int texId = GL11.glGenTextures();
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
			
			// All RGB bytes are aligned to each other and each component is 1 byte
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			
			// Upload the texture data and generate mip maps (for scaling)
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, tWidth, tHeight, 0, 
					GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
			//GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			
			// Setup the ST coordinate system
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			
			// Setup what to do when the texture has to be scaled
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, 
					GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, 
					GL11.GL_LINEAR_MIPMAP_LINEAR);
			
			
			Texture texture = new Texture(GL11.GL_TEXTURE_2D,texId, tWidth, tHeight);
			alreadyLoadedTextures.put(filename, texture); //make the texture and put it into the list of already made textures
			
			
			return texture;
		}else {
			return alreadyLoadedTextures.get(filename);
		}
	}
	
	public static void removeAlreadyLoadedTexture(Texture texture) {
		if(alreadyLoadedTextures.containsValue(texture)){
			Iterator<HashMap.Entry<String, Texture>> iterator = alreadyLoadedTextures.entrySet().iterator();
			while(iterator.hasNext()) {
				Map.Entry<String, Texture> entry = iterator.next();
				if(entry.getValue().equals(texture)) {
					alreadyLoadedTextures.remove(entry.getKey());
				}
			}
		}
	}

}
