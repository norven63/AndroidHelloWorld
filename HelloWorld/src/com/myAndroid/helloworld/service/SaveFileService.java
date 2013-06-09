package com.myAndroid.helloworld.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;

public class SaveFileService {
	private Context context;

	public SaveFileService(Context context) {
		super();
		this.context = context;
	}

	public void saveFile(String fileName, String content) throws Exception{
		FileOutputStream outStream = context.openFileOutput(fileName,Context.MODE_PRIVATE);
		outStream.write(content.getBytes());
		outStream.close();
	}

}
