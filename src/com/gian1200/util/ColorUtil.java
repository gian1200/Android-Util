package com.gian1200.util;

import android.graphics.Color;

public class ColorUtil {

	public static int getColorBetween(int initialColor, int finalColor,
			float percentage) {
		int temp = 0;
		temp |= Math.round(Color.alpha(initialColor)
				+ (Color.alpha(finalColor) - Color.alpha(initialColor))
				* percentage) << 24;
		temp |= Math.round(Color.red(initialColor)
				+ (Color.red(finalColor) - Color.red(initialColor))
				* percentage) << 16;
		temp |= Math.round(Color.green(initialColor)
				+ (Color.green(finalColor) - Color.green(initialColor))
				* percentage) << 8;
		temp |= Math.round(Color.blue(initialColor)
				+ (Color.blue(finalColor) - Color.blue(initialColor))
				* percentage);
		return temp;
	}
}
