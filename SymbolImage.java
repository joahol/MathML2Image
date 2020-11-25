import java.awt.image.BufferedImage;
import java.util.Arrays;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class SymbolImage {

	private int nor =0;

	INDArray pixels;

	private int label=0;
	
	private char charLabel;

	private BufferedImage image;

	private BufferedImage thumb;

	public double xleft, xright, ytop, ybottom;
	/**
	 * 
	 */
	private int prediction=-1;
	/**
	 * 
	 */
	private SymbolMeassure symMeasure;
	/**
	 * 
	 */
	private INDArray grayThumb;
	
	/**
	 * 
	 */
	private static final double[][] EXPECTED_ARRAY = new double[][]{
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 1}
};

	/**
	 * @return
	 */
	public int getPrediction() {
		return prediction;
	}
	/**
	 * @param thum
	 */
	public void setThumb(BufferedImage thum) {
		thumb = thum;
	}

	/**
	 * @param predic
	 */
	public void setPrediction(int predic) {
		this.prediction = predic;
		
	}

	/**
	 * @param x
	 * @param y
	 * @param x2
	 * @param y2
	 * @param biSym
	 * @param grayThumb
	 */
	public SymbolImage(double x, double y, double x2, double y2, BufferedImage biSym, INDArray grayThumb) {
	System.out.println("SymbolImage.Constructor");
		
		this.xleft = x;
		this.xright = x2;
		this.ytop = y;
		this.ybottom = y2;
		this.image = biSym;
		System.out.println("grayThumb magic");
		this.grayThumb = grayThumb;
		System.out.println(grayThumb.length());
		pixels = grayThumb.reshape(new int[]{1, 784});
	}
	
	/**
	 * @param img
	 * @param readByte
	 */
	public SymbolImage(double[] img, byte readByte) {
		pixels = Nd4j.create(img);
		char charLabel = (char)readByte;
	}
	//Normalizes image pixels. Method from Ramo Klevis book
	/**
	 * @param pixel
	 * @return
	 */
	public double[] normalize(double[] pixel) {
		System.out.println("normalize: "+nor+" pixlength:"+pixel.length);
		double[] norm = new double[pixel.length];
		for(int j=0;j<pixel.length;j++) {
			norm[j]=pixel[j]/255d;
		}
		nor++;
		return norm;
	}
	/*
	public double[] getNormalized() {
		return this.normalized;
	}
	*/
	 /**
	 * @param val
	 * @return
	 */
	private char toChar(double val) {
	        return " .:-=+*#%@".charAt(Integer.min((int) (val * 10), 9));
	    }
	 
	/**
	 *
	 */
	@Override
	public String toString() {
	
	    final StringBuilder sb = new StringBuilder();
        String line = " ---------------------------- ";
        sb.append("Label: ").append(label)
                .append("  (").append(Arrays.toString(getLabelAsArray()))
                .append("\n").append(line);
        int cnt = 0;
        for (int r = 0; r < 28; r++) {
            sb.append("\n|");
            for (int c = 0; c < 28; c++) {
                sb.append(toChar(pixels.getInt(cnt)));
                cnt++;
            }
            sb.append("|");
        }

        sb.append("\n").append(line).append("\n");
		return sb.toString();
	}

	public double[] getLabelAsArray() {
	        return EXPECTED_ARRAY[label];
	    }

	public BufferedImage getBufferedImage() {
		return this.image;
		}

	public SymbolMeassure getMeassure() {return this.symMeasure;}
	
	public void setMeassure(SymbolMeassure meassure) {
		this.symMeasure = meassure;}
}
