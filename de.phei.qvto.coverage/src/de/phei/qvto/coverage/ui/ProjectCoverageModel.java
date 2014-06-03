package de.phei.qvto.coverage.ui;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;

public class ProjectCoverageModel {

	IProject project;
	
	Set<TransformationCoverageModel> transformationModels = new HashSet<TransformationCoverageModel>();
	
	public ProjectCoverageModel(IProject p) {
		project = p;
	}
	
	public IProject getProject() {
		return project;
	}

	public void addTransformationModel(TransformationCoverageModel transModel) {
		transformationModels.add(transModel);
	}
	
	public TransformationCoverageModel[] getTransformationModels() {
		return transformationModels.toArray(new TransformationCoverageModel[transformationModels.size()]);
	}
	
	public Set<NodeCoverageModel> getUntouchedMappingNodes() {
		Set<NodeCoverageModel> nodes = new HashSet<NodeCoverageModel>();
		for (TransformationCoverageModel t : transformationModels) {
			nodes.addAll(t.getUntouchedMappingNodes());
		}
		return nodes;
	}
	
	public Set<NodeCoverageModel> getTouchedMappingNodes() {
		Set<NodeCoverageModel> nodes = new HashSet<NodeCoverageModel>();
		for (TransformationCoverageModel t : transformationModels) {
			nodes.addAll(t.getTouchedMappingNodes());
		}
		return nodes;
	}
	
	public Set<NodeCoverageModel> getUntouchedHelperNodes() {
		Set<NodeCoverageModel> nodes = new HashSet<NodeCoverageModel>();
		for (TransformationCoverageModel t : transformationModels) {
			nodes.addAll(t.getUntouchedHelperNodes());
		}
		return nodes;
	}
	
	public Set<NodeCoverageModel> getTouchedHelperNodes() {
		Set<NodeCoverageModel> nodes = new HashSet<NodeCoverageModel>();
		for (TransformationCoverageModel t : transformationModels) {
			nodes.addAll(t.getTouchedHelperNodes());
		}
		return nodes;
	}

	public Set<NodeCoverageModel> getUntouchedConstructorNodes() {
		Set<NodeCoverageModel> nodes = new HashSet<NodeCoverageModel>();
		for (TransformationCoverageModel t : transformationModels) {
			nodes.addAll(t.getUntouchedConstructorNodes());
		}
		return nodes;
	}
	
	public Set<NodeCoverageModel> getTouchedConstructorNodes() {
		Set<NodeCoverageModel> nodes = new HashSet<NodeCoverageModel>();
		for (TransformationCoverageModel t : transformationModels) {
			nodes.addAll(t.getTouchedConstructorNodes());
		}
		return nodes;
	}
	
	public Set<NodeCoverageModel> getUntouchedExpressionNodes() {
		Set<NodeCoverageModel> nodes = new HashSet<NodeCoverageModel>();
		for (TransformationCoverageModel t : transformationModels) {
			nodes.addAll(t.getUntouchedExpressionNodes());
		}
		return nodes;
	}
	
	public Set<NodeCoverageModel> getTouchedExpressionNodes() {
		Set<NodeCoverageModel> nodes = new HashSet<NodeCoverageModel>();
		for (TransformationCoverageModel t : transformationModels) {
			nodes.addAll(t.getTouchedExpressionNodes());
		}
		return nodes;
	}
	
	public Set<NodeCoverageModel> getTouchedNodes() {
		Set<NodeCoverageModel> nodes = new HashSet<NodeCoverageModel>();
		nodes.addAll(getTouchedMappingNodes());
		nodes.addAll(getTouchedHelperNodes());
		nodes.addAll(getTouchedConstructorNodes());
		nodes.addAll(getTouchedExpressionNodes());
		return nodes;
	}
	
	public Set<NodeCoverageModel> getUntouchedNodes() {
		Set<NodeCoverageModel> nodes = new HashSet<NodeCoverageModel>();
		nodes.addAll(getUntouchedMappingNodes());
		nodes.addAll(getUntouchedHelperNodes());
		nodes.addAll(getUntouchedConstructorNodes());
		nodes.addAll(getUntouchedExpressionNodes());
		return nodes;
	}
	
}
