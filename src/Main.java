
import java.util.ArrayList;

import processing.core.PApplet;

public class Main extends PApplet {

	public Grid grid;
	public Pathfinder path;
	public ArrayList<Tile> pathTo = new ArrayList<Tile>();
	public int rows = 20, cols = 20;

	public void setup()
	{
		size(800,800);
		grid = new Grid(20,20);
		path = new Pathfinder(grid);
		status = new Color[rows][cols];
	}

	public void draw()
	{
		background(255);
		for (int r = 0; r < grid.rows; r++)
		{
			for (int c = 0; c < grid.cols; c++)
			{
				Tile t = grid.getTile(r, c);
				if (t.biome == -1)
					fill(150,225,255);
				else
					fill(255);
				int len = width/grid.rows;
				rect(r*len, c*len, len, len);
				Color f = status[r][c]; //f for fill
				if (f != null)
				{
					fill(f.r, f.g, f.b);
					rect(r*len + len/4, c*len + len/4, len/2, len/2);
				}
			}
		}
		fill(255,0,0);
		if (pathTo != null)
			for (int i = 0; i < pathTo.size(); i++)
			{
				Tile t = pathTo.get(i);
				int len = width/grid.rows;
				rect(t.r*len, t.c*len, len, len);
			}
	}

	public Tile first = null, next = null;
	public Color[][] status;
	public void mousePressed()
	{
		if (mouseButton == LEFT)
		{
			Tile candidate = grid.getTile((int)(mouseX/(float)width*(float)grid.rows), (int)(mouseY/(float)height*(float)grid.cols));
			//println(next.r + " " + next.c);
			if (first == null)
			{
				first = candidate;
				pathTo = null;
			}
			else if (next == null)
			{
				next = candidate;
				path.findPath(first.r, first.c, next.r, next.c, true);
			}
			else
			{
				
			}
		}
		else
		{
			if (first == null || next == null) return;
			ArrayList<Tile> result = path.iterateAndReturn();
			if (result == null)
			{
				println("No path");
				first = null; next = null;
			}
			else if (result.size() != 0)
			{
				pathTo = result;
				if (pathTo.contains(next))
				{
					first = null; next = null;
					path.openSet.clear(); path.closedSet.clear();
				}
			}
			status = new Color[rows][cols];
			for (int i = 0; i < path.openSet.size(); i++)
			{
				int[] t = path.openSet.get(i).array();
				status[t[0]][t[1]] = new Color(0,255,0);
			}
			for (int i = 0; i < path.closedSet.size(); i++)
			{
				int[] t = path.openSet.get(i).array();
				status[t[0]][t[1]] = new Color(255,0,0);
			}
			ArrayList<Tile> t = path.recursivePath();
			if (t != null)
				for (int i = 0; i < t.size(); i++)
					status[t.get(i).r][t.get(i).c] = new Color(150,225,255);
		}
	}

	public class Color {float r, g, b; public Color(float x, float y, float z) {r = x; g = y; b = z;} }
	
}
