package com.gian1200.util.billing;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.gian1200.util.billing.IabHelper.OnConsumeFinishedListener;
import com.gian1200.util.billing.IabHelper.OnIabPurchaseFinishedListener;
import com.gian1200.util.billing.IabHelper.QueryInventoryFinishedListener;

public class BillingManager {
	boolean mDebugLog = false;
	final String mDebugTag = "BillingManager";
	IabHelper mHelper;
	// (arbitrary) request code for the purchase flow
	final int RC_REQUEST;

	QueryInventoryFinishedListener mGotInventoryListener = new QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
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
					mHelper.consumeAsync(inventory.getPurchase(sku),
							mConsumeFinishedListener);
				}
			}
		}
	};

	// Callback for when a purchase is finished
	OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			logDebug("Purchase finished: " + result + ", purchase: " + purchase);
			if (result.isSuccess()) {
				// final String sku = purchase.getSku();
				// TODO validar con un IF
				// if item is consumable, consume
				mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			} else {
			}
		}
	};

	// Called when consumption is complete
	OnConsumeFinishedListener mConsumeFinishedListener = new OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			logDebug("Consumption finished. Purchase: " + purchase
					+ ", result: " + result);
			if (result.isSuccess()) {
				// hacer la lógica por comprar
				// final String sku = purchase.getSku();
			} else {
			}
		}
	};

	public BillingManager(Context context, String base64EncodedPublicKey) {
		this(context, base64EncodedPublicKey, 10001);
	}

	public BillingManager(Context context, String base64EncodedPublicKey,
			int RC_REQUEST) {
		this.RC_REQUEST = RC_REQUEST;
		mHelper = new IabHelper(context, base64EncodedPublicKey);
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (result.isSuccess()) {
					mHelper.queryInventoryAsync(mGotInventoryListener);
				} else {
					// Oh noes, there was a problem.
					Log.e(mDebugTag, result.toString());
				}
			}
		});
	}

	void setDebugLogging(boolean isdebugable) {
		mDebugLog = isdebugable;
		mHelper.enableDebugLogging(mDebugLog);
	}

	void logDebug(final String msg) {
		if (mDebugLog) {
			Log.d(mDebugTag, msg);
		}
	}

	void destroyIabHelper() {
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}
	}
}
