package AStar;

public class Edge {
	private double cost;
	private Node target;
	
	public Edge(double cost, Node target){
		this.setCost(cost);
		this.setTarget(target);
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public Node getTarget() {
		return target;
	}

	public void setTarget(Node target) {
		this.target = target;
	}
}
