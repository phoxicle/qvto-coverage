package de.phei.qvto.coverage.ui;

import org.eclipse.ocl.utilities.ASTNode;

public class NodeCoverageModel {

	private boolean touched = false;
	private int start;
	private int end;
	private ASTNode node;
	
	public NodeCoverageModel(ASTNode n) {
		start = n.getStartPosition();
		end = n.getEndPosition();
		node = n;
	}
	
	public boolean getTouched() {
		return touched;
	}
	
	public void setTouched(boolean b) {
		touched = b;
	}
	
	public int getStart() {
		return start;
	}
	
	public int getEnd() {
		return end;
	}
	
	public ASTNode getNode() {
		return node;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + end;
		result = prime * result + start;
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
		NodeCoverageModel other = (NodeCoverageModel) obj;
		if (end != other.end)
			return false;
		if (start != other.start)
			return false;
		return true;
	}
	
	public String toString() {
		return node.getClass().getName() + ":" + start + "-" + end + " (parent: " + node.eContainer().getClass() + ")";
	}
}
