package de.phei.qvto.coverage;


import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The page for setting debugger preferences.  Built on the 'field editor' infrastructure.
 */
public class QVTOCoveragePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public QVTOCoveragePreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		
		IPreferenceStore store = CoveragePlugin.getDefault().getPreferenceStore();
		setPreferenceStore(store);
	}
	
	public void createControl(Composite parent) {
		super.createControl(parent);
	}
	
	@Override
	protected void createFieldEditors() {
		IntegerFieldEditor highThreshold = new IntegerFieldEditor(
				CoveragePlugin.HIGH_THRESHOLD_PREFERENCE,
				"&High coverage threshold:",
		 		getFieldEditorParent());
		highThreshold.setValidRange(0, 100);
		addField(highThreshold);	
		
		IntegerFieldEditor lowThreshold = new IntegerFieldEditor(
				CoveragePlugin.LOW_THRESHOLD_PREFERENCE,
				"&Low coverage threshold:",
		 		getFieldEditorParent());
		lowThreshold.setValidRange(0, 100);
		addField(lowThreshold);	
	}
	
	public void init(IWorkbench workbench) {}
				
}

