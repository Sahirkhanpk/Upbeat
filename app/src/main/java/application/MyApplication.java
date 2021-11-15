package application;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import java.text.NumberFormat;
import java.util.Locale;


public class MyApplication extends MultiDexApplication {

    public static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 123;
    private static application.MyApplication mAppController;

    public static application.MyApplication getInstance() {
        return mAppController;
    }

    public static NumberFormat getNumberFormat() {
//        String currencyCode = SharedHelper.getKey(getInstance(), "currencyCode", "INR");
//        String currencyCode = SharedHelper.getKey(getInstance(), "currencyCode", GlobalData.profileNew.getCurrency());

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
//        numberFormat.setCurrency(Currency.getInstance(currencyCode));
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    //    Fabric.with(this, new Crashlytics());
//        Stetho.initializeWithDefaults(this);

        mAppController = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
