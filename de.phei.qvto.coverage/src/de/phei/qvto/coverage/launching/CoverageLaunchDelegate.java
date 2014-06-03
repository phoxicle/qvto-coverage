package de.phei.qvto.coverage.launching;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.internal.junit.JUnitCorePlugin;
import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.jdt.junit.launcher.JUnitLaunchConfigurationDelegate;
import org.osgi.framework.Bundle;

import de.phei.qvto.coverage.CoveragePlugin;
import de.phei.qvto.coverage.common.CoverageData;

@SuppressWarnings("restriction")
public class CoverageLaunchDelegate extends JUnitLaunchConfigurationDelegate {
	
	public synchronized void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		
		// Add test listener
		JUnitCore.addTestRunListener(CoverageTestRunListener.INSTANCE);
		
		// Log event
		CoveragePlugin.getTracker().log("Launched coverage delegate for project: " + super.getJavaProjectName(configuration));
		
		// Directories used to save information that needs to be passed between this plugin and the code run by the tests
		CoverageData.prepareDirectories();
		
		super.launch(configuration, mode, launch, monitor);
	}
	
	@Override
	public String verifyMainTypeName(ILaunchConfiguration configuration) throws CoreException {
		return CoveragePlugin.RUNTIME_PLUGIN_ID + ".Shell"; //$NON-NLS-1$
	}
	
	@Override
	public String[] getClasspath(ILaunchConfiguration configuration) throws CoreException {
		String[] cp = super.getClasspath(configuration);

		String[] bundles = new String[] {
				CoveragePlugin.COMMON_PLUGIN_ID,
				CoveragePlugin.RUNTIME_PLUGIN_ID,
		};
		
		List<String> classList = new ArrayList(Arrays.asList(cp));
		
		try {
			for (int i =0; i < bundles.length; i++) {
				Bundle bundle = JUnitCorePlugin.getDefault().getBundle(bundles[i]);
				URL url = bundle.getEntry("/");
				
				classList.add(0, FileLocator.toFileURL(url).getFile());
				
				//TODO Seems needed for runtime eclipse...
				classList.add(0, FileLocator.toFileURL(url).getFile() + "bin/");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return classList.toArray(new String[classList.size()]);
	}
	
}
