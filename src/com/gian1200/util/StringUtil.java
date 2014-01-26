package com.gian1200.util;

import android.content.Context;

public class StringUtil {
	public static String getMarketAndroidLink(Context context) {
		return context.getString(R.string.market_android_link,
				context.getPackageName());
	}

	public static String getMarketWebLink(Context context) {
		return context.getString(R.string.market_web_link,
				context.getPackageName());
	}

	public static String getLicenseApache2_0(Context context, String year,
			String copyrightOwner) {
		return context.getString(R.string.apache_license_2_0, year,
				copyrightOwner);
	}

	public static String getAndroidUtilLicense(Context context) {
		return getLicenseApache2_0(context, "2014",
				context.getString(R.string.android_util_author_name));
	}
}
