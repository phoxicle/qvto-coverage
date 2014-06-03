package de.phei.qvto.coverage.launching;

import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestRunSession;

import de.phei.qvto.coverage.CoveragePlugin;

public class CoverageTestRunListener extends TestRunListener {
	
	public final static CoverageTestRunListener INSTANCE = new CoverageTestRunListener();

	@Override
	public void sessionFinished(ITestRunSession session) {
		// Show view
		CoveragePlugin.getDefault().showCoverageView();
		JUnitCore.removeTestRunListener(INSTANCE);
	}
	
	@Override
	public void testCaseFinished(ITestCaseElement testCaseElement) {
		// Log event
		CoveragePlugin.getTracker().log("Test case finished");
	}

}
