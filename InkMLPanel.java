import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class InkMLPanel extends JPanel {

	BufferedImage bim;
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		  if(bim != null) {
	        g.drawImage(bim, 0, 0, this);
		  }
		  }
	public void draw(InkMLSymbol im) {
		   int width = 300, height = 300;
	        
	        bim = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	        Graphics2D g2 = bim.createGraphics();
	       try {
	        g2.setColor(Color.WHITE);
	        g2.setPaint(Color.WHITE);
	        g2.setBackground(Color.WHITE);
	        ArrayList<Stroke> st = (ArrayList<Stroke>) im.getStrokes();
	        System.out.println("SS:"+st.size());
	        for(Stroke s: st) {
	        	System.out.println("s");
	        	ArrayList<Point> ps= (ArrayList<Point>) s.getPoints();
	        	System.out.println("Points: "+ps.size());
	        	for(int i=0; i<ps.size()-1;i++) {
	        		Point p1 = ps.get(i);
	        		Point p2 = ps.get(i+1);
	        	
	        		g2.drawLine((int)p1.x,(int)p1.y,(int)p2.x,(int)p2.y);
	        	}
	        }
	       }catch(Exception e) {e.printStackTrace();}
	   
	        
	}
	
}
