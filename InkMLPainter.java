import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class InkMLPainter extends JFrame
{
	
	private static final long serialVersionUID = 1L;
	static InkComponent drawPanel;
	static JFrame drawFrame;
	static Graphics2D g2d;

	public InkMLPainter() throws Exception {		
	}
	
	public static void init() {
		
		
	}
	public static void main(String[] args){
		try {
		InkMLParser imlp = new InkMLParser();
		imlp.init();
		List<InkMLSymbol> syms = imlp.getSymbols();
		drawFrame = new JFrame();
		drawPanel = new InkComponent(syms);
		drawPanel.setSize(1000, 1000);
		drawFrame.setSize(1000, 1000);
		drawFrame.add(drawPanel);
		drawFrame.pack();
		drawFrame.setVisible(true);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
