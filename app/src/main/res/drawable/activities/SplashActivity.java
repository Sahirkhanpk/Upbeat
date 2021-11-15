package activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.excellabs.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import controller.GetProfile;
import controller.ProfileListener;
import helper.ConnectionHelper;
import helper.CustomDialog;
import helper.GlobalData;
import helper.SharedHelper;
import model.Profile;
import model.ProfileNew;
import network.ApiClient;
import network.ApiInterface;
import utils.Utils;

public class SplashActivity extends AppCompatActivity implements ProfileListener {

    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    String device_token, device_UDID;
    Utils utils = new Utils();
    String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseApp.initializeApp(this);


        context = SplashActivity.this;
        activity = SplashActivity.this;
        connectionHelper = new ConnectionHelper(context);
        customDialog = new CustomDialog(context);

        GlobalData.accessToken = SharedHelper.getKey(context, "access_token");
//
      //  startActivity(new Intent(context, Home.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
      //  finish();
//

        getDeviceToken();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedHelper.getKey(context, "logged").equalsIgnoreCase("true")
                        && SharedHelper.getKey(context, "logged") != null) {
                    if (connectionHelper.isConnectingToInternet())
                        if((!SharedHelper.getKey(context, "access_token").isEmpty()
                                && SharedHelper.getKey(context, "access_token") != null) )
                        { GlobalData.accessToken =SharedHelper.getKey(context, "access_token");
                            new GetProfile(apiInterface, SplashActivity.this);}
                    else
                        Utils.displayMessage(SplashActivity.this, getString(R.string.oops_no_internet));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        }, 3000);

    }


    public void getDeviceToken() {
        try {
            if (!SharedHelper.getKey(context, "device_token").equals("")
                    && !SharedHelper.getKey(context, "device_token").equals("null")) {
                device_token = SharedHelper.getKey(context, "device_token");
                Log.d(TAG, "GCM Registration Token: " + device_token);
            } else {
                device_token = "" + FirebaseInstanceId.getInstance().getToken();
                SharedHelper.putKey(context, "device_token", "" + device_token);
                Log.d(TAG, "Failed to complete token refresh: " + device_token);
            }
        } catch (Exception e) {
            device_token = "COULD NOT GET FCM TOKEN";
            Log.d(TAG, "Failed to complete token refresh");
        }

        try {
            device_UDID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.d(TAG, "Device UDID:" + device_UDID);
        } catch (Exception e) {
            device_UDID = "COULD NOT GET UDID";
            e.printStackTrace();
            Log.d(TAG, "Failed to complete device UDID");
        }
    }

    @Override
    public void onSuccess(Profile profile) {
        GlobalData.profile = profile;
        startActivity(new Intent(context, Home.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();


    }



    @Override
    public void onSuccess(ProfileNew profile) {
        GlobalData.profileNew = profile;
       /* startActivity(new Intent(context, Home.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();*/
        if(profile.getData().getRole().equals("staff")||profile.getData().getRole().equals("ROLE_STAFF")){
            startActivity(new Intent(context, Stuff_Home_Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }else {
            startActivity(new Intent(context, Home.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
    }



    @Override
    public void onFailure(String error) {
        customDialog.dismiss();
       /* if (error.equals("null")||error.isEmpty())
            Utils.displayMessage(activity, getString(R.string.something_went_wrong));
        else*/
            Utils.displayMessage(activity, getString(R.string.something_went_wrong));

        SharedHelper.putKey(context, "logged", "false");
        startActivity(new Intent(SplashActivity.this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
