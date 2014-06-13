package de.phei.qvto.coverage.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CoverageData implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static String DIR_SEP = System.getProperty("file.separator");
	private static String FILE_PATH = "coveragedatanew.ser";
	
	public static String DIR_PATH = System.getProperty("java.io.tmpdir") + "CoverageData";
	public static String TRANS_DIR_PATH = DIR_PATH + DIR_SEP + "TransDatas";
	

	private static CoverageData instance;
	
	private List<TransformationCoverageData> transformations;
	
	public CoverageData() {
		transformations = new ArrayList<TransformationCoverageData>();
	}
	
	public static CoverageData getInstance() {
		if (instance == null) {
			instance = new CoverageData();
		}
		return instance;
	}
	
	public void addTransformationData(TransformationCoverageData data) {
		transformations.add(data);
	}
	
	public TransformationCoverageData[] getTransformationDatas() {
		return transformations.toArray(new TransformationCoverageData[transformations.size()]);
	}
	
	public CoverageData retrieve() {
		try{
			
			ObjectInputStream os = new ObjectInputStream(new FileInputStream(DIR_PATH + DIR_SEP + CoverageData.FILE_PATH));
			CoverageData coverageData = (CoverageData) os.readObject();
			os.close();
			
			// Since transDatas were saved separately, reattach them now
			File folder = new File(TRANS_DIR_PATH);
			for (File transDataFile : folder.listFiles()) {
				ObjectInputStream stream = new ObjectInputStream(new FileInputStream(transDataFile));
				TransformationCoverageData transData = (TransformationCoverageData) stream.readObject();
				stream.close();
				coverageData.addTransformationData(transData);
			}
			
			instance = coverageData;
			return coverageData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void save() {
		try {
			// Note, directories are assumed to exist since prepareDirectories is called in the launch
			
			// Save any transdatas in a new file
			for (TransformationCoverageData transData : getTransformationDatas()) {
				ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(TRANS_DIR_PATH + DIR_SEP + transData.hashCode()));
				os.writeObject(transData);
				os.close();
			}
			
			// Now remove transdatas and save myself
			transformations.clear();
			
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(DIR_PATH + DIR_SEP + CoverageData.FILE_PATH));
			os.writeObject(this);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void prepareDirectories() {
		// First make sure directory exists, and if not, create it
		File dir = new File(DIR_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		File transDir = new File(CoverageData.TRANS_DIR_PATH);
		if (!transDir.exists()) {
			transDir.mkdirs();
		} else {
			// Make sure this is empty
			for(File file: transDir.listFiles()) file.delete();
		}
		
	}
	
}
