import java.io.File;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

public class MapGen{
	public static void main(String[] args){
		if(args.length != 4){
            System.err.println("Wrong Number of arguments");
            System.err.println("java main [rows] [cols] [blocked] [fname]");
            System.exit(-1);
        }
        
        for(int i = 0; i<4; i++){
			if(i < 2){
				if(!args[i].matches("[0-9]+")){
					if(args[i].matches("-.*")){
						System.err.println("ERROR: Invalid input, " + args[i] + " is negative");
						System.exit(-1);
					}else{
						System.err.println("ERROR: Invalid input, " + args[i] + " is not a valid integer");
						System.exit(-1);
					}
				}
			} else if (i == 2){
				if(!args[i].matches("[0-1]")&&!args[i].matches("[0].[0-9]+")){
					System.err.println("ERROR: Invalid input, " + args[i] + " is not a valid double");
					System.exit(-1);
				}
			} else {
				System.out.println("Still working on test case...");
			}
		}
        
        int rows = Integer.parseInt(args[0]);
        int cols = Integer.parseInt(args[1]);
        double pb = Double.parseDouble(args[2]);
        String f = args[3] + ".txt";
        
        Scanner input = new Scanner(System.in);
        
        File file = new File(f);
        if(file.exists()){
			while(true){
				System.out.println("File already exists. Delete file? Y/N");
				String in = input.nextLine();
			
				if(in.equals("Y") || in.equals("y")){
					file.delete();
					break;
				} else if(in.equals("N") || in.equals("n")){
					System.err.println("EXITING");
					System.exit(-1);
				}
			}
		}
		
		Random r = new Random();
		double tmp;
		char[][] map = new char[rows][cols];
		
		for(int i = 0; i<rows; i++){
			for(int j = 0; j<cols; j++){
				tmp = r.nextDouble();
				if(tmp <= pb){
					map[i][j] = '#';
				} else {
					map[i][j] = '_';
				}
			}
		}
		
		int x,y;

		x = r.nextInt(cols);
		y = r.nextInt(rows);
		map[y][x] = '@';
		
		while(true){
			x = r.nextInt(cols);
			y = r.nextInt(rows);
		
			if(map[y][x] == '@'){
				System.err.println("ERROR: goal coordinates same as start");
			} else {
				map[y][x] = '*';
				break;
			}
		}
		
		System.out.println("Map Generated");
		
		BufferedWriter output;
        try{
			file.createNewFile();
			output = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			
			output.write(args[0]);
			output.newLine();
			output.write(args[1]);
			output.newLine();
			
			for(char[] s: map){
				for(char c: s){
					output.write(c);
				}
				output.newLine();
			}
			
			output.close();
		} catch (IOException e){
			System.err.println(e);
			System.exit(-1);
		}
		
		
	}
}
