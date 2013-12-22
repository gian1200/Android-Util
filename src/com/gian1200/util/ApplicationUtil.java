package com.gian1200.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
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

	public void loadData() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		loadData(sharedPreferences);
	}

	@SuppressLint("CommitPrefEdits")
	public void saveData() {
		final Editor editor = PreferenceManager.getDefaultSharedPreferences(
				this).edit();
		saveData(editor);
		applyChanges(editor);
	}

	/**
	 * Removes the desired values defined by {@link #removeData(Editor)
	 * removeData(Editor)}. The default SharedPreference is used.
	 * 
	 * @param editor
	 *            Interface which contains the values to be removed
	 */

	@SuppressLint("CommitPrefEdits")
	public void removeData() {
		final Editor editor = PreferenceManager.getDefaultSharedPreferences(
				this).edit();
		removeData(editor);
		applyChanges(editor);
	}

	@SuppressLint("CommitPrefEdits")
	public void deleteAllData() {
		final Editor editor = PreferenceManager.getDefaultSharedPreferences(
				this).edit();
		editor.clear();
		applyChanges(editor);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private void applyChanges(final Editor editor) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					editor.commit();
					return null;
				}
			}.execute();
		} else {
			editor.apply();
		}
	}

	/**
	 * Prints in the LogCat the Hash of the used key (Debug or Release). Useful
	 * if your app uses Facebook SDK (put the hash in your Developer Console) or
	 * any other services which requires the Hash
	 * 
	 */

	public void printHash() {
		Log.w("hash key",
				"Do not call this method in your release version: ApplicationUtil.printHash()");
		PackageInfo info;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(),
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md;
				md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String something = new String(Base64.encode(md.digest(),
						Base64.DEFAULT));
				// String something = new
				// String(Base64.encodeBytes(md.digest()));
				Log.w("hash key", something);
			}
		} catch (NameNotFoundException e) {
			Log.e("NameNotFoundException", e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			Log.e("NoSuchAlgorithmException", e.getMessage(), e);
		} catch (Exception e) {
			Log.e("Exception", e.getMessage(), e);
		}
	}

	public String getApplicationVersionName() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e("NameNotFoundException", e.getMessage(), e);
		}
		return null;
	}

	public String getMarketAndroidLink() {
		return getString(R.string.market_android_link, getPackageName());
	}

	public String getMarketWebLink() {
		return getString(R.string.market_web_link, getPackageName());
	}

	/**
	 * Override this method to use {@link #removeData() removeData()} and remove
	 * the desired values using the appropriate keys. The default
	 * SharedPreference is used.
	 * 
	 * @param editor
	 *            Interface which contains the values to be removed
	 */

	protected abstract void removeData(Editor editor);

	/**
	 * Override this method to load all the desired values using the appropriate
	 * keys. The default SharedPreference is used.
	 * 
	 * @param sharedPreferences
	 *            Interface to access the data to be loaded
	 */

	protected abstract void loadData(SharedPreferences sharedPreferences);

	/**
	 * Override this method to save all the desired values using the appropriate
	 * keys. The default SharedPreference is used.
	 * 
	 * @param editor
	 *            Interface to save desired values
	 */

	protected abstract void saveData(Editor editor);
}
