package de.phei.qvto.coverage.ui;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.m2m.internal.qvt.oml.InternalTransformationExecutor;
import org.eclipse.m2m.internal.qvt.oml.compiler.CompiledUnit;
import org.eclipse.m2m.internal.qvt.oml.expressions.Constructor;
import org.eclipse.m2m.internal.qvt.oml.expressions.Helper;
import org.eclipse.m2m.internal.qvt.oml.expressions.Library;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.Module;
import org.eclipse.ocl.ecore.OCLExpression;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

@SuppressWarnings("restriction")
public class TransformationCoverageModel {
	
	private URI uri;
	
	private Map<NodeCoverageModel,NodeCoverageModel> nodes = new HashMap<NodeCoverageModel,NodeCoverageModel>();
	
	private Module module;
	private IProject project;
	private IFile file;
	
	private RangeSet<Integer> touchedRanges;
	private RangeSet<Integer> untouchedRanges;
	
	public TransformationCoverageModel() {
	}
	
	public TransformationCoverageModel(URI uri) {
		setURI(uri);
	}
	
	public void setURI(URI aUri) {
		uri = aUri;
	}
	
	public URI getURI(){
		return uri;
	}
	
	private Set<NodeCoverageModel> getNodes(boolean touched, Class<?> c) {
		Set<NodeCoverageModel> desiredNodes = new HashSet<NodeCoverageModel>();
		for (NodeCoverageModel n : nodes.values()) {
			
			if (c.isInstance(n.getNode()) && n.getTouched() == touched) {
				desiredNodes.add(n);
			}
		}
		return desiredNodes;
	}
	
	private Set<NodeCoverageModel> getMappingNodes(boolean touched) {
		return getNodes(touched, MappingOperation.class);
	}
	
	// TODO add fields for these to improve speed
	public Set<NodeCoverageModel> getUntouchedMappingNodes() {
		return getMappingNodes(false);
	}
	
	public Set<NodeCoverageModel> getTouchedMappingNodes() {
		return getMappingNodes(true);
	}
	
	private Set<NodeCoverageModel> getHelperNodes(boolean touched) {
		return getNodes(touched, Helper.class);
	}
	
	public Set<NodeCoverageModel> getUntouchedHelperNodes() {
		return getHelperNodes(false);
	}
	
	public Set<NodeCoverageModel> getTouchedHelperNodes() {
		return getHelperNodes(true);
	}
	
	private Set<NodeCoverageModel> getConstructorNodes(boolean touched) {
		return getNodes(touched, Constructor.class);
	}
	
	public Set<NodeCoverageModel> getUntouchedConstructorNodes() {
		return getConstructorNodes(false);
	}
	
	public Set<NodeCoverageModel> getTouchedConstructorNodes() {
		return getConstructorNodes(true);
	}
	
	private Set<NodeCoverageModel> getExpressionNodes(boolean touched) {
		return getNodes(touched, OCLExpression.class);
	}
	
	public Set<NodeCoverageModel> getUntouchedExpressionNodes() {
		return getExpressionNodes(false);
	}
	
	public Set<NodeCoverageModel> getTouchedExpressionNodes() {
		return getExpressionNodes(true);
	}
	
	public RangeSet<Integer> getTouchedRanges() {
		if (touchedRanges == null) {
			touchedRanges = TreeRangeSet.create();
			for (NodeCoverageModel nodeModel : getTouchedExpressionNodes()) {
				touchedRanges.add(Range.closed(nodeModel.getStart(), nodeModel.getEnd() + 1));
			}
			// Now remove untouched ranges
			for (Range<Integer> untouchedRange : getUntouchedRanges().asRanges()) {
				touchedRanges.remove(untouchedRange);
			}
		}
		return touchedRanges;
	}
	
	public RangeSet<Integer> getUntouchedRanges() {
		if (untouchedRanges == null) {
			untouchedRanges = TreeRangeSet.create();
			for (NodeCoverageModel nodeModel : getUntouchedExpressionNodes()) {
				untouchedRanges.add(Range.closed(nodeModel.getStart(), nodeModel.getEnd() + 1));
			}
		}
		return untouchedRanges;
	}
	
	
	public IFile getFile() {
		if (file == null) {
		
			try {
				IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(
						new java.net.URI(getURI().toString()));
				
				// Assume exactly one
				file = files[0];
				
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	public IProject getProject() {
		if (project == null) {
			project = this.getFile().getProject();
		}
		return project;
	}

	public Module getModule() {
		if (module == null) {
			// Check for transformation first
			InternalTransformationExecutor executor = new InternalTransformationExecutor(getURI());
			executor.loadTransformation();
			module = executor.getTransformation();
			
			// Otherwise find library
			if (module == null ) {
			
				CompiledUnit unit = executor.getUnit();
				if (unit != null) {
					for (Module m : unit.getModules()) {
						//TODO: definitely should choose correct one, according to URI
						if (m instanceof Library) module = m;
					}
				}
				
			}
			
			
		}
		return module;
	}
	
	public boolean containsNode(NodeCoverageModel n) {
		return nodes.containsKey(n);
	}

	public NodeCoverageModel getNode(NodeCoverageModel n) {
		return nodes.get(n);
	}

	public void addNode(NodeCoverageModel nodeModel) {
		nodes.put(nodeModel, nodeModel);
	}
	
	public Set<NodeCoverageModel> getUntouchedNodes() {
		Set<NodeCoverageModel> untouched = new HashSet<NodeCoverageModel>();
		
		untouched.addAll(getUntouchedMappingNodes());
		untouched.addAll(getUntouchedHelperNodes());
		untouched.addAll(getUntouchedConstructorNodes());
		untouched.addAll(getUntouchedExpressionNodes());
		
		return untouched;
	}
	
	public Set<NodeCoverageModel> getTouchedNodes() {
		Set<NodeCoverageModel> touched = new HashSet<NodeCoverageModel>();
		
		touched.addAll(getTouchedMappingNodes());
		touched.addAll(getTouchedHelperNodes());
		touched.addAll(getTouchedConstructorNodes());
		touched.addAll(getTouchedExpressionNodes());
		
		return touched;
	}
	
}
