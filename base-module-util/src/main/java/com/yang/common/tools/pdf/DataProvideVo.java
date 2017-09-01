package com.yang.common.tools.pdf;

import java.util.HashMap;

public class DataProvideVo {
	
	private HashMap<String, Object> allData;

	private String fileName;

	public HashMap<String, Object> getAllData() {
		return allData;
	}

	public void setAllData(HashMap<String, Object> allData) {
		this.allData = allData;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
