/**********************************
 * Created by Isaac Gagnon 6/11/2016
 * 
 * This is a simple program that scans the world (in the form of a
 * 2D Tile array) and builds a corresponding display out of colored
 * squares. It updates every time a node is expanded.
 * 
 * Last updated 6/18/2016
 **********************************/

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;

public class Visualizer extends JPanel{
	private Tile[][] map;
	
	Visualizer(Tile[][] m){
		super();
		setBackground(Color.white);
		map = m;
	}
	
	/**
	 * Takes in an int and a graphics object, and sets the color of 
	 * the graphics object according to the int.
	 **/
	private void updateColor(Graphics g, int n){
		switch(n){
			case 0: g.setColor(Color.gray);
				break;
			case 1: g.setColor(Color.orange);
				break;
			case 2: g.setColor(Color.magenta);
				break;
			case 3: g.setColor(Color.black);
				break;
			case 4: g.setColor(Color.green);
				break;
			default: g.setColor(Color.gray);
		}
	}
	
	/**
	 * Runs a double for loop over the representative 2D Tile array
	 * and builds a display out of colored squares.
	 **/
	protected void paintComponent(Graphics g){
		int width = getWidth();
		int height = getHeight();
		
		super.paintComponent(g);
		
		for(int i = 0; i<map.length; i++){
			for(int j = 0; j<map[0].length; j++){
				//setColor(g,map[i][j]);
				if(map[i][j].getSVal() == "@"){
					g.setColor(Color.blue);
				} else if(map[i][j].getSVal() == "*"){
					g.setColor(Color.red);
				} else {
					updateColor(g,map[i][j].getIVal());
				}
				g.fillRect(j*4, i*4, 4, 4);
			}
		}
		
		synchronized(map){
			map.notify();
		}
	}
}
