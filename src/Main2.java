import processing.core.PApplet;

import java.util.ArrayList;

//Extend PApplet to support possible addition of rendering 
public class Main2 extends PApplet {

	public Grid grid;
	public Pathfinder path;
	public ArrayList<Tile> pathTo = new ArrayList<Tile>();
	public int rows = 20, cols = 20, n = 1000;

	public void setup()
	{
		size(800,800);
		grid = new Grid(rows, cols, 1);
		path = new Pathfinder(grid);
		
		long time = System.currentTimeMillis();
		for (int i = 0; i < n; i++)
		{
			ArrayList<Tile> result;
			Tile first = randomTile(), next = randomTile();
			path.findPath(first.r, first.c, next.r, next.c, true);
			do
			{
				result = path.iterateAndReturn();
				if (result == null) break;
				else if (result.size() > 0) break;
			} while (true);
		}
		time = System.currentTimeMillis() - time;
		System.out.println("Completed " + n + " trials in " + (time) + " milliseconds with "
				+ "an average of " + time/(float)n + " milliseconds per attempt");
		noLoop();
	}
	
	public void draw()
	{
		
	}
	
	public Tile randomTile()
	{
		return grid.getTile((int)(Math.random()*rows), (int)(Math.random()*cols));
	}
	
}
