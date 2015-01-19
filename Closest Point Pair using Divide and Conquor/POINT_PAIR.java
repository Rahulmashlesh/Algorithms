
/*
 * Author: Poorn Pragya
 * Created on September 25, 2014
 * This program is used by class CLOSEST PAIR 
 */

public class POINT_PAIR {

	float distance;
	POINT p1,p2;
	
	public POINT_PAIR(POINT p1,POINT p2,float d) {
		this.p1=p1;
		this.p2=p2;
		this.distance=d;
	}
	
	public POINT_PAIR() {
		this.p1=new POINT();
		this.p2=new POINT();
		this.distance=Float.MAX_VALUE;
	}
}
