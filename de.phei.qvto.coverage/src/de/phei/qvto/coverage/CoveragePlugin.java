package de.phei.qvto.coverage;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.phei.qvto.coverage.common.CoverageData;
import de.phei.qvto.coverage.ui.CoverageModel;
import de.phei.qvto.coverage.ui.CoverageView;
import de.phei.qvto.logging.UsageTracker;

/**
 * The activator class controls the plug-in life cycle
 */
public class CoveragePlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "de.phei.qvto.coverage";
	public static final String RUNTIME_PLUGIN_ID = "de.phei.qvto.coverage.runtime";
	public static final String COMMON_PLUGIN_ID = "de.phei.qvto.coverage.common";
	
	private static UsageTracker tracker;
	

	private static CoveragePlugin plugin;
	
	//The identifiers for the preferences	
	public static final String LOW_THRESHOLD_PREFERENCE = "de.phei.qvto.coverage.threshold.low";
	public static final String HIGH_THRESHOLD_PREFERENCE = "de.phei.qvto.coverage.threshold.high";

	//The default values for the preferences
	public static final int DEFAULT_LOW_THRESHOLD = 30;
	public static final int DEFAULT_HIGH_THRESHOLD = 90;

	public CoveragePlugin() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static CoveragePlugin getDefault() {
		return plugin;
	}
	
	protected void initializeDefaultPreferences(IPreferenceStore store) {
		store.setDefault(LOW_THRESHOLD_PREFERENCE, DEFAULT_LOW_THRESHOLD);
		store.setDefault(HIGH_THRESHOLD_PREFERENCE, DEFAULT_HIGH_THRESHOLD);
	}
	
	public static UsageTracker getTracker() {
		if (tracker == null) {
			tracker = new UsageTracker(CoveragePlugin.class);
		}
		return tracker;
	}

	public void showCoverageView() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage().showView(CoverageView.VIEW_ID);
					CoverageView view = (CoverageView) PlatformUI
							.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage().findView(CoverageView.VIEW_ID);
					view.setLoading(true);

				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});

		CoverageData coverageData = CoverageData.getInstance().retrieve();
		final CoverageModel coverageModel = new CoverageModel(coverageData);

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				// Note that we don't have a guarantee the async block above has run first... we just assume.
				CoverageView view = (CoverageView) PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.findView(CoverageView.VIEW_ID);
				view.setLoading(false);
				view.update(coverageModel);

				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().activate(view);
			}
		});
	}

}
