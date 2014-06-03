package de.phei.qvto.coverage.runtime;

import org.eclipse.emf.common.util.URI;
import org.eclipse.m2m.internal.qvt.oml.ast.env.InternalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.compiler.ExeXMISerializer;
import org.eclipse.m2m.internal.qvt.oml.evaluator.InternalEvaluator;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtGenericVisitorDecorator;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtRuntimeException;
import org.eclipse.m2m.internal.qvt.oml.expressions.Module;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;
import org.eclipse.ocl.ecore.impl.PropertyCallExpImpl;
import org.eclipse.ocl.utilities.ASTNode;


import de.phei.qvto.coverage.common.CoverageData;
import de.phei.qvto.coverage.common.TransformationCoverageData;

@SuppressWarnings("restriction")
public class QVTOCoverageDecorator extends QvtGenericVisitorDecorator {

	
	
	public QVTOCoverageDecorator(InternalEvaluator qvtExtVisitor) {
		super(qvtExtVisitor);
	}
	
	@Override
	protected Object genericPreVisitAST(ASTNode element) {
		getTransformationCoverageData().touch(element);
		return super.genericPreVisitAST(element);
	}
	
	@Override
	public Object execute(OperationalTransformation transformation)
			throws QvtRuntimeException {
		Object ret = getInternalEvalDelegate().execute(transformation);
		
		// Save after each execution
		CoverageData.getInstance().save();
		return ret;
	}
	
	private TransformationCoverageData getTransformationCoverageData() {
		
		QvtOperationalEvaluationEnv evalEnv = (QvtOperationalEvaluationEnv) getEvaluationEnvironment();
		InternalEvaluationEnv internEnv = evalEnv.getAdapter(InternalEvaluationEnv.class);
		
		TransformationCoverageData transformationData = null;
		
		if(internEnv.getCurrentModule() != null) {
			Module module = internEnv.getCurrentModule().getModule();
			
			URI compiledUri = module.eResource().getURI(); // .qvtox
			URI uri = ExeXMISerializer.toSourceUnitURI(compiledUri); // .qvto
			
			
			
			// See if we already have a data for that module.
			for (TransformationCoverageData transData : CoverageData.getInstance().getTransformationDatas()) {
				if (transData.getURI() == uri) {
					transformationData = transData;
				}
			}
			
			// Otherwise create a new one
			if (transformationData == null) {
				transformationData = new TransformationCoverageData(uri);
				// And store it in CoverageData, since that's what gets saved later.
				CoverageData.getInstance().addTransformationData(transformationData);
			}
		} else {
			System.out.println("no current module module :(");
		}
		return transformationData;
	}
	
}
