
public class SymbolMeassure implements AlignmentComparator{
	public SymbolMeassure() {
		height =0;
		width=0;
		centerX=0;
		centerY=0;
	}
	public SymbolMeassure(double xleft, double ybottom,double xright,double ytop) {
	xLeft = xleft;
	yBottom = ybottom;
	xRight = xright;
	yTop = ytop;
	}
	public double getHeight() {
		return height;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getCenterX() {
		return centerX;
	}

	public void setCenterX(double centerX) {
		this.centerX = centerX;
	}

	public double getCenterY() {
		return centerY;
	}

	public void setCenterY(double centerY) {
		this.centerY = centerY;
	}
	@Override
	public String toString() {
		return "Width:"+width+" Height:"+height+" CenterX:"+centerX+" CenterY:"+centerY;
		
	}

	public double height, width, centerX, centerY;
	public double xLeft, yBottom, xRight, yTop;
	@Override
	
	public int compareTo(SymbolImage si) {
		SymbolMeassure sm = si.getMeassure();
		
		if(sm.yBottom>= yTop) { // the compared symbol is above
			// two options above or power
			if(sm.centerX < xLeft && sm.centerX > xRight) {
				return Alignment.ABOVE;
			}
			else if(sm.centerX > xLeft) {}
		}
		return 0;
	}
}
