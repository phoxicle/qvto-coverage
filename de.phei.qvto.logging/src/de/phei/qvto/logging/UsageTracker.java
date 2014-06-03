package de.phei.qvto.logging;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class UsageTracker {

	private static UsageTracker instance;
	private Logger logger;
	
	public UsageTracker(Class c) {
		String key = c.getPackage().getName();
		
		// Initialize logger
		logger = Logger.getLogger(key);  
		String filename = key + ".log";
		
		// Create log dir if needed
		File dir = new File("logs");
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		File file = new File(dir + "/" + filename);
		String path = file.getAbsolutePath();
		
        try {  
        	// Attach log file
        	FileHandler fh = new FileHandler(path.toString(), true);  
            logger.addHandler(fh);  
            fh.setFormatter(new SimpleFormatter());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
	}
	
	public void log(String s) {
        logger.info(s);  
	}
	
}
