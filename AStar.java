/**********************************
 * Created by Isaac Gagnon 6/11/2016
 * 
 * The main class of the program which builds the map, and then runs the
 * A* planning algorithm, or Dijkstras if given 0 as a heuristic.
 * 
 * Last updated 6/18/2016
 * v2.0: updated to handle multiple goals instead of one
 **********************************/

import java.util.Stack;
import java.util.Scanner;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.lang.Math;
import java.awt.Point;
import javax.swing.JFrame;

public class AStar{
	private int rows, cols, exp, gen, pCost, goalCount;
	private String valid = "_#@*";
	private Tile[][] expanded;
	private Point start;
	private ArrayList<Point> goals;
	private PriorityQueue<Node> queue;
	private JFrame frame;
	private Visualizer vis;
	private boolean goalReached, update;
	
	/**
	 * Scans the input file character by character and builds a map
	 * based on it. The @ symbol is the start and is saved as so when 
	 * found. Each asterisk (*) is a goal, and is saved as such when 
	 * reached. Underscores (_) are empty spaces, pound signs (#) are
	 * blocked spaces.
	 **/
	private void buildMap(){
		Scanner input = new Scanner(System.in);
		String tmp;
		
		rows = input.nextInt();
		cols = input.nextInt();
		
		goals = new ArrayList<Point>();
		expanded = new Tile[rows][cols];
		goalCount = 0;
		
		input.useDelimiter("");
		for(int i = 0; i<rows; i++){
			for(int j = 0; j<cols; j++){
				if(input.hasNext()){
					tmp = input.next();
					
					while(!valid.contains(tmp)){
						tmp = input.next();
					}
					
					if(tmp.equals("@")){
						expanded[i][j] = new Tile(0, "@");
						start = new Point(j,i);
					} else if(tmp.equals("*")) {
						expanded[i][j] = new Tile(0, "*");
						goals.add(new Point(j,i));
						goalCount++;
					} else if(tmp.equals("_")){
						expanded[i][j] = new Tile(0, "_");
					} else {
						expanded[i][j] = new Tile(3, "#");
					}
				} else {
					System.err.println("ERROR: Part of map missing");
					System.exit(-1);
				}
			}
		}
	}
	
	/**
	 * Removes the specified goal point from the ArrayList of goals that
	 * still haven't been reached.
	 **/
	private void deleteGoal(Point p){
		int i = 0;
		for(i = 0; i < goals.size(); i++){
			if(goals.get(i).equals(p)){
				break;
			}
		}
		goals.remove(i);
	}
	
	/**
	 * Returns the goal point that is closest to the node being expanded
	 **/
	private Point cDif(int x, int y){
		int closest = 0;
		Point curP = goals.get(0);
		double diff = Math.abs(x - curP.getX()) + Math.abs(y - curP.getY());
		double curDiff;
		
		for(int i = 0; i<goals.size(); i++){
			curP = goals.get(i);
			if(expanded[(int) curP.getY()][(int) curP.getX()].getIVal() < 2){
				curDiff = Math.abs(x - curP.getX()) + Math.abs(y - curP.getY());
				if(curDiff < diff){
					diff = curDiff;
					closest = i;
				}
			}
		}
		
		return goals.get(closest);
	}
	
	/**
	 * Calculates the h value to the closest goal based on the 
	 * designated heuristic
	 **/
	private double calcH(int h, int x, int y){
		Point p = cDif(x,y);
		double dy = Math.abs(y - p.getY());
		double dx = Math.abs(x - p.getX());
		switch(h){
            case 0: return 0;
            case 1: return Math.sqrt((dx*dx) + (dy*dy));
            case 2: return 1 * (dx+dy) + (Math.sqrt(2)-2*1) * Math.min(dx,dy);
            default: return 0;
        }
	}

	/**
	 * Returns the number of goals reached by the given node. If the
	 * node being expanded is a goal node, update the goal count by one,
	 * else return the goal count unchanged.
	 **/
	private int getG(int x, int y, int g){
		if(expanded[y][x].getSVal().equals("*")){
			return g+1;
		} else {
			return g;
		}
	}

	/**
	 * Generates all eight possible nodes from the given location.
	 * If the node is closed or a wall, it is not generated.
	 **/
	private void expandNodes(Node cur, int h){
		int x = (int) cur.getLoc().getX();
		int y = (int) cur.getLoc().getY();
		int gVis = cur.getVisited();
		double D = 1;
		double D2 = Math.sqrt(2);
		double g = cur.getG();
		
		if(!(expanded[y][x].getIVal() == 2)){
			if(expanded[y][x].getSVal().equals("*")){
				paintPath(cur);
				resetMap();
				queue.clear();
				deleteGoal(cur.getLoc());
			}
			update = true;
			expanded[y][x].setIVal(2);
			exp++;
		
			if((y-1) >= 0){
				if(expanded[y-1][x].getIVal() < 2){
					queue.add(new Node(x, y-1, getG(x, y-1, gVis), g+D+calcH(h,x,y-1), g+D, cur));
					expanded[y-1][x].setIVal(1);
					gen++;
				}
				if((x+1) < cols){
					if(expanded[y-1][x+1].getIVal() < 2){
						queue.add(new Node(x+1, y-1, getG(x+1, y-1, gVis), g+D2+calcH(h,x+1,y-1), g+D2, cur));
						expanded[y-1][x+1].setIVal(1);
						gen++;
					}
				}
				if((x-1) >= 0){
					if(expanded[y-1][x-1].getIVal() < 2){
						queue.add(new Node(x-1, y-1, getG(x-1, y-1, gVis), g+D2+calcH(h,x-1,y-1), g+D2, cur));
						expanded[y-1][x-1].setIVal(1);
						gen++;
					}
				}
			}
			if((y+1) < rows){
				if(expanded[y+1][x].getIVal() < 2){
					queue.add(new Node(x, y+1, getG(x, y+1, gVis), g+D+calcH(h,x,y+1), g+D, cur));
					expanded[y+1][x].setIVal(1);
					gen++;
				}
				if((x+1) < cols){
					if(expanded[y+1][x+1].getIVal() < 2){
						queue.add(new Node(x+1, y+1, getG(x+1, y+1, gVis), g+D2+calcH(h,x+1,y+1), g+D2, cur));
						expanded[y+1][x+1].setIVal(1);
						gen++;
					}
				}
				if((x-1) >= 0){
					if(expanded[y+1][x-1].getIVal() < 2){
						queue.add(new Node(x-1, y+1, getG(x-1, y+1, gVis), g+D2+calcH(h,x-1,y+1), g+D2, cur));
						expanded[y+1][x-1].setIVal(1);
						gen++;
					}
				}
			}
			if((x+1) < cols){
				if(expanded[y][x+1].getIVal() < 2){
					queue.add(new Node(x+1, y, getG(x+1, y, gVis), g+D+calcH(h,x+1,y), g+D, cur));
					expanded[y][x+1].setIVal(1);
					gen++;
				}
			}
			if((x-1) >= 0){
				if(expanded[y][x-1].getIVal() < 2){
					queue.add(new Node(x-1, y, getG(x-1, y, gVis), g+D+calcH(h,x-1,y), g+D, cur));
					expanded[y][x-1].setIVal(1);
					gen++;
				}
			}
		}
	}
	
	/**
	 * Resets all tiles that are not the start, a goal, a wall, or part
	 * of the least cost path to their original non-generated value.
	 **/
	private void resetMap(){
		for(Tile[] r: expanded){
			for(Tile t: r){
				if(t.getIVal() < 3){
					t.setIVal(0);
				}
			}
		}
	}
	
	/**
	 * Iterates back through the path from the goal node, setting the
	 * paint color value for the least cost path. Then repaints it.
	 **/
	private void paintPath(Node c){
		Node cur = c;
		Point p = cur.getLoc();
			pCost = 0;
			while(cur.getParent() != null){
				pCost++;
				cur = cur.getParent();
				p = cur.getLoc();
				expanded[(int) p.getY()][(int) p.getX()].setIVal(4);
			}
		
			vis.repaint();
	}
	
	/**
	 * Runs the actual A* planning algorithm
	 **/
	private void planPath(int h){
		queue = new PriorityQueue<Node>();
		update = false;
		goalReached = false;
		Node cur = new Node((int)start.getX(), (int)start.getY());
		queue.add(cur);
		exp = 0;
		gen = 0;
		vis = new Visualizer(expanded);
		frame = new JFrame();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(vis);
		frame.setSize(expanded[0].length*4 + 16,expanded.length*4 + 39);
		frame.setVisible(true);
		
		synchronized(expanded){
			try{
				expanded.wait();
			} catch(InterruptedException e){
				System.err.println(e);
				System.exit(-1);
			}
		}
		
		while(queue.size() != 0){
			cur = queue.remove();
			if(cur.getVisited() == goalCount){
				goalReached = true;
				break;
			}else{
				expandNodes(cur, h);
				
				if(update){
					update = false;
					vis.repaint();
					synchronized(expanded){
						try{
							expanded.wait();
						} catch(InterruptedException e){
							System.err.println(e);
							System.exit(-1);
						}
					}
				}
			}
		}
		
		if(goalReached){
			paintPath(cur);
		} else {
			System.out.println("Goal unreachable!");
		}
	}
	
	/**
	 * Initializes the AStar program and sets everything in motion
	 **/
	public static void main(String[] args){
		if(args.length != 1){
			System.err.println("java AStar [heuristic] < [file]");
			System.exit(-1);
		}
		
		AStar as = new AStar();
		as.buildMap();
		as.queue = new PriorityQueue<Node>();
		
		as.planPath(Integer.parseInt(args[0]));
		
		if(as.goalReached){
			System.out.println("Path Cost: " + as.pCost);
		}
		System.out.println("Expanded: " + as.exp);
		System.out.println("Generated: " + as.gen);
	}
}
