import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class Vertex implements Comparable<Vertex>
{
	public final String vertexName;
	public Edge[] adjacentVertices;
	public double minimumDistance = Double.POSITIVE_INFINITY;
	public Vertex previousVertex;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((vertexName == null) ? 0 : vertexName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (vertexName == null) {
			if (other.vertexName != null)
				return false;
		} else if (!vertexName.equals(other.vertexName))
			return false;
		return true;
	}


	public Edge[] getAdjacentVertices() {
		return adjacentVertices;
	}

	public void setAdjacentVertices(Edge[] adjacencies) {
		this.adjacentVertices = adjacencies;
	}


	public Vertex(String vertexName) {
		this.vertexName = vertexName;
	}

	public String toString() {
		return vertexName;
	}

	public int compareTo(Vertex other)
	{
		return Double.compare(minimumDistance, other.minimumDistance);
	}

}

class Edge
{
	public final Vertex nextVertex;
	public final double dist;

	public Edge(Vertex nextVertex, double dist)
	{
		this.nextVertex = nextVertex;
		this.dist = dist;
	}
}

public class BFS
{
	public List<Vertex> vertexList = new ArrayList<Vertex>();
	public static List<Vertex> shortestPath;

	public static void main(String[] args) throws IOException
	{

		BFS d = new BFS();

		BufferedReader br = new BufferedReader(new FileReader("SampleInput"));
		String line = br.readLine();
		String prefix = "v";
		int index = 0;
		Vertex startVertex = null;
		Vertex endVertex = null;
		while(line!=null) {

			String[] splitArray = line.split(" ");
			Edge[] edgeArr = new Edge[splitArray.length / 2];
			Vertex tempVertex = new Vertex(prefix+String.valueOf(index));
			if(!d.vertexList.contains(tempVertex))
				d.vertexList.add(tempVertex);
			if(startVertex == null)
				startVertex = tempVertex;
			
			for(int i=0; i<splitArray.length; i = i+2) {
				String postfix = splitArray[i];
				Vertex ver = new Vertex(prefix+postfix);
				if(!d.vertexList.contains(ver))
					d.vertexList.add(ver);
				Edge e = new Edge(ver, Double.parseDouble(splitArray[i+1]));
				edgeArr[i/2] = e;
			}

			d.vertexList.get(index).setAdjacentVertices(edgeArr);
			index++;
			line = br.readLine();
		}
		endVertex = new Vertex(prefix+String.valueOf(index));
		calculatePath(startVertex);
		
		for (Vertex v : d.vertexList)
		{
			System.out.println("Distance to vertex "+ v + " is " + ": " + v.minimumDistance);
			List<Vertex> path = findShortestPath(v);
			if(v.vertexName.equalsIgnoreCase(endVertex.vertexName))
				shortestPath = path;
			//System.out.println("Current Path: " + path);
		}
		System.out.println("Shortest Path to end Vertex: " + shortestPath);
	}


	public static List<Vertex> findShortestPath(Vertex ver)
	{
		List<Vertex> shortestPath = new ArrayList<Vertex>();
		for (Vertex vertex = ver; vertex != null; vertex = vertex.previousVertex)
			shortestPath.add(vertex);
		Collections.reverse(shortestPath);
		return shortestPath;
	}


	public static void calculatePath(Vertex sourceVertex)
	{
		sourceVertex.minimumDistance = 0.;
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(sourceVertex);

		while (!vertexQueue.isEmpty()) {
			Vertex u = vertexQueue.poll();

			if(u.getAdjacentVertices() != null) {
				for (Edge e : u.adjacentVertices)
				{
					Vertex v = e.nextVertex;
					double dist = e.dist;
					double currentDistance = u.minimumDistance + dist;
					if (currentDistance < v.minimumDistance) {
						vertexQueue.remove(v);
						v.minimumDistance = currentDistance ;
						v.previousVertex = u;
						vertexQueue.add(v);
					}
				}
			}

		}
	}
}