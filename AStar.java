import java.util.Stack;
import java.util.Scanner;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.lang.Math;
import java.awt.Point;
import javax.swing.JFrame;

public class AStar{
	private int rows, cols, exp, gen, pCost;
	private String valid = "_#@*";
	private int[][] expanded;
	private Point start, end;
	private PriorityQueue<Node> queue;
	private JFrame frame;
	private Visualizer vis;
	private boolean goalReached, update;
	
	private void buildMap(){
		Scanner input = new Scanner(System.in);
		String tmp;
		
		rows = input.nextInt();
		cols = input.nextInt();
		
		expanded = new int[rows][cols];
		
		input.useDelimiter("");
		for(int i = 0; i<rows; i++){
			for(int j = 0; j<cols; j++){
				if(input.hasNext()){
					tmp = input.next();
					
					while(!valid.contains(tmp)){
						tmp = input.next();
					}
					
					if(tmp.equals("@")){
						start = new Point(j,i);
						tmp = "_";
					} else if(tmp.equals("*")) {
						end = new Point(j,i);
						tmp = "_";
					}
					
					if(tmp.equals("_")){
						expanded[i][j] = 0;
					} else {
						expanded[i][j] = 3;
					}
				} else {
					System.err.println("ERROR: Part of map missing");
					System.exit(-1);
				}
			}
		}
	}
	
	private double calcH(int h, int x, int y){
		double dy = Math.abs(y - end.getY());
		double dx = Math.abs(x - end.getX());
		switch(h){
            case 0: return 0;
            case 1: return Math.sqrt((dx*dx) + (dy*dy));
            case 2: return 1 * (dx+dy) + (Math.sqrt(2)-2*1) * Math.min(dx,dy);
            default: return 0;
        }
	}

	private void expandNodes(Node cur, int h){
		int x = (int) cur.getLoc().getX();
		int y = (int) cur.getLoc().getY();
		double D = 1;
		double D2 = Math.sqrt(2);
		double g = cur.getG();
		
		if(!(expanded[y][x] == 2)){
			update = true;
			expanded[y][x] = 2;
			exp++;
		
			if((y-1) >= 0){
				if(expanded[y-1][x] < 2){
					queue.add(new Node(x, y-1, g+D+calcH(h,x,y-1), g+D, cur, "N"));
					expanded[y-1][x] = 1;
					gen++;
				}
				if((x+1) < cols){
					if(expanded[y-1][x+1] < 2){
						queue.add(new Node(x+1, y-1, g+D2+calcH(h,x+1,y-1), g+D2, cur, "NE"));
						expanded[y-1][x+1] = 1;
						gen++;
					}
				}
				if((x-1) >= 0){
					if(expanded[y-1][x-1] < 2){
						queue.add(new Node(x-1, y-1, g+D2+calcH(h,x-1,y-1), g+D2, cur, "NW"));
						expanded[y-1][x-1] = 1;
						gen++;
					}
				}
			}
			if((y+1) < rows){
				if(expanded[y+1][x] < 2){
					queue.add(new Node(x, y+1, g+D+calcH(h,x,y+1), g+D, cur, "S"));
					expanded[y+1][x] = 1;
					gen++;
				}
				if((x+1) < cols){
					if(expanded[y+1][x+1] < 2){
						queue.add(new Node(x+1, y+1, g+D2+calcH(h,x+1,y+1), g+D2, cur, "SE"));
						expanded[y+1][x+1] = 1;
						gen++;
					}
				}
				if((x-1) >= 0){
					if(expanded[y+1][x-1] < 2){
						queue.add(new Node(x-1, y+1, g+D2+calcH(h,x-1,y+1), g+D2, cur, "SW"));
						expanded[y+1][x-1] = 1;
						gen++;
					}
				}
			}
			if((x+1) < cols){
				if(expanded[y][x+1] < 2){
					queue.add(new Node(x+1, y, g+D+calcH(h,x+1,y), g+D, cur, "E"));
					expanded[y][x+1] = 1;
					gen++;
				}
			}
			if((x-1) >= 0){
				if(expanded[y][x-1] < 2){
					queue.add(new Node(x-1, y, g+D+calcH(h,x-1,y), g+D, cur, "W"));
					expanded[y][x-1] = 1;
					gen++;
				}
			}
		}
	}
	
	private void planPath(int h){
		queue = new PriorityQueue<Node>();
		update = false;
		goalReached = false;
		Node cur = new Node((int)start.getX(), (int)start.getY());
		queue.add(cur);
		exp = 0;
		gen = 0;
		vis = new Visualizer(expanded, start, end);
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
			if(cur.getLoc().equals(end)){
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
			Point p = cur.getLoc();
			pCost = 0;
			expanded[(int) p.getY()][(int) p.getX()] = 5;
			while(cur.getParent() != null){
				pCost++;
				cur = cur.getParent();
				p = cur.getLoc();
				expanded[(int) p.getY()][(int) p.getX()] = 4;
			}
			expanded[(int) p.getY()][(int) p.getX()] = 6;
		
			vis.repaint();
		} else {
			System.out.println("Goal unreachable!");
		}
	}
	
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
