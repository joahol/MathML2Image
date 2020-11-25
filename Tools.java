import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.File;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class  Tools{

	public static Point buildScalingFactors(Point PMax, Point PMin, int widthRange , int heightRange) {
		double Xd = (PMax.x-PMin.x)/widthRange;
		double Yd = (PMax.y-PMin.y)/heightRange;
		double scale = Max(PMax.x-PMin.x,PMax.y-PMin.y);
		//System.out.println("scale:"+scale+" Xd:"+Xd+" Yd:");
		Point res = new Point(1,1);
		return res;
	}
	public static Point buildMoveToZeroFactors(Point PMax, Point PMin) {
		double xFactor, yFactor;
		xFactor = PMax.x-(PMax.x-PMin.x);
		yFactor = PMax.y-(PMax.y-PMin.y);
		return new Point(xFactor, yFactor);
	}

	public static double Max(double a, double b) {
		if(a>b) {return a;}
		else {return b;}
	}
	public static int Max(int a, int b) {
		if(a>b) {return a;}
		else {return b;}
	}
	public static double Min(double a, double b) {
		if(a<b) {return a;}
		else {return b;}
	}
	public static int Min(int a, int b) {
		if(a<b) {return a;}
		else {return b;}
	}
	
	/*
	 * <param>tMin = target minimum
	 * <param>tMax = target maximum
	 * <param>rMin = measured minimum
	 * <param>rMax = measured maximum 
	 */
	public static double squeezeToRange(double tMin, double tMax, double rMin, double rMax, double meassure) {
		double result=0;
		double t = (meassure-rMin);
		double rRange = (rMax-rMin);
		double tRange = (tMax-tMin);
		result = ((t/rRange)*tRange)+tMin;
		//System.out.println("("+t+"/"+rRange+")"+"*"+tRange+"+"+tMin);
		System.out.println(meassure+ " to "+result);
		return result;
	}
	/*
	 * 
	 * @Param rMin: Minimun of existing range
	 * @Param rMax: Maximum of existing range
	 * @Param tMin: Minimum of target range
	 * @Param tMax: Maximum of target range
	 * @Param measure: fitting value
	 *@Return result: fitted value in desired range.
	 */
	public static double scaleToRange(double tMin, double tMax, double rMin, double rMax,double meassure) {
		double result = 0;
		double r = rMax -rMin;
		double t = tMax - tMin;
		double c = meassure - tMin;
		result = (t *(meassure-rMin)/r)+rMin;
		
		return result;
	}
	public static boolean saveImage(BufferedImage img, String path,String format) {
		File f = new File(path+"."+format); 
		try {
			ImageIO.write(img,format,f);
			
		}catch(Exception e) {
			e.printStackTrace();
		return false;
		}
		return true;
	}
/*
 * Remove the char / from a string
 */
	public static String CleanString(String toClean) {
		String ret="";
		char[] chars = toClean.toCharArray();
		for(int i=0;i<toClean.length();i++) {
			if(chars[i]!=(char)92) {
				ret+=chars[i];
			}
			
		}
		return ret;
	}
	
	public static ArrayList<String> buildFileList(String path, String fileExtension){
		ArrayList filePaths = new ArrayList<String>();
		File files = new File(path);
		if(files.isDirectory()) {
			String fileArray[] = files.list();
			for(String fname: fileArray) {
				File f = new File(path+"/"+fname); 
				if(f.isFile()) {
				String fx = getFileExtension(fname);
				
				if(fx.equals(fileExtension)&& f.isFile()) {
					filePaths.add(path+"/"+fname);
				//	System.out.println("added to parsing:"+path+"/"+fname+" "+f.isFile()+ " "+getFileExtension(fname));
				}
				else {
					//System.out.println("Not added to parsing:"+path+"/"+fname+" "+fileExtension+ " "+fx);
				}
				}
			}
		}
		return filePaths;
	}
	public static String getFileExtension(String filename) {
		int dotIndex = filename.lastIndexOf(".");
		System.out.println(filename);
		String extension = filename.substring(dotIndex);
		return extension;
	}
	
	public static int[] numFilesAndFolders(String path) {
		int folders = 0;
		int files =0;
		File folder = new File(path);
		String[] aFold = folder.list();
		for(String as: aFold) {
			File f= new File(path+"/"+as);
			
			if(!as.equals(".DS_Store")) {
			//	System.out.println(path+"/"+as);
				if(f.isFile()) {files++;}
			else if(f.isDirectory()) {
				String[] content = f.list();
				//assume there is only files in the subfolder!
				files += content.length;
				folders++;
			}
			}
		}
		int[] ret = new int[2];
		ret[1] = folders;
		ret[0] = files;
		return ret;
	}
	
	public static BufferedImage ScaleImage(BufferedImage bim, int sWidth, int sHeight) {
		BufferedImage scaled = new BufferedImage(sWidth, sHeight, BufferedImage.TYPE_BYTE_BINARY);
		AffineTransform aft = AffineTransform.getScaleInstance(sWidth, sHeight);
		Graphics2D gr = scaled.createGraphics();
		gr.drawImage(bim, aft,null);
		gr.drawRenderedImage(bim, aft);
		return scaled;
		
	}
	//Find a factor to move coordinates with a base of center
	public static Point buildMoveToCenterFactor(Point PMax, Point PMin, int Width, int Height) {
		double xFactor, yFactor;
		
		xFactor = (PMax.x-PMin.x)/2;
		yFactor = (PMax.y-PMin.y)/2;
	//	System.out.println("Pmax.x:"+PMax.x+" Pmin.x:"+PMin.x+" pMax.y:"+PMax.y+" PMin:"+PMin.y+ " xFactor: "+xFactor+" yFactor:"+yFactor);
		return new Point(xFactor, yFactor);
		
	}
	public static ArrayList<String> buildDataBaseFilePaths(String dbPath) {
//		System.out.println(dbPath);
		File f = new File(dbPath);
		
		ArrayList<String> paths = new ArrayList<String>();
		String[] base = f.list();
		if(f.isDirectory()) {
			paths.add(dbPath);
		}
		for(String s: base) {
			System.out.println(dbPath+"/"+s);
			File fa = new File(dbPath+"/"+s);
			if(fa.isDirectory()) {
				paths.add(dbPath+"/"+s);
			//	System.out.println(dbPath+"/"+s);
			}
		}
		//if(paths.size()==0) {//Assume that this folder is the root and
							//contain inkml files
	//		paths.add(dbPath);
			
	//	}
	//	System.out.println("Path size:"+paths.size());
		//System.out.println(paths.size());
		return paths;
	}
	static public String getFileNameFromPath(String path) {
		int index = path.lastIndexOf("/");
		String ret = path.substring(index+1);
		return ret;
	}
	
	static public String replaceFileEnding(String filename, String newExtension) {
		int index = filename.lastIndexOf(".");
		String ret = filename.substring(0, index)+newExtension;
		return ret;
	}
	static public double distance(Point p1, Point p2) {
		
		double distance = Math.sqrt(Math.pow(p2.x-p1.x, 2) + Math.pow(p2.y-p1.y ,2));
		return distance;
	}
	
	static public double angle(Point A, Point B) {
		double angle=0;
		double Vx=B.x-A.x;
		double Vy=B.y-A.y;
		double tanT = Vy/Vx;
		angle = Math.atan(tanT);	
		
		return angle;
	}
	static double AbsoluteDifference(double x, double y) {
		double absoulute = java.lang.Math.abs(x)-java.lang.Math.abs(y);
		return java.lang.Math.abs(absoulute);
	}
	
	

}
