

import java.util.ArrayList;

public class Pathfinder {

	public Grid grid;
	public Node[][] nodes;
	public Node start, end;
	public ArrayList<Node> openSet, closedSet;

	public Pathfinder(Grid grid)
	{
		this.grid = grid;
		nodes = new Node[grid.rows][grid.cols];
		for (int r = 0; r < grid.rows; r++)
		{
			for (int c = 0; c < grid.cols; c++)
			{
				if (grid.getTile(r,c).biome != -1)
					nodes[r][c] = new Node(r,c);
			}
		}
		//System.out.println(x1 + "," + y1 + "," + x2 + "," + y2);
	}
	
	//http://theory.stanford.edu/~amitp/GameProgramming/\
	//public ArrayList<Tile> findPath(Civilization civ, int x1, int y1, int x2, int y2, boolean diagonal)
	Node lastNode;
	public void findPath(int x1, int y1, int x2, int y2, boolean diagonal)
	{
		for (int r = 0; r < grid.rows; r++)
		{
			for (int c = 0; c < grid.cols; c++)
			{
				if (grid.getTile(r,c).biome != -1)
				{
					//nodes[r][c] = new Node(r,c);
					nodes[r][c].parent = null;
					nodes[r][c].g = 0;
					nodes[r][c].queue = 0;
				}
			}
		}
		start = nodes[x1][y1];
		start.queue = 0;
		end = nodes[x2][y2];
		openSet = new ArrayList<Node>();
		closedSet = new ArrayList<Node>();

		//System.out.println(openSet + ":::" + closedSet);
		
		openSet.add(start);

		lastNode = start;
	}
	
	//Simulates one step of the iteration process and has 3 possibilities
	//null -> no path
	//empty -> still pathing
	//non-empty -> path
	//Intended to be used by a visual output program
	public ArrayList<Tile> iterateAndReturn()
	{
		if (!iterate()) 
			return null;
		Node candidate = openSet.get(findLowestQueueIndex(openSet)); 
		if (candidate.equals(end))
		{
			ArrayList<Tile> temp = recursivePath();
			temp.add(grid.getTile(candidate.r, candidate.c));
			return temp;
		}
		else
		{
			//Shortcut checking
			ArrayList<Node> n = findValidNeighbors(candidate, true);
			for (int i = 0; i < n.size(); i++)
			{
				if (n.get(i).equals(end))
				{
					ArrayList<Tile> temp = recursivePath();
					temp.add(grid.getTile(n.get(i).r, n.get(i).c));
					return temp;
				}
			}
		}
		return new ArrayList<Tile>();
	}
	
	//Simulates one iteration of the actual algorithm itself
	private boolean iterate()
	{
		//System.out.println("ran");
		System.out.println(openSet + " " + closedSet);
		Node current = openSet.get(findLowestQueueIndex(openSet));
		openSet.remove(findLowestQueueIndex(openSet));
		closedSet.add(current);
		ArrayList<Node> ns = findValidNeighbors(current,true);
		for (int i = 0; i < ns.size(); i++)
		{
			double cost;
			if (true)
				cost = current.g + current.dist(ns.get(i));
			else
				cost = current.g + 1;
			/*if (current.r != ns.get(i).r && current.c != ns.get(i).c)
				cost = current.g + 1.4;
			else
				cost = current.g + 1;*/
			if (openSet.contains(ns.get(i)) && cost < ns.get(i).g)
				removeNodeFromOpen(ns.get(i));
			if (closedSet.contains(ns.get(i)) && cost < ns.get(i).g)
				removeNodeFromClosed(ns.get(i));
			if (!openSet.contains(ns.get(i)) && !closedSet.contains(ns.get(i)))
			{
				ns.get(i).g = cost;
				openSet.add(ns.get(i));
				double dist = ns.get(i).dist(end);
				if (dist == -1)
					return false;
				ns.get(i).queue = ns.get(i).g + 1.1*dist;
				ns.get(i).parent = current;
				lastNode = ns.get(i);
			}
		}
		for (int i = openSet.size() - 1; i >= 0; i--)
			if (openSet.get(i).dist(end) > 1.25*start.dist(end))
				openSet.remove(i);
		if (openSet.size() == 0) 
			return false;
		return true;
	}
	
	private ArrayList<Tile> recursivePath()
	{
		ArrayList<Tile> temp = new ArrayList<Tile>();
		do
		{
			temp.add(grid.getTile(lastNode.r,lastNode.c));
			lastNode = lastNode.parent;
			if (lastNode == null) return null;
		} while (lastNode.parent != null);
		return temp;
	}
	
	//http://theory.stanford.edu/~amitp/GameProgramming/\
	//public ArrayList<Tile> findPath(Civilization civ, int x1, int y1, int x2, int y2, boolean diagonal)
	/*Node lastNode = null;
	boolean diagonal = true;
	public ArrayList<Tile> findPath(int x1, int y1, int x2, int y2)
	{
		for (int r = 0; r < grid.rows; r++)
		{
			for (int c = 0; c < grid.cols; c++)
			{
				//if (grid.getTile(r,c).biome != -1)
				{
					//nodes[r][c] = new Node(r,c);
					nodes[r][c].parent = null;
					nodes[r][c].g = 0;
					nodes[r][c].queue = 0;
				}
			}
		}
		start = nodes[x1][y1];
		start.queue = 0;
		end = nodes[x2][y2];
		openSet = new ArrayList<Node>();
		closedSet = new ArrayList<Node>();
		openSet.add(start);
		int stop;
		do
		{
			//System.out.println("ran");
			stop = iterate();
			if (stop == -1)
				return null;
			if (stop == 1)
				break;
		} while (true);
		ArrayList<Tile> temp = new ArrayList<Tile>();
		do
		{
			temp.add(grid.getTile(lastNode.r,lastNode.c));
			lastNode = lastNode.parent;
		} while (lastNode.parent != null);
		return temp;
	}
	
	//Iterate a step of the A* process and return true if it's time to stop
	public int iterate()
	{
		Node current = openSet.get(findLowestQueueIndex(openSet));
		openSet.remove(findLowestQueueIndex(openSet));
		closedSet.add(current);
		//ArrayList<Node> ns = findValidNeighbors(current,civ,diagonal);
		ArrayList<Node> ns = findValidNeighbors(current,diagonal);
		for (int i = 0; i < ns.size(); i++)
		{
			double cost;
			if (diagonal)
				cost = current.g + current.dist(ns.get(i));
			else
				cost = current.g + 1;
			if (current.r != ns.get(i).r && current.c != ns.get(i).c)
				cost = current.g + 1.4;
			else
				cost = current.g + 1;
			if (openSet.contains(ns.get(i)) && cost < ns.get(i).g)
			{
				removeNodeFromOpen(ns.get(i));
				//closedSet.add(ns.get(i));
			}
			if (closedSet.contains(ns.get(i)) && cost < ns.get(i).g)
			{
				removeNodeFromClosed(ns.get(i));
			}
			if (!openSet.contains(ns.get(i)) && !closedSet.contains(ns.get(i)))
			{
				ns.get(i).g = cost;
				openSet.add(ns.get(i));
				double dist = ns.get(i).dist(end);
				if (dist == -1)
				{
					//System.err.println("No path found.");
					return -1;
				}
				ns.get(i).queue = ns.get(i).g + 1.1*dist;
				ns.get(i).parent = current;
				lastNode = ns.get(i);
				System.out.println("::::" + lastNode.r + " " + lastNode.c);
			}
		}
		for (int i = openSet.size() - 1; i >= 0; i--)
			if (openSet.get(i).dist(end) > 1.25*start.dist(end))
				openSet.remove(i);
		if (openSet.size() == 0) 
			return -1;
		if (!openSet.get(findLowestQueueIndex(openSet)).equals(end))
			return 1;
		return 0;
	}*/
	
	//public ArrayList<Tile> findAdjustedPath(Civilization civ, int x1, int y1, int x2, int y2)
	/*public ArrayList<Tile> findAdjustedPath(int x1, int y1, int x2, int y2)
	{
		ArrayList<Tile> temp = findPath(x1,y1,x2,y2,true);
		//ArrayList<Tile> temp = findPath(civ,x1,y1,x2,y2,true);
		if (temp == null) return null;
		if (!temp.get(0).equals(grid.getTile(end.r,end.c)))
		{
			temp.remove(0);
			temp.add(0,grid.getTile(end.r,end.c));
		}
		//temp.add(temp.size(),new Location(start.r,start.c));
		return temp;
	}*/

	public int findLowestQueueIndex(ArrayList<Node> nodes)
	{
		if (nodes.size() == 1) return 0;
		int lowestIndex = 1;
		for (int i = 0; i < nodes.size(); i++)
		{
			if (nodes.get(i).queue < nodes.get(lowestIndex).queue)
				lowestIndex = i;
		}
		return lowestIndex;
	}

	public void removeNodeFromOpen(Node node)
	{
		for (int i = 0; i < openSet.size(); i++)
		{
			if (openSet.get(i).equals(node))
			{
				openSet.remove(i);
				return;
			}
		}
	}

	public void removeNodeFromClosed(Node node)
	{
		for (int i = 0; i < closedSet.size(); i++)
		{
			if (closedSet.get(i).equals(node))
			{
				closedSet.remove(i);
				return;
			}
		}
	}

	public ArrayList<Node> findValidNeighbors(Node node, boolean diagonal)
	{
		ArrayList<Node> temp = new ArrayList<Node>();
		int r = node.r;
		int c = node.c;
		try
		{
			if (nodes[r+1][c] != null)
			{
				temp.add(nodes[r+1][c]);
			}
			if (nodes[r-1][c] != null)
			{
				temp.add(nodes[r-1][c]);
			}
			if (nodes[r][c+1] != null)
			{
				temp.add(nodes[r][c+1]);
			}
			if (nodes[r][c-1] != null)
			{
				temp.add(nodes[r][c-1]);
			}
			if (diagonal)
			{
				if (nodes[r+1][c+1] != null)
				{
					temp.add(nodes[r+1][c+1]);
				}
				if (nodes[r+1][c-1] != null)
				{
					temp.add(nodes[r+1][c-1]);
				}
				if (nodes[r-1][c+1] != null)
				{
					temp.add(nodes[r-1][c+1]);
				}
				if (nodes[r-1][c-1] != null)
				{
					temp.add(nodes[r-1][c-1]);
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {}
		/*for (int i = temp.size() - 1; i >= 0; i--)
		{
			Civilization civ2 = grid.getTile(temp.get(i).r, temp.get(i).c).owner;
			if (civ2 != null)
				if (!civ.isOpenBorder(civ2) && !civ.isWar(civ2) && !civ.equals(civ2))
					temp.remove(i);
		}*/
		return temp;
	}

	public class Node
	{
		public int r,c;
		public double g;
		public Node parent;
		public double queue;
		public Node(int r, int c)
		{
			this.r = r;
			this.c = c;
			g = 0;
		}
		public boolean equals(Node n)
		{
			return r == n.r && c == n.c;
		}
		public double dist(Node other)
		{
			if (other != null)
				return Math.sqrt(Math.pow(other.r - r,2) + Math.pow(other.c - c,2));
				//return Math.abs((double)other.r - r) + Math.abs((double)other.c - c);
			return -1;
		}
		public int[] array() {return new int[]{r,c};}
	}

	public class Location {int r, c; public Location(int x, int y) {r=x; c=y;}}

}
