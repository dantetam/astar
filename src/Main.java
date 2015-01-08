
import java.util.ArrayList;

import processing.core.PApplet;

public class Main extends PApplet {

	public Grid grid;
	public Pathfinder path;
	public ArrayList<Tile> pathTo = new ArrayList<Tile>();

	public void setup()
	{
		size(800,800);
		grid = new Grid(20,20);
		path = new Pathfinder(grid);
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
				{
					fill(150,225,255);
				}
				else
				{
					fill(255);
				}
				int len = width/grid.rows;
				rect(r*len, c*len, len, len);
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

	public Tile first = null;
	public void mousePressed()
	{
		Tile next = grid.getTile(mouseX/width*grid.rows, mouseY/height*grid.cols);
		if (first == null)
		{
			first = next;
		}
		else
		{
			pathTo = path.findAdjustedPath(first.r, first.c, next.r, next.c);
			first = null;
		}
	}

}
