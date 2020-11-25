import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;
import org.opencv.core.CvType;


public class Extractor {
private boolean verbose = true;
Size preferedSize = new Size(25,25);
private int paddingSize=2;
public Extractor() {
	
}


//Sort and align symbols in the source mat
public Mat OrderExpression(Mat source, List<MatOfPoint> contours) {
List<Mat> tRes = new ArrayList<Mat>();
	int cols=0, rows=20;
	
	for(int i=0; i< contours.size(); i++) {
		Rect r = Imgproc.boundingRect(contours.get(i));
		Mat m = new Mat(source, r);
		if(m.size()!=preferedSize) {
			m = ScaleMat(preferedSize, m);
		}
		tRes.add(m);
	}
	Mat Result = new Mat(source.size(),source.type());
	Core.hconcat(tRes,Result);
return Result;
}

/*
 * @description
 * Takes Size preferedsize and Mat as parmeters 
 * if the axis are uneven, padding is added before scale operation.
 * @return Mat, scaled and padded in one direction if necessary.
 */
public Mat ScaleMat(Size prefsize, Mat source) {
	Mat result = new Mat();
	Mat anew =source;
	int diff;
	//lite eksperieng som må taes vekk om det ikke fungerer!!! i DAG! 
	//Hvis bredde er mye større enn høyde, må vi legge til tilsvarende på høyde for å få en skikkelig skalering!!!
	System.out.print("source:"+source.size()+" ");
	if(source.width()>source.height()) {
		diff = source.width()-source.height();
		System.out.println("diff:"+diff);
		Mat m = new Mat(new Size(source.width(),diff/2),0);
		
		List<Mat> con = new ArrayList<Mat>();
		con.add(m);
		con.add(source);
		con.add(m);
		//Mat anew = new Mat();
		Core.vconcat(con, anew);
	}else if(source.width()>source.height()) {
		diff = source.height()-source.width();
		System.out.println("diff:"+diff);
		Mat m = new Mat(new Size(source.height(),diff/2),0);
		
		List<Mat> con = new ArrayList<Mat>();
		con.add(m);
		con.add(source);
		con.add(m);
		
		Core.hconcat(con, anew);
	}
		Imgproc.resize(anew, result, prefsize);// denne er ikke interpolert, det finnes en annen version av resize
	System.out.println("new size: "+result.size());
	return result;
}

public List<MatOfPoint> findContours(Mat contourSource){
	List<MatOfPoint> contour = new ArrayList<MatOfPoint>();
	
	Imgproc.GaussianBlur(contourSource, contourSource, new Size(3,3),0);
	Imgproc.adaptiveThreshold(contourSource, contourSource, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,Imgproc.THRESH_BINARY, 75,10);
	org.opencv.core.Core.bitwise_not(contourSource, contourSource);
	Imgproc.findContours(contourSource, contour,new Mat(),  Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
	return contour;
}

public Mat ContourExtraction(Mat frame, List<MatOfPoint> contour) {
		Mat target = new Mat();
		try {
			//contour = FilterContours(contour);
		for(int i=0;i < contour.size();i++) {
			Rect r =  Imgproc.boundingRect(contour.get(i));
			Mat mask = Mat.zeros(frame.size(), org.opencv.core.CvType.CV_8UC1);
			Imgproc.drawContours(mask, contour, i, new Scalar(255), -1);// -1 CV_FILLED or core.FILLED
			
			frame.copyTo(target, mask);
			Imgproc.rectangle(target, r.tl(), r.br(), new Scalar(125));
			if(verbose) {
				System.out.println("Contour:"+i+" Position(x,y) "+r.x+","+r.y+"... H:"+r.height+" W: "+r.width);
			}
		}
		
		}catch(Exception e) {
			if(verbose) {
			toLog("extractor.ContourExtraction-Error:",e.toString());}
	}
		return target;
		}



public Mat addPadding(int pad, Mat toPad) {
	Mat target = new Mat(toPad.rows()+pad, toPad.cols()+pad, toPad.depth());
	Core.copyMakeBorder(toPad, target, pad, pad, pad, pad, Core.BORDER_ISOLATED);
	return target;
}

/*
 * Function should be removed, due to duplicate behaviour and functionality
 * 
 */
public List<Mat> ContourExtractionToListOfMat(Mat frame, List<MatOfPoint> contour, boolean drawSquared,boolean addPadding) {
	List<Mat> newlist = new ArrayList<Mat>();
	try {
		
		//Mat m = new Mat(source, r);
		//contour = FilterContours(contour);
	for(int i=0;i < contour.size();i++) {
		Rect r =  Imgproc.boundingRect(contour.get(i));
		System.out.println(String.valueOf(r.width/r.height));
		Mat mask = Mat.zeros(frame.size(), org.opencv.core.CvType.CV_8UC1);
		Imgproc.drawContours(mask, contour, i, new Scalar(255), -1);// -1 CV_FILLED or core.FILLED
		//Mat target = new Mat(new Size(r.width,r.height),CvType.CV_8UC1);
		Mat target = new Mat(frame, r);
		if(target.size()!=preferedSize) {
			target = ScaleMat(preferedSize, target);
		}
		//frame.copyTo(target, mask);
		if(drawSquared) {
		Imgproc.rectangle(target, r.tl(), r.br(), new Scalar(125));
		}
		//add padding pixels on all sides of mat
		if(addPadding) {
			System.out.print("target width"+target.cols());
			Mat co = new Mat(target.rows()+this.paddingSize, target.cols()+this.paddingSize, target.depth());
		Core.copyMakeBorder(target, co, paddingSize, paddingSize, paddingSize, paddingSize, Core.BORDER_ISOLATED);
		target = co;
		System.out.println("target new width"+target.cols());
		}
		newlist.add(target);
		if(verbose) {
			System.out.println("Contour:"+i+" Position(x,y) "+r.x+","+r.y+"... H:"+r.height+" W: "+r.width);
		}
	}
	
	}catch(Exception e) {
		if(verbose) {
		toLog("extractor.ContourExtraction-Error:",e.toString());}
}
	return newlist;
	}

public List<Mat> ContoursToListOfMat(Mat frame, List<MatOfPoint> contour) {
	List<Mat> target = new ArrayList<Mat>();
	try {
		
	for(int i=0;i < contour.size();i++) {
		Rect r =  Imgproc.boundingRect(contour.get(i));
		Mat t = frame.submat(r);
		target.add(t);
		if(verbose) {
			System.out.println("Extracting "+i+" (x,y) "+r.x+","+r.y+"... H:"+r.height+" W: "+r.width);
		}
	}
	
	}catch(Exception e) {
		if(verbose) {
		toLog("extractor.ContourExtraction-Error:",e.toString());}
}
	return target;
	}
	
	void toLog(String source, String message) {
		System.out.println("Source:"+source+" :"+message);
	}
	
	
	public static BufferedImage Mat2Img(Mat convert) {
		try {
			int type=-1;
		int size = convert.channels() * convert.cols()*convert.rows();
		byte[] buffer = new byte[size]; 
		
		if(convert.channels()>1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}else {
			type = BufferedImage.TYPE_BYTE_GRAY;
		}
		
		BufferedImage bi = new BufferedImage(convert.cols(), convert.rows(), type);
		final byte[] pixels = ((DataBufferByte)bi.getRaster().getDataBuffer()).getData();
		convert.get(0, 0,buffer);
		System.arraycopy(buffer, 0, pixels, 0, buffer.length);
		return bi;
		}catch(Exception e) { 
			System.out.println("Mat2Img"+e.getMessage());
			
		}
		return null;
	}
	
	public static BufferedImage Mat2Image(Mat mat) {
		System.out.println("channels "+mat.channels());
		MatOfByte mob = new MatOfByte();
		Imgcodecs.imencode(".jpg", mat, mob);
		ByteArrayInputStream bais = new ByteArrayInputStream(mob.toArray());
		try {
			return ImageIO.read(bais);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	//go through list of contours and remove items that is not likely a symbol based on size
	public List<MatOfPoint> FilterContours(List<MatOfPoint> contour){
		List<MatOfPoint> newContourList = new ArrayList<MatOfPoint>();
		for(int i=0; i<contour.size();i++) {
			
			if(isContourOfPReferedSize(contour.get(i), 17, 18, 5)) {
					newContourList.add(contour.get(i));
		}
			}
		return newContourList;
	}
	
	//Ettt symbol er passe stort innenfor en hviss variance.. 
	//Dette fungerer ikke helt ett ideen! Glemmer at noen tegn ikke er så brede, eller så lange 
	public boolean isContourOfPReferedSize(MatOfPoint contour, int prefWidth, int prefHeight, int variance) {
		boolean test = true;
		if(contour.width() <= prefWidth+variance && contour.width()>= prefWidth-variance) {test=true;}
		else {test=false;}
		if(test) {
		if(contour.height() <= prefHeight+variance && contour.height()>= prefHeight-variance) {test=true;}
		else {test=false;}
		}
		System.out.println("Size test: "+test+ " pfw+v:"+(prefWidth+variance)+" pfw-v:"+(prefWidth-variance)+"act w:"+contour.width()+ " "+contour.height());
		
		return test;
		
	}
	
	public void setVerbose(boolean Verbose) {
		this.verbose = Verbose;
	}
	
	
}
