/**********************************
 * Created by Isaac Gagnon 6/18/2016
 * 
 * A simple class used to maintain the current state of the world tiles
 * as ints, and the original state of the world as strings.
 * 
 * Last updated 6/18/2016
 **********************************/

public class Tile{
	private int iVal;
	private String sVal;
	
	Tile(int i, String s){
		iVal = i;
		sVal = s;
	}
	
	/**
	 * Sets the int value of the tile, updating its current state
	 **/
	public void setIVal(int i){
		iVal = i;
	}
	
	/**
	 * Returns the current state of the tile as an int 
	 **/
	public int getIVal(){
		return iVal;
	}
	
	/**
	 * Returns the original state of the tile as a string
	 * used to assure that the start tile and goal tiles consistently
	 * remain the same color throughout the search process.
	 **/
	public String getSVal(){
		return sVal;
	}
}
