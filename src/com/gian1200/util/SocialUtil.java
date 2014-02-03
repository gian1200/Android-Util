package com.gian1200.util;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

public class SocialUtil {

	// url URL of the page to share
	// via Screen name of the user to attribute the Tweet to
	// text Default Tweet text
	// related Related accounts
	// count Count box position
	// lang The language for the Tweet Button
	// counturl URL to which your shared URL resolves
	// hashtags Comma separated hashtags appended to tweet text
	// size The size of the rendered button
	// dnt See this section for information
	// https://dev.twitter.com/docs/tweet-button
	public static void tweet(Context context, Bundle bundle) {
		String tweetUrl = "https://twitter.com/intent/tweet?";
		final ArrayList<String> params = new ArrayList<String>(bundle.size());
		for (String key : bundle.keySet()) {
			params.add(key + "=" + bundle.get(key));
		}
		tweetUrl += TextUtils.join("&", params);
		Uri uri = Uri.parse(tweetUrl);
		context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
	}

	public static void tweet(Context context, CharSequence text) {
		Bundle bundle = new Bundle();
		bundle.putCharSequence("text", text);
		tweet(context, bundle);
	}

}
