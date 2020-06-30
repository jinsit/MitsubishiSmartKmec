package com.jinsit.kmec.comm.jinLib;

import java.io.File;

import android.util.Log;

public class RemoveSDcard {

	public static void removeFiles()
	{
		
		Log.w("MUPDF", "removeFile start");
		File dir = new File(AbsoluteFilePath.PDF_PATH);
		String[] fileNames = dir.list();
		if (fileNames != null) {
			for (String fileName : fileNames) {
				File f = new File(AbsoluteFilePath.PDF_PATH + fileName);
				if (f.exists()) {
					f.delete();
				}
			}
		}
		
		Log.w("MUPDF", "removeFile end");
	}
	
}
