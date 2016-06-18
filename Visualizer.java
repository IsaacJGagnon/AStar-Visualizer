import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;

public class Visualizer extends JPanel{
	private int[][] map;
	private Point start;
	private Point end;
	
	Visualizer(int[][] m, Point s, Point e){
		super();
		setBackground(Color.white);
		map = m;
		start = s;
		end = e;
	}
	
	private void setColor(Graphics g, int n){
		switch(n){
			case 0: g.setColor(Color.gray);
				break;
			case 1: g.setColor(Color.yellow);
				break;
			case 2: g.setColor(Color.green);
				break;
			case 3: g.setColor(Color.black);
				break;
			case 4: g.setColor(Color.magenta);
				break;
			case 5: g.setColor(Color.red);
				break;
			case 6: g.setColor(Color.blue);
				break;
			default: g.setColor(Color.gray);
		}
	}
	
	protected void paintComponent(Graphics g){
		int width = getWidth();
		int height = getHeight();
		
		super.paintComponent(g);
		
		for(int i = 0; i<map.length; i++){
			for(int j = 0; j<map[0].length; j++){
				setColor(g,map[i][j]);
				if((i == start.getY()) && (j == start.getX())){
					g.setColor(Color.blue);
				} else if((i == end.getY()) && (j == end.getX())){
					g.setColor(Color.red);
				}
				g.fillRect(j*4, i*4, 4, 4);
			}
		}
		
		synchronized(map){
			map.notify();
		}
	}
}
