package activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.ServerError;
import com.example.excellabs.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helper.CustomDialog;
import helper.SharedHelper;
import me.philio.pinentry.PinEntryView;
import model.verifyPinResponse;
import network.ApiClient;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import utils.Utils;

public class OTP extends AppCompatActivity {



    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.otp)
    PinEntryView otp;
    @BindView(R.id.continue_btn)
    Button continue_btn;
  //  SmsVerifyCatcher smsVerifyCatcher;
  String TAG = "OTP";
    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        getDeviceToken();
        customDialog = new CustomDialog(OTP.this);

        String mobile_number = SharedHelper.getKey(OTP.this, "mobile_number");
        phone.setText(mobile_number);


        otp.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(otp.getText().length()==5)
                {    continue_btn.setBackgroundResource(R.drawable.rounded_corners_dark);
                    continue_btn.setEnabled(true);
                }
                else
                { continue_btn.setBackgroundResource(R.drawable.rounded_corners_white);
                    continue_btn.setEnabled(false);}
            }
        });
    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("(\\d{6})");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

 /*   private void login(HashMap<String, String> map) {
        if (customDialog != null)
            customDialog.show();

        Call<Token> call = apiInterface.postLogin(map);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    if (response.body().getAccessToken() != null) {
                        GlobalData.token = response.body();
                        SharedHelper.putKey(com.deliveraround.delivery.activities.OTP.this, "token_type", GlobalData.token.getTokenType());
                        SharedHelper.putKey(com.deliveraround.delivery.activities.OTP.this, "access_token", GlobalData.token.getAccessToken());
                        System.out.println("login " + GlobalData.token.getTokenType() + " " + GlobalData.token.getAccessToken());
                        getProfile();
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(com.deliveraround.delivery.activities.OTP.this, error.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(com.deliveraround.delivery.activities.OTP.this, "login Something wrong", Toast.LENGTH_LONG).show();
            }
        });


    }*/

   /* private void getProfile() {
        if (customDialog != null)
            customDialog.show();

        HashMap<String, String> map = new HashMap<>();
        map.put("device_type", "android");
        map.put("device_id", SharedHelper.getKey(this, "device_id"));
        map.put("device_token", SharedHelper.getKey(this, "device_token"));

        String header = SharedHelper.getKey(com.deliveraround.delivery.activities.OTP.this, "token_type") + " " + SharedHelper.getKey(com.deliveraround.delivery.activities.OTP.this, "access_token");
        System.out.println("getProfile Header " + header);

        Call<ProfieResponse> call = apiInterface.getProfile(header, map);
        call.enqueue(new Callback<ProfieResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProfieResponse> call, @NonNull Response<ProfieResponse> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    GlobalData.profieResponse = response.body();
                    SharedHelper.putKey(com.deliveraround.delivery.activities.OTP.this, "logged_in", "1");
                    startActivity(new Intent(com.deliveraround.delivery.activities.OTP.this, ShiftStatus.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(com.deliveraround.delivery.activities.OTP.this, error.getError(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ProfieResponse> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(com.deliveraround.delivery.activities.OTP.this, "Something wrong getProfile", Toast.LENGTH_LONG).show();
            }
        });
    }
*/
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick(R.id.continue_btn)
    public void onViewClicked() {
        String pin_value = otp.getText().toString();
        if (pin_value.length() < 5) {
            Toast.makeText(OTP.this, "Please enter the valid OTP", Toast.LENGTH_LONG).show();
        } else {

            VerifyPin(pin_value);
        }
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                System.out.println("BroadcastReceiver"+message);
//                otp.setText(message);
            }
        }
    };
    @Override
    public void onResume() {
      //  LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
    public void getDeviceToken() {
        String TAG = "FCM";
        try {
            if (!SharedHelper.getKey(this, "device_token").equals("") && SharedHelper.getKey(this, "device_token") != null) {
                String device_token = SharedHelper.getKey(this, "device_token");
                Log.d(TAG, "GCM Registration Token: " + device_token);
            } else {
                String device_token = FirebaseInstanceId.getInstance().getToken();
                SharedHelper.putKey(this, "device_token", "" + FirebaseInstanceId.getInstance().getToken());
                Log.d(TAG, "Failed to complete token refresh: " + device_token);
            }
        } catch (Exception e) {
            Log.d(TAG, "COULD NOT GET FCM TOKEN");
        }

        try {
            String device_UDID = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            SharedHelper.putKey(this, "device_id", device_UDID);
            Log.d(TAG, "Device UDID:" + device_UDID);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "COULD NOT GET UDID");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
       // smsVerifyCatcher.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       // smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    private void VerifyPin(String otp) {
        HashMap<String, String> map = new HashMap<>();
//                map.put("username", email);
        map.put("otp", otp);
        customDialog.show();
        Call<verifyPinResponse> call = apiInterface.verifyPin(map);
        call.enqueue(new Callback<verifyPinResponse>() {
            @Override
            public void onResponse(@NonNull Call<verifyPinResponse> call, @NonNull Response<verifyPinResponse> response) {
                Log.d(TAG, "onResponse: " + response.isSuccessful());
                if (response.isSuccessful()) {

                    Utils.displayMessage( OTP .this, getString(R.string.verified_success));
                    startActivity(new Intent( OTP .this, ResgisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();

                } else {
                    try {
                        ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage( OTP .this, getString(R.string.something_went_wrong));

                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage( OTP .this, getString(R.string.something_went_wrong));
                    }

                    customDialog.dismiss();
                }
                //customDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<verifyPinResponse> call, @NonNull Throwable t) {
                customDialog.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
                Utils.displayMessage( OTP .this, getString(R.string.something_went_wrong));
            }
        });

    }

}

