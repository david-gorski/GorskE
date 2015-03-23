package gorskE.IO.load;

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
import gorskE.IO.Texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

public class TextureLoader {
	
	 private static final int BYTES_PER_PIXEL = 4;//3 for RGB, 4 for RGBA
	    
	    public static Texture loadTextureDirect(BufferedImage image){

	        int[] pixels = new int[image.getWidth() * image.getHeight()];
	          image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

	          ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB

	          for(int y = 0; y < image.getHeight(); y++){
	              for(int x = 0; x < image.getWidth(); x++){
	                  int pixel = pixels[y * image.getWidth() + x];
	                  buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
	                  buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
	                  buffer.put((byte) (pixel & 0xFF));               // Blue component
	                  buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
	              }
	          }

	          buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS

	          // You now have a ByteBuffer filled with the color data of each pixel.
	          // Now just create a texture ID and bind it. Then you can load it using 
	          // whatever OpenGL method you want, for example:

	        int textureID = glGenTextures(); //Generate texture ID
	          glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID

	          //Setup wrap mode
	          glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
	          glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

	          //Setup texture scaling filtering
	          glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	          glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

	          //Send texel data to OpenGL
	          glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

	          //Return the texture ID so we can bind it later again
	        return new Texture(GL_TEXTURE_2D, textureID, image.getWidth(), image.getHeight());
	     }
	       
	       public static Texture loadSubTexture(String loc,int x, int y, int height, int width){
	    	   BufferedImage image = loadImage(loc); //loads the buffered image from the dataPath "loc"
	    	   return loadSubTexture(image, x, y, height, width);
	       }
	       
	       /**
	        * Special method for loading a texture that is square from inside a larger image, ie 32x32 tile from tilesheet
	        */
	       public static Texture loadSubTexture(BufferedImage image, int x, int y, int heightt, int widthh){
	    	   int width = widthh;
	    	   int height = heightt;
	    	   
	           int[] pixels = new int[image.getWidth() * image.getHeight()]; //creates the array of pixels for the specific tile according to tileSize x tileSize
	             image.getRGB(x, y, width, height, pixels, 0, width); //adds the rgba values of the image to the pixels array

	             ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB
	            
	             for(int Ycount = y; Ycount < height+y; Ycount++){
	                 for(int Xcount = x; Xcount < width+x; Xcount++){
	                    // int pixel = pixels[Ycount * width + Xcount];
	                	 int pixel = pixels[(Ycount-y)*width + (Xcount-x)]; 
	                     buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
	                     buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
	                     buffer.put((byte) (pixel & 0xFF));               // Blue component
	                     buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
	                     }
	                 }
	             
	             buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS, flips the buffer because opengl uses flipped buffer

	             // You now have a ByteBuffer filled with the color data of each pixel.
	             // Now just create a texture ID and bind it. Then you can load it using 
	             // whatever OpenGL method you want, for example:

	           int textureID = glGenTextures(); //Generate texture ID
	             glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID

	             //Setup wrap mode
	             glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
	             glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

	             //Setup texture scaling filtering
	             glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	             glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

	             //Send texel data to OpenGL
	             glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

	             //Return the texture ID so we can bind it later again
	           return new Texture(GL_TEXTURE_2D, textureID, width, height);
	       }
	       
	       public static Texture loadTextureDirect(String loc){
	    	   return loadTextureDirect(loadImage(loc));
	       }
	       
	      
	       public static BufferedImage loadImage(String loc)
	       {
	            try {
	               BufferedImage img = ImageIO.read(new File(loc));
	               return img;
	            } catch (Exception e) {
	                System.out.println("Failed to ImageIO.read(), inside of TextureLoader");
	                e.printStackTrace();
	                return null;
	            }
	       }

}
