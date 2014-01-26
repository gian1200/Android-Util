package com.gian1200.util.views;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gian1200.util.R;

public class CardView extends LinearLayout {
	public TextView titleView, subtitleView, contentTextView;
	public FrameLayout contentLayout;
	public ValueAnimator collapseContentAnimator, expandContentAnimator;

	public CardView(Context context) {
		super(context);
		init(context);
	}

	public CardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		initAttributeSet(context, attrs, R.style.Card);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public CardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
		initAttributeSet(context, attrs, defStyle);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.view_card, this, true);
		titleView = (TextView) findViewById(R.id.card_title);
		subtitleView = (TextView) findViewById(R.id.card_subtitle);
		contentLayout = (FrameLayout) findViewById(R.id.card_content_layout);
	}

	private void initAttributeSet(Context context, AttributeSet attrs,
			int defStyle) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CardView, 0, defStyle);
		try {
			titleView.setText(a.getString(R.styleable.CardView_titleText));
			titleView.setTextColor(a
					.getColorStateList(R.styleable.CardView_titleColor));
			if (a.getBoolean(R.styleable.CardView_subtitleVisible, false)) {
				subtitleView.setVisibility(View.VISIBLE);
				subtitleView.setText(a
						.getString(R.styleable.CardView_subtitleText));
				subtitleView.setTextColor(a
						.getColorStateList(R.styleable.CardView_subtitleColor));
			} else {
				subtitleView.setVisibility(View.GONE);
			}
			if (a.getBoolean(R.styleable.CardView_contentColapsed, true)) {
				contentLayout.getLayoutParams().height = 0;
			}

			if (isInEditMode()) {
				LayoutInflater.from(context).inflate(
						R.layout.card_text_content, contentLayout, true);
				contentTextView = (TextView) findViewById(R.id.card_content_text);
				contentTextView.setText(a
						.getString(R.styleable.CardView_contentText));
			} else {
				TypedValue content_type = new TypedValue();
				a.getValue(R.styleable.CardView_contentType, content_type);
				switch (content_type.data) {
				case 0x02:// Text
					LayoutInflater.from(context).inflate(
							R.layout.card_text_content, contentLayout, true);
					contentTextView = (TextView) findViewById(R.id.card_content_text);
					contentTextView.setText(a
							.getString(R.styleable.CardView_contentText));
					break;
				// case 0x01:// custom
				default:
					LayoutInflater.from(context).inflate(
							a.getResourceId(R.styleable.CardView_contentId, 0),
							contentLayout, true);
				}
			}
		} finally {
			a.recycle();
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void expandorCollapseContent() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			if (contentLayout.getHeight() == 0) {
				expandContent();
			} else {
				collapseContent();
			}
		} else {
			if (expandContentAnimator != null
					&& expandContentAnimator.isRunning()) {
				collapseContent();
			} else if (collapseContentAnimator != null
					&& collapseContentAnimator.isRunning()) {
				expandContent();
			} else if (contentLayout.getHeight() == 0) {
				expandContent();
			} else {
				collapseContent();
			}
		}

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void collapseContent() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			contentLayout.getLayoutParams().height = 0;
			contentLayout.requestLayout();
		} else {
			if (expandContentAnimator != null
					&& expandContentAnimator.isRunning()) {
				expandContentAnimator.cancel();
			}
			if (collapseContentAnimator == null) {
				collapseContentAnimator = ValueAnimator.ofInt(
						contentLayout.getHeight(), 0);
			} else {
				if (collapseContentAnimator.isRunning()) {
					return;
				} else {
					collapseContentAnimator.setIntValues(
							contentLayout.getHeight(), 0);
					collapseContentAnimator.removeAllUpdateListeners();
				}
			}
			collapseContentAnimator
					.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
						@Override
						public void onAnimationUpdate(
								ValueAnimator valueAnimator) {
							int value = (Integer) valueAnimator
									.getAnimatedValue();
							contentLayout.getLayoutParams().height = value;
							contentLayout.requestLayout();
						}
					});
			collapseContentAnimator.start();
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void expandContent() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			contentLayout.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
			contentLayout.requestLayout();
		} else {
			if (collapseContentAnimator != null
					&& collapseContentAnimator.isRunning()) {
				collapseContentAnimator.cancel();
			}
			contentLayout.measure(MeasureSpec.makeMeasureSpec(
					getMeasuredWidth(), MeasureSpec.AT_MOST), MeasureSpec
					.makeMeasureSpec(LayoutParams.WRAP_CONTENT,
							MeasureSpec.UNSPECIFIED));
			if (expandContentAnimator == null) {
				expandContentAnimator = ValueAnimator.ofInt(
						contentLayout.getHeight(),
						contentLayout.getMeasuredHeight());
			} else {
				if (expandContentAnimator.isRunning()) {
					return;
				} else {
					expandContentAnimator.setIntValues(
							contentLayout.getHeight(),
							contentLayout.getMeasuredHeight());
					expandContentAnimator.removeAllUpdateListeners();
				}
			}
			expandContentAnimator
					.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
						@Override
						public void onAnimationUpdate(
								ValueAnimator valueAnimator) {
							int value = (Integer) valueAnimator
									.getAnimatedValue();
							contentLayout.getLayoutParams().height = value;
							contentLayout.requestLayout();
						}
					});
			expandContentAnimator.start();
		}
	}

}
