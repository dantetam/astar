
public class Grid {

	private Tile[][] tiles;
	public int rows, cols;
	
	public Grid(int nr, int nc, double p)
	{
		rows = nr; 
		cols = nc;
		tiles = new Tile[rows][cols];
		for (int r = 0; r < rows; r++)
		{
			for (int c = 0; c < cols; c++)
			{
				tiles[r][c] = new Tile(r,c);
				if (Math.random() < p)
					tiles[r][c].biome = 0;
				else
					tiles[r][c].biome = -1;
			}
		}
	}

	public Tile getTile(int r, int c) {
		if (r >= 0 && r < rows && c >= 0 && c < cols)
			return tiles[r][c];
		return null;
	}
	
}
