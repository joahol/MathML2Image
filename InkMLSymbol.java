import java.util.ArrayList;
import java.util.List;

//import org.junit.Assert;

public class InkMLSymbol {

	public List<Stroke> strokes;
public List<String> refXmlId;// list of referenced strokes:
	public String Label="";
	public String XmlId="";
	public Point bottomRight;
	public Point topLeft;
	public int Height=0;
	public int Width=0;
	public Point centerPoint;
	
	public InkMLSymbol() {
		strokes = new ArrayList<Stroke>();
		refXmlId = new ArrayList<String>();
		bottomRight = new Point();
		topLeft = new Point();
		centerPoint = new Point();
	}
public void setXmlID(String XMLID) {
	this.XmlId = XMLID;
}
public String getXmlId() {
	return this.XmlId;
}


	public List<Stroke> getStrokes() {
		return strokes;
	}

	public void setStrokes(List<Stroke> strokes) {
		this.strokes = strokes;
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(String label) {
		//System.out.println("SetLabel:"+label);
		Label = label;
	}
	
	public void addStroke(Stroke stroke) {
		strokes.add(stroke);
	}
	
	@Override
	public String toString() {
		String res ="";
		for(Stroke s: strokes) {
			res+= s.toString();
		}
		//System.out.println(res);
		return res;
	}
	public Point Max() {
		double x=Double.MIN_VALUE,y=Double.MIN_VALUE;
		for(Stroke s: strokes) {
			for(Point p: s.points) {
				if(p.x > x) {x=p.x;}
				if(p.y > y) {y=p.y;}
			}
		}
		bottomRight =  new Point(x,y);
		return new Point(x,y);
	}
	public Point Min() {
		double x=Double.MAX_VALUE,y=Double.MAX_VALUE;
		for(Stroke s: strokes) {
			for(Point p: s.points) {
				if(p.x < x) {x=p.x;}
				if(p.y < y) {y=p.y;}
			}
		}
		topLeft = new Point(x,y);
		return new Point(x,y);
	}
	public void Scale(Point p) {
		for(Stroke s: strokes) {
			for(Point po: s.points) {
				po.Scale(p);
			}
		}
	}
	public void Scale(double tMax, double tMin, double rMax, double rMin) {
		for(Stroke s: strokes) {
			for(Point po: s.points) {
				po.x = Tools.squeezeToRange(tMin,tMax,rMin,rMax,po.x);
				po.y = Tools.squeezeToRange(tMin,tMax,rMin,rMax,po.y);
			}
		}	
	}
	public void translate(Point tf) {
		for(Stroke s: strokes) {
			for(Point po: s.points) {
				po.TransformMove(tf);
			}
		}	
	}
	public void addReferenceXMLID(String xmlid) {
		this.refXmlId.add(xmlid);
	}
	public List<String> getRefXMLID(){
		return this.refXmlId;
	}
	public Point getBottomRight() {
		return bottomRight;
	}
	public void setBottomRight(Point bottomRight) {
		this.bottomRight = bottomRight;
	}
	public Point getTopLeft() {
		return topLeft;
	}
	public void setTopLeft(Point topLeft) {
		this.topLeft = topLeft;
	}
	public void setHeight() {
		Height = (int) (bottomRight.y-topLeft.y);
	}
	public void setWidth() {
		Width = (int)(bottomRight.x-topLeft.x);
	}
	public void setCenterPoint() {
		double x = bottomRight.x - (Width/2);
		double y = bottomRight.y - (Height/2);
		this.centerPoint.x = x;
		this.centerPoint.y = y;
	//	System.out.println("InkMLSymbol Centerpoint:("+x+","+y+"): bry"+bottomRight.y+"-"+(Height/2)+" tly"+this.topLeft.y+ " rx,lx"+bottomRight.x+ ","+topLeft.x);
	}
	public double getHeight() {
		return (bottomRight.y - topLeft.y); 
	}
	public double getWidth() {
		return (bottomRight.x - topLeft.x); 
	}
	
}
