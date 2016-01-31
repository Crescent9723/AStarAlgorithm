package AStar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Graph {
	private static Node startNode, goalNode;
	private static int numNode = 0;
	private static int numNodeAStar = 0, numNodeDijkstra = 0;
	private static Scanner scanner;
	private static PrintWriter writer;
	private static ArrayList<Integer> query1 = new ArrayList<Integer>();
	private static ArrayList<Integer> query2 = new ArrayList<Integer>();
	static Map<String, Node> nodeList = new HashMap<String, Node>();
	public static void main(String[] args){
		File inputFile = new File("graph1000.txt");
		try{
			writer = new PrintWriter("4861.txt", "UTF-8");
			getRandomNumbers();
			
			for (int i = 0 ; i < 20; i++){
				int v1 = query1.get(i);
				int v2 = query2.get(i);
				writer.println("Vertex " + (v1+1) + " to Vertex " + (v2+1));
				initializeGraph(inputFile, v1, v2);
				writer.println("A* algorithm: \nDistance: " + aStarSearch(startNode, goalNode) + "\nNumber of Nodes visited: " + numNode);
				numNodeAStar += numNode;
				initializeGraph(inputFile, v1, v2);
				writer.println("\nDijkstra: \nDistance: " + dijkstraSearch(startNode, goalNode) + "\nNumber of Nodes visited: " + numNode);
				numNodeDijkstra += numNode;
				writer.println("-------------------------------------");
			}
			int saving = (numNodeDijkstra - numNodeAStar) / 20;
			writer.println("Average Saving: " + saving);
			scanner.close();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void getRandomNumbers() {
		Random rand = new Random();
		for (int i = 0 ; i < 20 ; i++){
			int  n = rand.nextInt(1000) + 1;
			query1.add(n);			
		}
		for (int i = 0 ; i < 20 ; i++){
			int  n = rand.nextInt(1000) + 1;
			query2.add(n);			
		}
	}
	private static void initializeGraph(File inputFile, int v1, int v2) throws FileNotFoundException, UnsupportedEncodingException {
		nodeList.clear();
		scanner = new Scanner(inputFile);
		String line = scanner.nextLine();
		String[] tokens;
		while (!line.trim().isEmpty()){
			tokens = line.split(", | : ");
			Node node = new Node(tokens[0], Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]));
			nodeList.put(tokens[0], node);
			line = scanner.nextLine();
		}
		while(scanner.hasNextLine()){
			line = scanner.nextLine();
			tokens = line.split(", | : ");
			Node sourceNode = nodeList.get(tokens[0]);
			for (int i = 1 ; i < tokens.length ; i++){
				Node targetNode = nodeList.get(tokens[i]);
				Edge edge = new Edge(getDistance(sourceNode, targetNode), targetNode);
				sourceNode.addEdge(edge);
			}
		}
		startNode = nodeList.get("" + v1);
		goalNode = nodeList.get("" + v2);
	}
	
	//Test with specific data
	private static void initializeGraph(){
		nodeList.clear();
		Node node1 = new Node("1", 0, 1);
		nodeList.put("1", node1);
		Node node2 = new Node("2", 5, 1);
		nodeList.put("2", node2);
		Node node3 = new Node("3", 8, 1);
		nodeList.put("3", node3);
		Node node4 = new Node("4", 0, 2);
		nodeList.put("4", node4);

		
		Edge edge = new Edge(5, node4);
		node1.addEdge(edge);
		edge = new Edge(1, node2);
		node1.addEdge(edge);
		edge = new Edge(2, node3);
		node2.addEdge(edge);
		edge = new Edge(30, node4);
		node3.addEdge(edge);
		startNode = nodeList.get("1");
		goalNode = nodeList.get("4");
	}
	public static double aStarSearch(Node start, Node goal){
		numNode = 1;
		FibonacciHeap heap = new FibonacciHeap();
		nodeList.values().forEach((each) -> {
			each.setDist(Double.MAX_VALUE);
			each.setRem(Math.sqrt(Math.pow(each.getX()-goal.getX(), 2) + (Math.pow(each.getY()-goal.getY(), 2))));
			each.setEst(Double.MAX_VALUE);
			heap.insertNode(each);
		});
		
		start.setDist(0);
		start.setEst(start.getDist() + start.getRem());
		while (!heap.isEmpty()){
			Node u = heap.removeMin();
			if (u == goal){
				return u.getEst();
			}
			ArrayList<Edge> adjacency = u.getAdjacencies();
			adjacency.forEach((each) -> {
				Node v = each.getTarget();
				if (v.getDist() > u.getDist() + each.getCost()){
					numNode++;
					v.setDist(u.getDist() + each.getCost());
					v.setEst(v.getDist() + v.getRem());
					heap.decreaseKey(v, v.getEst());
				}
			});
		}
		return goal.getEst();
	}
	public static double dijkstraSearch(Node start, Node goal){
		numNode = 1;
		FibonacciHeap heap = new FibonacciHeap();
		nodeList.values().forEach((each) -> {
			each.setEst(Double.MAX_VALUE);
			heap.insertNode(each);
		});
		start.setEst(0);
		while (!heap.isEmpty()){
			Node u = heap.removeMin();
			if (u == goal){
				return u.getEst();
			}
			ArrayList<Edge> adjacency = u.getAdjacencies();
			adjacency.forEach((each) -> {
				Node v = each.getTarget();
				if (v.getEst() > u.getEst() + each.getCost()){
					numNode++;
					v.setEst(u.getEst() + each.getCost());
					heap.decreaseKey(v, v.getEst());
				}
			});
		}
		return goal.getEst();
	}
	public static double getDistance(Node q1, Node q2){
        double dlat = 2 * Math.PI * (q2.getX() - q1.getX()) / 360;
        double mlat = 2 * Math.PI * (q1.getX() + q2.getX()) / 2 / 360;
        double dlon = 2 * Math.PI * (q2.getY() - q1.getY()) / 360;
        return 6371009 * Math.sqrt((Math.pow(dlat, 2) + (Math.pow(Math.cos(mlat) * dlon, 2))));
	}
}
