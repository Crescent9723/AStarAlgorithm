package AStar;

import java.util.ArrayList;
import AStar.Node;

public class FibonacciHeap {
	private static final double oneOverLogPhi = 1.0 / Math.log((1.0 + Math.sqrt(5.0)) / 2.0);
	private Node min;
	private int size;
	public FibonacciHeap() {
		min = null;
		size = 0;
	}
	public Node getMin(){
		return min;
	}
	public boolean isEmpty() {
		return min == null;
	}
	public void clear(){
		min = null;
		size = 0;
	}
	public int getSize(){
		return size;
	}
	public void insertNode(Node node){
		if (min != null) {
			node.setLeft(min);
			node.setRight(min.getRight());
			min.setRight(node);
			node.getRight().setLeft(node);
			if (node.getEst() < min.getEst()){
				min = node;
			}
		} else {
			min = node;
		}
		size++;
	}
	public Node removeMin(){
		Node temp = min;
		if (temp != null){
			int deg = temp.getDegree();
			Node child = temp.getChild();
			Node tempRight;
			while (deg > 0) {
				tempRight = child.getRight();
				child.getLeft().setRight(child.getRight());
				child.getRight().setLeft(child.getLeft());
				
				child.setLeft(min);
				child.setRight(min.getRight());
				min.setRight(child);
				child.getRight().setLeft(child);
				
				child.setParent(null);
				child = tempRight;
				deg--;
			}		
			temp.getLeft().setRight(temp.getRight());
			temp.getRight().setLeft(temp.getLeft());
			
			if (temp == temp.getRight()){
				min = null;
			} else {
				min = temp.getRight();
				consolidate();
			}
			size--;
		}
		return temp;
	}
    private void consolidate(){
        int arraySize = ((int) Math.floor(Math.log(size) * oneOverLogPhi)) + 2;
        ArrayList<Node> array = new ArrayList<Node>(arraySize);

        for (int i = 0; i < arraySize; i++){
        	array.add(null);
       	}

        int numRoots = 0;
        Node rootNode = min;

        if (rootNode != null) {
            numRoots++;
            rootNode = rootNode.getRight();

            while (rootNode != min) {
                numRoots++;
                rootNode = rootNode.getRight();
            }
        }

        while (numRoots > 0) {
            int deg = rootNode.getDegree();
            Node next = rootNode.getRight();

           	while (true) {
           		Node y = array.get(deg);
           		if (y == null) {
           			break;
           		}
           		
           		if (rootNode.getEst() > y.getEst()){
           			Node temp = y;
           			y = rootNode;
           			rootNode = temp;
           		}
           		
           		link(y, rootNode);
           		array.set(deg, null);
           		deg++;
           	}

            array.set(deg, rootNode);

            rootNode = next;
            numRoots--;
        }

        min = null;

        for (int i = 0; i < arraySize; i++) {
            Node y = array.get(i);
            if (y == null) {
            	continue;
            }
            
            if (min != null){
            	y.getLeft().setRight(y.getRight());
            	y.getRight().setLeft(y.getLeft());
            	y.setLeft(min);
            	y.setRight(min.getRight());
            	min.setRight(y);
            	y.getRight().setLeft(y);
            	
            	if (y.getEst() < min.getEst()){
            		min = y;
            	}
            } else {
            	min = y;
            }
        }
    }
    protected void link(Node child, Node parent)
    {
        child.getLeft().setRight(child.getRight());
        child.getRight().setLeft(child.getLeft());

        child.setParent(parent);;

        if (parent.getChild() == null) {
            parent.setChild(child);
            child.setRight(child);
            child.setLeft(child);
        } else {
            child.setLeft(parent.getChild());
            child.setRight(parent.getChild().getRight());
            parent.getChild().setRight(child);
            child.getRight().setLeft(child);
        }

        parent.incrementDegree();

        child.setMarked(false);
    }


	public void decreaseKey(Node node, double key){
        if (key > node.getEst()) {
            throw new IllegalArgumentException("new key value is larger than original key");
        }

        node.setEst(key);
        Node parent = node.getParent();
        if ((parent != null) && (node.getEst() < parent.getEst())) {
            cut(node, parent);
            cascadingCut(parent);
        }

        if (node.getEst() < min.getEst()) {
            min = node;
        }

	}
	private void cut(Node node, Node parent){
		node.getLeft().setRight(node.getRight());
		node.getRight().setLeft(node.getLeft());
		parent.decrementDegree();
		
		if (parent.getChild() == node){
			parent.setChild(node.getRight());
		}
		
		if (parent.getDegree() == 0) {
			parent.setChild(null);
		}
		
		node.setLeft(min);
		node.setRight(min.getRight());
		min.setRight(node);
		node.getRight().setLeft(node);
		
		node.setParent(null);
		
		node.setMarked(false);
	}
	
    private void cascadingCut(Node parent){
        Node grandParent = parent.getParent();

        if (grandParent != null) {
            if (!parent.isMarked()) {
                parent.setMarked(true);
            } else {
                cut(parent, grandParent);
                cascadingCut(grandParent);
            }
        }
    }
}
