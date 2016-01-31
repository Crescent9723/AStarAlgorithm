package AStar;

import java.util.ArrayList;

public class Node {
	private String name;
	private double x;
	private double y;
	private double dist;
	private double rem;
	private double est;
	private ArrayList<Edge> adjacencies = new ArrayList<Edge>();
	private Node child;
	private Node parent;
	private Node left;
	private Node right;
	private boolean visited;
	private boolean marked;
	private int degree;
	
	public Node(String name, double x, double y){
		setName(name);
		setX(x);
		setY(y);
		setRight(this);
		setLeft(this);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getDist() {
		return dist;
	}
	public void setDist(double dist) {
		this.dist = dist;
	}
	public double getRem() {
		return rem;
	}
	public void setRem(double rem) {
		this.rem = rem;
	}
	public double getEst() {
		return est;
	}
	public void setEst(double est) {
		this.est = est;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public Node getLeft() {
		return left;
	}
	public void setLeft(Node left) {
		this.left = left;
	}
	public Node getRight() {
		return right;
	}
	public void setRight(Node right) {
		this.right = right;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public void addEdge(Edge edge){
		getAdjacencies().add(edge);
	}
	public ArrayList<Edge> getAdjacencies() {
		return adjacencies;
	}
	public boolean isVisited() {
		return visited;
	}
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	public boolean isMarked() {
		return marked;
	}
	public void setMarked(boolean marked) {
		this.marked = marked;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	public void decrementDegree() {
		degree--;
	}
	public void incrementDegree() {
		degree++;
	}
	public Node getChild() {
		return child;
	}
	public void setChild(Node child) {
		this.child = child;
	}
}
