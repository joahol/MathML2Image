import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import java.util.List;

import javax.swing.BorderFactory;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class InkComponent extends JPanel{

	private Graphics2D graphic;
	private int x,y, xold,yold;
	private Image img;
	private BasicStroke bs = new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER); 
	List<InkMLSymbol> inks;
	
	public InkComponent(List<InkMLSymbol> imk) {
		setDoubleBuffered(false);
		Font impact = new Font("Tahoma", Font.PLAIN, 14);
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Write",TitledBorder.LEFT,TitledBorder.TOP,impact, Color.BLUE));		
		inks = imk;
	}
	public void clear() {
		//System.out.println("Clear area: "+getSize().width);
		graphic.setPaint(Color.white);
		graphic.fillRect(0, 0, getSize().width, getSize().height);
		graphic.setPaint(Color.BLACK);
		repaint();
	}
	
	public void setImage(Image image) {
		img = image;
	}
	
	protected void paintComponent(Graphics g) {	
	super.paintComponent(g);
		if(img==null) {
			System.out.println("creating graphics");
			img = createImage(getSize().width, getSize().height);
			graphic = ((Graphics2D)img.getGraphics());
			graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);	
			clear();
			
		}
		if(inks!=null) {
			
			System.out.println("something is in there");
			for(InkMLSymbol is: inks) {
			List<Stroke> strokes = is.getStrokes();
			Graphics2D g2= (Graphics2D)g;
			g2.setStroke(new BasicStroke(5));
			for(Stroke s : strokes) {
				double[] x =s.XpointsAsVec();
				double[] y = s.YpointsAsVec();
				for(int i=0; i<x.length-1;i++) {
					g2.drawLine((int)x[i], (int)y[i], (int)x[i+1], (int)y[i+1]);
				}

			}
			}
		}if(inks==null) {
		
	
		}
	
		
	}
	
	public Image getImage() {
		return img;
	}
	
	public void setINKSymbol(List<InkMLSymbol> imks) {
		inks = imks;
	}


}
