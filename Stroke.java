import java.util.ArrayList;
import java.util.List;

public class Stroke {
	List<Point> points;

public Stroke() {
	points = new ArrayList<Point>();
}

public void addPoint(Point p) {
	points.add(p);
}

public List<Point> getPoints(){
	return points;
}
@Override
public String toString(){
	String res="";
	for(Point p: points) {
		res += p.toString()+"\n";
	}
	return res;
}

public double[] XpointsAsVec(){
double[] lpoints = new double[points.size()];
for(int i=0;i<points.size();i++) {
	lpoints[i]=points.get(i).getX();
}
return lpoints;
}
public double[] YpointsAsVec(){
double[] lpoints = new double[points.size()];
for(int i=0;i<points.size();i++) {
	lpoints[i]=points.get(i).getY();
}
return lpoints;
}
}
