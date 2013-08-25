package com.gian1200.util.billing;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gian1200.util.billing.IabHelper.OnConsumeFinishedListener;
import com.gian1200.util.billing.IabHelper.OnIabPurchaseFinishedListener;
import com.gian1200.util.billing.IabHelper.OnIabSetupFinishedListener;
import com.gian1200.util.billing.IabHelper.QueryInventoryFinishedListener;

public class BillingManager {
	boolean mDebugLog = false;
	final String mDebugTag = "BillingManager";
	public IabHelper mHelper;
	public String[] consumablesSKUs = new String[] {},
			nonConsumablesSKUs = new String[] {};

	// (arbitrary) request code for the purchase flow
	final int RC_REQUEST;

	public QueryInventoryFinishedListener mGotInventoryListener = new QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			BillingManager.this.onQueryInventoryFinished(result, inventory);
		}
	};

	// Callback for when a purchase is finished
	public OnIabPurchaseFinishedListener mPurchaseFinishedListener = new OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			BillingManager.this.onPurchaseFinished(result, purchase);
		}
	};

	// Called when consumption is complete
	public OnConsumeFinishedListener mConsumeFinishedListener = new OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			BillingManager.this.onConsumeFinished(purchase, result);
		}
	};

	public BillingManager(Context context, String base64EncodedPublicKey) {
		this(context, base64EncodedPublicKey, 10001);
	}

	public BillingManager(Context context, String base64EncodedPublicKey,
			int RC_REQUEST) {
		this.RC_REQUEST = RC_REQUEST;
		mHelper = new IabHelper(context, base64EncodedPublicKey);
	}

	public void startSetup() {
		mHelper.startSetup(new OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (result.isSuccess()) {
					mHelper.queryInventoryAsync(mGotInventoryListener);
				} else {
					// Oh noes, there was a problem.
					logError(result.toString());
				}
			}
		});
	}

	public void setDebugLogging(boolean isdebugable) {
		mDebugLog = isdebugable;
		mHelper.enableDebugLogging(mDebugLog);
	}

	void logDebug(final String msg) {
		if (mDebugLog) {
			Log.d(mDebugTag, msg);
		}
	}

	void logError(String msg) {
		if (mDebugLog) {
			Log.e(mDebugTag, msg);
		}
	}

	public void destroyIabHelper() {
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}
	}

	public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
		logDebug("Query inventory finished");
		if (result.isSuccess()) {
			logDebug("Query inventory was successful");
			// for (InAppProduct prod : inAppReveals) {
			// logDebug("Si hay producto");
			// if (inventory.hasDetails(prod.sku)) {
			// prod.price = inventory.getSkuDetails(prod.sku)
			// .getPrice();
			// logDebug("Precio: " + prod.price);
			// break;
			// }
			// }

			final List<String> allOwnedSkus = inventory.getAllOwnedSkus();
			for (String sku : allOwnedSkus) {
				logDebug("There is a consumable product");
				for (String conSKU : consumablesSKUs) {
					if (conSKU.equals(sku)) {
						mHelper.consumeAsync(inventory.getPurchase(sku),
								mConsumeFinishedListener);
					}
				}

			}
		} else {
			logError("Result wasn't successful.");
		}
	}

	public void onPurchaseFinished(IabResult result, Purchase purchase) {
		logDebug("Purchase finished: " + result + ", purchase: " + purchase);
		if (result.isSuccess()) {
			logDebug("Purchase was successful");

			// final String sku = purchase.getSku();
			// if item is consumable, consume
			for (String conSKU : consumablesSKUs) {
				if (conSKU.equals(purchase.mSku)) {
					mHelper.consumeAsync(purchase, mConsumeFinishedListener);
				}
			}
		} else {
			logError("Result wasn't successful.");
		}
	}

	public void onConsumeFinished(Purchase purchase, IabResult result) {
		logDebug("Consumption finished. Purchase: " + purchase + ", result: "
				+ result);
		if (result.isSuccess()) {
			logDebug("Consumption was successful");
			// hacer la lógica por comprar
			// final String sku = purchase.getSku();
		} else {
			logError("Result wasn't successful.");
		}
	}

	public void launchPurchaseFlow(Activity activity, String sku) {
		mHelper.launchPurchaseFlow(activity, sku, RC_REQUEST,
				mPurchaseFinishedListener);
	}

	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		return mHelper.handleActivityResult(requestCode, resultCode, data);
	}
}
