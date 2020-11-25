
public class Point {
public double x;
public double y;
public boolean verbose = false;

public Point(double x,double y) {
	this.x = x;
	this.y = y;
}

public Point() {
	// TODO Auto-generated constructor stub
}

public double getX() {
	return x;
}

public void setX(double x) {
	this.x = x;
}

public double getY() {
	return y;
}

public void setY(double y) {
	this.y = y;
}
@Override
public String toString() {
	return x+","+y;
}
public void Scale(Point scale) {
	x = x/scale.x;
	y = y/scale.y;
	if(verbose) {
	System.out.println(x+","+y);
	}
	}
public void TransformMove(Point transform) {
	String s = "tpoint:"+transform.toString()+" move "+x+","+y;
	
	x = x-transform.x;
	y = y-transform.y;
	if(verbose) {
	System.out.print(s+" to "+x+","+y+"\n");
}
	}
public void convertCoordToIntValue() {
	this.x = (double)((int)x);
	this.y = (double)((int)y);
}
}
