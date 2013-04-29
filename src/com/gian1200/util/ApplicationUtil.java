package com.gian1200.util;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.preference.PreferenceManager;

public abstract class ApplicationUtil extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		loadData();
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void saveData() {
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this)
				.edit();
		saveData(editor);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	public void loadData() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		loadData(sharedPreferences);
	}

	abstract void loadData(SharedPreferences sharedPreferences);

	abstract void saveData(Editor editor);
}
