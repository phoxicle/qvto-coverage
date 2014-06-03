package de.phei.qvto.coverage.common;

import java.io.Serializable;
import java.util.HashSet;

import org.eclipse.emf.common.util.URI;
import org.eclipse.m2m.internal.qvt.oml.expressions.Constructor;
import org.eclipse.m2m.internal.qvt.oml.expressions.Helper;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.AssignExp;
import org.eclipse.ocl.ecore.InvalidLiteralExp;
import org.eclipse.ocl.ecore.OCLExpression;
import org.eclipse.ocl.utilities.ASTNode;

@SuppressWarnings("restriction")
public class TransformationCoverageData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static boolean isIncluded(OCLExpression node) {		
		// Also skip LHS of AssignExpressions, since those are not formally visited anyway
		return !(node.eContainer() instanceof AssignExp && ((AssignExp)node.eContainer()).getLeft().equals(node));
	}
	
	
	
	public static boolean isIncluded(ASTNode node) {
		
		boolean included = false;
		if (!(node instanceof InvalidLiteralExp)
				&& (node instanceof MappingOperation
				|| node instanceof Helper
				|| node instanceof Constructor
				|| (node instanceof OCLExpression && TransformationCoverageData.isIncluded((OCLExpression) node))
				)) {
			included = true;
		}
		return included;
	}

	private String URIString;
	private HashSet<NodeData> touched = new HashSet<NodeData>();
	
	public TransformationCoverageData(URI uri) {
		setURI(uri);
	}
	
	public void setURI(URI uri) {
		URIString = uri.toString();
	}
	
	public URI getURI() {
		return URI.createURI(URIString);
	}
	
	public boolean containsNode(ASTNode node) {
		return touched.contains(new NodeData(node));
	}
	
	public void touch(ASTNode element) {
		if (isIncluded(element)) {
			touched.add(new NodeData((ASTNode) element));
		}
	}

	

}
