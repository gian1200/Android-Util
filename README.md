#Android-Util

## Developer

 * gian1200

## Includes

 * Android Billing v3 Library (http://developer.android.com/google/play/billing/api.html)
 * Youtube Api 1.0.0 (https://developers.google.com/youtube/android/player/)

## Prerequisites

 * Android SDK updated

## How to setup

 * Create a new Android Project From Existing Code in Eclipse 
 * Link with favorite Project by adding this one as a library
 * Enjoy!

## Billing Manager

 The Android Billing Manager is built on top of [marketbilling] (https://code.google.com/p/marketbilling/).
 
### How to use Billing Manager

 Extend the Application class (or even better, the ApplicationUtil) if you haven't done it already. 

```java
public class ExampleApplication extends ApplicationUtil {

	BillingManager billingManager;
	final String SKU_consumable1 = "SKU_asdf", SKU_consumable2 = "SKU_qwerty", SKU_nonConsumable1 = "SKU_lalalala";

	@Override
	public void onCreate() {
		super.onCreate();
		final String base64EncodedPublicKey = "Insert here the LICENCE KEY for the application or generate it to improve security";
		billingManager = new BillingManager(this, base64EncodedPublicKey);
		billingManager.setDebugLogging(true);// optional to debug with LogCat
		billingManager.noConsumablesSKUs = new String[] { SKU_nonConsumable1 };
		billingManager.consumablesSKUs = new String[] { SKU_consumable1, SKU_consumable2 };
		billingManager.mGotInventoryListener = new QueryInventoryFinishedListener() {
			public void onQueryInventoryFinished(IabResult result,
					Inventory inventory) {
				billingManager.onQueryInventoryFinished(result, inventory); //very important to consume "consumable purchased products"
				if (result.isSuccess()) {
					//verify which products have already been purchased. (only non-Consumables)
					if (inventory.hasPurchase(SKU_nonConsumable1)) {
						//do something
					}
					saveData();
				}
			}
		};
		// only needed if you have non-consumable items
		billingManager.mPurchaseFinishedListener = new OnIabPurchaseFinishedListener() {
			public void onIabPurchaseFinished(IabResult result,
					Purchase purchase) {
				billingManager.onPurchaseFinished(result, purchase);// Very important to consume "consumable purchased products"
				if (result.isSuccess()) {
					//verify which product have been purchased (only non-Consumables).
					if (purchase.getSku().equals(SKU_nonConsumable1)) {
						//do something
					}
					//then save
					saveData();
				}
			}
		};
		// only needed if you have consumable items
		billingManager.mConsumeFinishedListener = new OnConsumeFinishedListener() {
			public void onConsumeFinished(Purchase purchase, IabResult result) {
				//billingManager.onConsumeFinished(purchase, result);// You don't need this like the other 2 above
				if (result.isSuccess()) {
					String sku = purchase.getSku();
					//check which product have this SKU and do your magic
					//then save
					saveData();
				}
			}
		};
		//Don't forget this. VERY IMPORTANT
		billingManager.startSetup();
	}
}
```

 Add it to your Manifest
 
 ```xml
 <?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

	<application
		android:name="YOURPACKAGE.ExampleApplication">
		...
	</application>

</manifest>
```

 Override this method in the last activity that will be closed in your application (it is most likely to be your main activity or root activity).
 
```java
	@Override
	protected void onDestroy() {
		((ExampleApplication) getApplication()).billingManager.destroyIabHelper();
		super.onDestroy();
	}
```
 
 Finally, in order to trigger a purchase, you only need to use 1 single line of code

```java
	((ExampleApplication) getApplication()).billingManager.launchPurchaseFlow(this, "SKU of the product");
```

##Final message
Feel free to ask any doubt you have. Just send me a message, email me or use smoke signals!