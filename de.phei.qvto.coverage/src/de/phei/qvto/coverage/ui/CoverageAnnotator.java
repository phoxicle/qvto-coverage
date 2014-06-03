package de.phei.qvto.coverage.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;

public class CoverageAnnotator {
	
	final private static String TOUCHED_MARKER_TYPE = "de.phei.qvto.coverage.markers.touchedMarker";
	final private static String UNTOUCHED_MARKER_TYPE = "de.phei.qvto.coverage.markers.untouchedMarker";
	
	public boolean BUSY = false;
	
	public void annotate(IFile file, RangeSet<Integer> ranges, String markerType, String message) {
		BUSY = true;
		try {
			// Remove any markers from previous runs
			file.deleteMarkers(markerType, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
		
		for(Range<Integer> range : ranges.asRanges()) {
			
			int start = range.lowerEndpoint();
			int end = range.upperEndpoint();
			
			try {
				IMarker m = file.createMarker(markerType);
				m.setAttribute(IMarker.CHAR_START, start);
		    	m.setAttribute(IMarker.CHAR_END, end);
		    	if (message.length() > 0) {
		    		m.setAttribute(IMarker.MESSAGE, message);
		    	}
		    	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		BUSY = false;
	}
	
	public void annotateTouched(IFile file, RangeSet<Integer> ranges) {
		annotate(file,ranges, TOUCHED_MARKER_TYPE, "");
	}
	
	public void annotateUntouched(IFile file, RangeSet<Integer> ranges) {
		annotate(file,ranges, UNTOUCHED_MARKER_TYPE, "");
	}
	
}
