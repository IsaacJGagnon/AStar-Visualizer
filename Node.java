/**********************************
 * Created by Isaac Gagnon 6/11/2016
 * 
 * Nodes represent the generated movement possibilities. These are put
 * into a priority queue and expanded one by one until the goal is
 * reached.
 * 
 * Last updated 6/18/2016
 **********************************/

import java.awt.Point;
import java.lang.Comparable;

public class Node implements Comparable<Node>{
	private Point location;
	private double fVal;
	private double gVal;
	private Node parent;
	private int gVisited;
	
	Node(int x, int y){
		location = new Point(x,y);
		fVal = 0;
		gVal = 0;
		parent = null;
		gVisited = 0;
	}
	
	Node(int x, int y, int c, double f, double g, Node p){
		location = new Point(x,y);
		fVal = f;
		gVal = g;
		parent = p;
		gVisited = c;
	}
	
	/**
	 * Returns the "location" of the node as a Point
	 **/
	public Point getLoc(){
		return location;
	}
	
	/**
	 * Returns the number of goals reached by this point in the path
	 **/
	public int getVisited(){
		return gVisited;
	}
	
	/**
	 * Returns the f value of the node. f(n) = g(n) + h(n) where g is
	 * the cost to get to the current node, and h is the heuristic
	 * estimate to get to the goal.
	 **/
	public double getF(){
		return fVal;
	}
	
	/**
	 * Returns the g value of the node, where g is the cost to get to
	 * the current node.
	 **/
	public double getG(){
		return gVal;
	}
	
	/**
	 * Returns the node that expanded this node. Used to color the path
	 * after the goal has been reached.
	 **/
	public Node getParent(){
		return parent;
	}
	
	/**
	 * Compares this node to another node for precedence. The node with
	 * more goals visited is given precedence. Then the node with the
	 * lower f value. If two nodes have the same f value, the node with 
	 * the higher g value is given precedence.
	 **/
	@Override
	public int compareTo(Node o){
		int visDif = o.getVisited() - gVisited;
		if(visDif == 0){
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
		} else {
			return visDif;
		}
	}
}
