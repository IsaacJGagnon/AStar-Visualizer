import java.awt.Point;
import java.lang.Comparable;

public class Node implements Comparable<Node>{
	private Point location;
	private double fVal;
	private double gVal;
	private Node parent;
	private String moveDir;
	
	Node(int x, int y){
		location = new Point(x,y);
		fVal = 0;
		gVal = 0;
		parent = null;
		moveDir = "";
	}
	
	Node(int x, int y, double f, double g, Node p, String s){
		location = new Point(x,y);
		fVal = f;
		gVal = g;
		parent = p;
		moveDir = s;
	}
	
	public Point getLoc(){
		return location;
	}
	
	public double getF(){
		return fVal;
	}
	
	public double getG(){
		return gVal;
	}
	
	public Node getParent(){
		return parent;
	}
	
	public String getMove(){
		return moveDir;
	}
	
	@Override
	public int compareTo(Node o){
		double fDif = fVal - o.getF();
		if(fDif == 0){
			double gDif = o.getG() - gVal;
			if(gDif > 0){
				return 1;
			} else if(gDif < 0){
				return -1;
			} else {
				return 0;
			}
		} else {
			if(fDif > 0){
				return 1;
			} else if(fDif < 0){
				return -1;
			} else {
				return 0;
			}
		}
	}
}
