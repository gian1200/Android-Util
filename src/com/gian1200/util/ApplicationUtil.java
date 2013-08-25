package com.gian1200.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

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

	public void printHash() {
		PackageInfo info;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(),
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md;
				md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String something = new String(Base64.encode(md.digest(), 0));
				// String something = new
				// String(Base64.encodeBytes(md.digest()));
				Log.v("hash key", something);
			}
		} catch (NameNotFoundException e) {
			Log.e("name not found", e.toString());
		} catch (NoSuchAlgorithmException e) {
			Log.e("no such an algorithm", e.toString());
		} catch (Exception e) {
			Log.e("exception", e.toString());
		}
	}

	protected abstract void loadData(SharedPreferences sharedPreferences);

	protected abstract void saveData(Editor editor);
}
