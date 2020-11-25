import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.mortennobel.imagescaling.ResampleFilters;
import com.mortennobel.imagescaling.ResampleOp;
import org.opencv.core.Scalar;

public class ImageTools {
//
public static int TARGET_WIDTH=28;
public static int TARGET_HEIGHT=28;
public static Scalar BLACK = new Scalar(255,255,255);
public static boolean verbose = true;
public static boolean dilate = false;


public static double[] RGBToGrayScale(BufferedImage img, int size) {
	double[] imageGray = new double[size * size];
	
    int w = img.getWidth();
    int h = img.getHeight();
    int index = 0;
    for (int i = 0; i < w; i++) {
        for (int j = 0; j < h; j++) {
            Color color = new Color(img.getRGB(j, i), true);
            int red = (color.getRed());
            int green = (color.getGreen());
            int blue = (color.getBlue());
           
            double v = 255 - (red + green + blue) / 3d;
            imageGray[index] = v;
            index++;
        }
    }
    return imageGray;
}
/*
@Param Image
@Return BufferedImage with transparency
*/ 
public static BufferedImage toBufferedImage(java.awt.Image img) {
	BufferedImage bimg = new BufferedImage(img.getWidth(null), img.getHeight(null),BufferedImage.TYPE_BYTE_GRAY);
	Graphics2D graph = bimg.createGraphics();
	graph.drawImage(img, 0,0,null);
	
	graph.dispose();
	return bimg;
}

/*
 @Param BufferedImage
 */
public static BufferedImage ImageScale(BufferedImage img) {
	ResampleOp resizeOp = new ResampleOp(TARGET_WIDTH, TARGET_HEIGHT);
    resizeOp.setFilter(ResampleFilters.getLanczos3Filter());
    return resizeOp.filter(img, null);
}

public static BufferedImage Scale(BufferedImage img, int width, int height) {
	BufferedImage ret  = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
	//Image temp = ret.getScaledInstance(width,height,Image.SCALE_SMOOTH);
	Graphics2D gr = ret.createGraphics();
	
	gr.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    gr.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
    gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	
	//gr.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
//		    RenderingHints.VALUE_STROKE_DEFAULT);
	//gr.drawImage(img,0,0,width,height,img.getWidth(), img.getHeight(),height, height, null);
	gr.drawImage(img, 0, 0, width, height, 0, 0, img.getWidth(),
		    img.getHeight(), null);
	gr.dispose();
	return ret;
}

public static boolean SaveBufferedImageToJpg(String path, BufferedImage img) {
	File output = new File(path+"img.jpg");
	try {
		ImageIO.write(img,"jpg",output);
	} catch (IOException e) {		
		e.printStackTrace();
		return false;
	}
	//System.out.println(path);
	return true;
}
public static boolean SaveBufferedImageToJpg(String path, BufferedImage img, boolean pathNeedExtension) {
	File output = null;
	if(pathNeedExtension) {
	output = new File(path+"img.jpg");}
	else {
		output = new File(path);
	}
	try {
		ImageIO.write(img,"jpg",output);
	} catch (IOException e) {		
		e.printStackTrace();
		return false;
	}
	//System.out.println(path);
	return true;
}

public static BufferedImage LoadImage(String path) {
	BufferedImage ret = null;
	try {
	ret = ImageIO.read(new File(path));
	}catch(Exception e) {e.printStackTrace();}
	return ret;
}

}


