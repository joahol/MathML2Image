/*
 * 
 * JH 2020
 * 
 */
import java.util.ArrayList;
import java.util.List;

public class StrokeFactory {
private static boolean VERBOSE = true;
	public StrokeFactory() {}
	public static Stroke generate(String pairs){

		//System.out.println(pairs);
		Stroke s = new Stroke();
		if(pairs.length()>0) {
			
			
				String coma = ",";
				String space =" ";
				String[] tokens = pairs.split(coma);
				for(String token: tokens) {
					//System.out.println("..."+token+"...");
					String ne = token.trim();
					String[] split = ne.split(" ");
					try {
						
						//System.out.println(split[0]+"-"+split[1]);
						int x = Integer.parseInt(split[0]);
						int y = Integer.parseInt(split[1]);
					Point p = new Point(x,y);
					s.addPoint(p);
					//System.out.print("StrokeFactory: "+ p.toString());
					
					}catch(NumberFormatException e) {
						try {
						float fx = Float.parseFloat(split[0]);
						float fy = Float.parseFloat(split[1]);
					Point p = new Point((double)(fx*1000),((double)fy*1000));
					s.addPoint(p);
					if(VERBOSE) {
					System.out.println("StrokeFactory:"+e.getMessage()+ p.toString());
					}
						}catch(Exception a) {
							if(VERBOSE) {
							a.printStackTrace();
							}
						}
						//e.printStackTrace();}
					}
				}
		}
		//System.out.println(s.toString());
		return s;
	}
}
