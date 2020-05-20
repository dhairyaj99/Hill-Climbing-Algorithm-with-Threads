package Assign3_2P05;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Assign3 extends JFrame{
	
	Object lock = new Object();
	static Boolean gridd[][];
	static String best[];
	static int bestscore;
	private static final String sequence = "NESW";
	int boardWidth = 5;
	int boardHeight = 3;
	
	public Assign3() {
		//setup();		
		
		String s[] = generateS((boardWidth*boardHeight)-1);
		Boolean grid2[][] = run(s, boardWidth,boardHeight);
		
		bestscore = evaluate(grid2);
		
		printb(grid2,s);
		
		Runnable run = new mt(s);
		
		Thread th,th2,th3;
		
		while(true) {
			th = new Thread(run);
			th2= new Thread(run);
			th3= new Thread(run);
			
			th.start();
			th2.start();
			th3.start();

			try {
				th.join();
				th2.join();
				th3.join();
			}
			catch(Exception e){
				System.out.println("FLOPPPP");
			}
			printb(grid2,best);
		}
	}
	
	public static void main(String[] args) { new Assign3();	
	}
	
	public static void printb(Boolean g[][], String s[]) {
		for(int i = 0;i<s.length;i++) {
			System.out.print(s[i]);
		}
		System.out.println();
		
		for(int i = 0;i<g.length;i++) {
			for(int j = 0;j<g[i].length;j++) {
				System.out.print(g[i][j]);
			}
			System.out.println();
		}
	}
	
	public static int evaluate(Boolean grid[][]) {
		int score = 0;
		
		for(int i=0;i<grid.length;i++) {
			for(int j=0;j<grid[i].length;j++) {
				if(grid[i][j]==true)
					score++;
			}
		}
		return score;
	}
	
	public static Boolean[][] run(String temp[], int w, int l) {
		
		Boolean grid[][] = new Boolean[l][w];
		for(int i = 0;i<grid.length;i++) {
			for(int j = 0;j<grid[i].length;j++) {
				grid[i][j] = false;
			}
		}
		int x=0;
		int y=0;
		grid[x][y] = true;
		
		for(int i=0;i<temp.length;i++) {
			if(temp[i].indexOf('W')!=-1) {
				if(y==0)
					return grid;
				else if(grid[x][y-1] ==true){
					return grid;
				}
				else {
					grid[x][y-1]=true;
					y--;
				}
			}
			
			if(temp[i].indexOf('E')!=-1) {
				if(y==grid[x].length-1)
					return grid;
				else if(grid[x][y+1] ==true){
					return grid;
				}
				else {
					grid[x][y+1]=true;
					y++;
				}
			}
			
			if(temp[i].indexOf('N')!=-1) {
				if(x==0)
					return grid;
				else if(grid[x-1][y] ==true){
					return grid;
				}
				else {
					grid[x-1][y]=true;
					x--;
				}
			}
			
			if(temp[i].indexOf('S')!=-1) {
				if(x==grid.length-1)
					return grid;
				else if(grid[x+1][y] ==true){
					return grid;
				}
				else {
					grid[x+1][y]=true;
					x++;
				}
			}
			
		}
		return grid;
	}
	
	public static String[] generateS(int count) {
		StringBuilder b = new StringBuilder();
		while (count-- != 0) {
			int character = (int)(Math.random()*sequence.length());
			b.append(sequence.charAt(character));
		}
		
		String temp [] = new String[b.length()];
		temp = b.toString().split("");
		
		return temp;
	}
}

class mt implements Runnable{
	
	private String[] s;
	private ReentrantLock mutex = new ReentrantLock();
	
	public mt(String[] s) {
		this.s = s;
	}
	
	@Override
	public void run() {
		Boolean g[][] = null;
		String temp[] = s;
		Boolean c = false;
		
		System.out.println(Assign3.bestscore + " " + Thread.currentThread().getName());
		
		while(c==false) {
			Random r = new Random();
			int l = r.nextInt(s.length);
			int d = r.nextInt(4);
			String n = "";

			if(d==0)
				n="N";
			else if(d==1)
				n="E";
			else if(d==2)
				n="S";
			else if(d==3)
				n="W";

			temp[l] = n;

			g = Assign3.run(temp, 5, 3);


			try {
				mutex.lock();
				try {
					if(Assign3.evaluate(g)>Assign3.bestscore) {
						c=true;
						Assign3.bestscore = Assign3.evaluate(g);
						Assign3.gridd = g;
						Assign3.best=temp;
						System.out.println(Thread.currentThread().getName() + " is entering critical region");
					}
					
				}
				finally {
						mutex.unlock();
					}
			}
			catch(Exception e) {}
		}
		System.out.println(Assign3.evaluate(g) + " " + Thread.currentThread().getName());
		}
		
}