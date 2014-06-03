package de.phei.qvto.coverage.common;

import java.io.Serializable;

import org.eclipse.ocl.utilities.ASTNode;

public class NodeData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int start;
	private int end;
	
	public NodeData(ASTNode node) {
		start = node.getStartPosition();
		end = node.getEndPosition();
	}
	
	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
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
		NodeData other = (NodeData) obj;
		if (end != other.end)
			return false;
		if (start != other.start)
			return false;
		return true;
	}
	

}
