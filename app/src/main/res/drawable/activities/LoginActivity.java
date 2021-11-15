package activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.android.volley.ServerError;
import com.example.excellabs.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import controller.GetProfile;
import controller.ProfileListener;
import helper.ConnectionHelper;
import helper.CustomDialog;
import helper.GlobalData;
import helper.SharedHelper;
import helper.SuccessDialog;
import model.AuthToken;
import model.Profile;
import model.ProfileNew;
import network.ApiClient;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.LocaleUtils;
import utils.Utils;

import static utils.TextUtils.isValidEmail;

public class LoginActivity extends AppCompatActivity implements ProfileListener {


    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    String device_UDID;

    @BindView(R.id.layout_facilities_page)
    LinearLayout layout_facilities_page;


    @BindView(R.id.txt_forgot_password)
  TextView txtForgotPassword;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password_eye_img)
    ImageView etPasswordEyeImg;



    @BindView(R.id.img_finger_print)
    ImageView img_finger_print;

    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.txt_register)
    TextView txtRegister;
    @BindView(R.id.bottom_lay)
    LinearLayout bottomLay;
    AlertDialog alertDialog;
    @BindView(R.id.layout_register)
    LinearLayout layout_register;

    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    SuccessDialog successDialog;
    boolean isInternetAvailable;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    String TAG = "LoginActivity";
    String email, password;
    int fingerflag=0;
    String role="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if( SharedHelper.getKey(LoginActivity.this, "language").equals("ar"))
            LocaleUtils.setLocale(LoginActivity.this, "ar");
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        context = LoginActivity.this;
        activity = LoginActivity.this;
        connectionHelper = new ConnectionHelper(context);
        isInternetAvailable = connectionHelper.isConnectingToInternet();
        customDialog = new CustomDialog(context);
        successDialog= new SuccessDialog(context);


         role=SharedHelper.getKey(context, "role");

        etPasswordEyeImg.setTag(1);

//        if (BuildConfig.DEBUG){
//            etEmail.setText("sundaram@appoets.com");
//            etPassword.setText("1234567");
//        }

        etPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() >= 6&&etEmail.getText().length()==11)
                {  loginBtn.setBackgroundResource(R.drawable.rounded_corners_dark);
                loginBtn.setEnabled(true);}
                else
                { loginBtn.setBackgroundResource(R.drawable.rounded_corners_white);
                    loginBtn.setEnabled(false);}
            }
        });





        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    // user clicked negative button
                    biometricPrompt.cancelAuthentication();
                }
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
              //  alertDialogFingerPrintConfirm();
                autenticateDevice();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint Authentication")
                .setSubtitle("Use finger print for login")
                .setNegativeButtonText("Cancel")
                .setDeviceCredentialAllowed(false)
                .setDescription("Touch sensor")//Don't Allow PIN/pattern/password authentication.
              //
                //  .setNegativeButtonText("Use account password")

                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
      /*  Button biometricLoginButton = findViewById(R.id.biometric_login);
        biometricLoginButton.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });*/

    }

    @OnClick({R.id.et_password_eye_img, R.id.login_btn, R.id.layout_register, R.id.txt_forgot_password,R.id.img_finger_print,R.id.layout_facilities_page})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_password_eye_img:
                if (etPasswordEyeImg.getTag().equals(1)) {
                    etPassword.setTransformationMethod(null);
                    etPasswordEyeImg.setImageResource(R.drawable.ic_eye_close);
                    etPasswordEyeImg.setTag(0);
                } else {
                    etPassword.setTransformationMethod(new PasswordTransformationMethod());
                    etPasswordEyeImg.setImageResource(R.drawable.ic_eye_open);
                    etPasswordEyeImg.setTag(1);
                }
                break;

            case R.id.img_finger_print:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
                   BiometricManager biometricManager = BiometricManager.from(context);
                    if (biometricManager.canAuthenticate() != BiometricManager.BIOMETRIC_SUCCESS){
                        Utils.displayMessage(activity, "Sorry your device doesn't support fingerprint");
                        //Fingerprint API only available on from Android 6.0 (M)
                    }else if (!fingerprintManager.hasEnrolledFingerprints()) {
                        Utils.displayMessage(activity, "Please Enable Fingerprint Authentication");
                    } else {
                        biometricPrompt.authenticate(promptInfo);
                    }
                }

                break;

            case R.id.layout_facilities_page:
                startActivity(new Intent(LoginActivity.this,LocationsActivity.class));
                break;
            case R.id.login_btn:
                validateLogin();
                break;
            case R.id.layout_register:
                startActivity(new Intent(context, SignUpActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
               finish();
                break;
            case R.id.txt_forgot_password:

                if(role != null && !role.isEmpty()&&!role.equals("")) {
                    if (role.equals("ROLE_STAFF"))
                        startActivity(new Intent(context, Stuff_Forgot_password_Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    else
                        startActivity(new Intent(context, Forgot_password_Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }else {
                    alertselectUser();
                }
                break;
        }
    }

    private void validateLogin() {
        email = etEmail.getText().toString();

        password = etPassword.getText().toString();
        if (email.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_phone_number));
        else if (!isValidEmail(email))
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_valid_phoneNumber));
        else if (password.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_password));
        else {
            if (isInternetAvailable) {

                try {
                    device_UDID = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                    Log.d(TAG, "Device UDID:" + device_UDID);
                    //  SharedHelper.putKey(this, "device_id", device_UDID);
                } catch (Exception e) {
                    device_UDID = "COULD NOT GET UDID";
                    e.printStackTrace();
                    Log.d(TAG, "Failed to complete device UDID");
                }
                HashMap<String, String> map = new HashMap<>();
                if(fingerflag==1)
                {map.put("device_token", device_UDID);}
//                map.put("username", email);
                map.put("phone_number", email.substring(1));
                map.put("password", password);
               /* map.put("grant_type", "password");
                map.put("client_id", AppConfigure.CLIENT_ID);
                map.put("client_secret", AppConfigure.CLIENT_SECRET);
                map.put("guard", "shops");*/
                fingerflag=0;
                login(map);
            } else {
                Utils.displayMessage(activity, getResources().getString(R.string.oops_no_internet));
            }

        }
    }

    private void login(HashMap<String, String> map) {
        customDialog.show();
        Call<AuthToken> call = apiInterface.login(map);
        call.enqueue(new Callback<AuthToken>() {
            @Override
            public void onResponse(@NonNull Call<AuthToken> call, @NonNull Response<AuthToken> response) {
                Log.d(TAG, "onResponse: " + response.isSuccessful());
                if (response.isSuccessful()) {
                  //  Log.d(TAG, "onResponse: access_token: " + response.body().getTokenType() + " "+response.body().getAccessToken());
                    SharedHelper.putKey(context, "access_token", /*response.body().getTokenType() + " " +*/ response.body().getData().getAccessToken());
                    GlobalData.accessToken = response.body().getData().getAccessToken();
//                    SharedHelper.putKey(context, "role", response.body().getRoles().get(0));
                    GlobalData.ROLE = response.body().getData().getRole();
                    SharedHelper.putKey(context, "role",response.body().getData().getRole());
                    GlobalData.authToken=response.body();
                    GlobalData.email=response.body().getData().getEmail();

                    new GetProfile(apiInterface, LoginActivity.this);
                } else {
                    try {
                        ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                        if (serverError !=null) {
                            Utils.displayMessage(activity, getString(R.string.invalid_credentials));
                        } else {
                            Utils.displayMessage(activity, "User Do not Exists ");
                        }

                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(activity, getString(R.string.something_went_wrong));
                    }

                    customDialog.dismiss();
                }
                //customDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<AuthToken> call, @NonNull Throwable t) {
                customDialog.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });

    }



    @Override
    public void onSuccess(ProfileNew profile) {
        customDialog.dismiss();
        SharedHelper.putKey(context, "logged", "true");
        SharedHelper.putKey(context, "access_token", GlobalData.accessToken);
        GlobalData.profileNew = profile;
        String s=profile.getData().getRole();
if(profile.getData().getRole().equals("staff")||profile.getData().getRole().equals("ROLE_STAFF")){
    startActivity(new Intent(context, Stuff_Home_Activity.class));
    finish();
}else {
    startActivity(new Intent(context, Home.class));
    finish();
}
    }

    @Override
    public void onSuccess(Profile profile) {

    }

    @Override
    public void onFailure(String error) {
        customDialog.dismiss();
       Utils.displayMessage(activity, getString(R.string.something_went_wrong));
    }



   /* public void alertDialogFoodprepared() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(getResources().getString(R.string.food_prepared))
                .setPositiveButton(getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
//                        handler.removeCallbacks(runnable);
                        dialog.dismiss();
                        handler.removeCallbacksAndMessages(null);


                    }
                });

        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        if(!isFinishing()) {
            alert.show();
            Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(ContextCompat.getColor(ServiceFlow.this, R.color.colorAccent));
            pbutton.setTypeface(pbutton.getTypeface(), Typeface.BOLD);
        }
        *//*if (!((ServiceFlow) this).isFinishing()) {
            alert.show();
        }*//*
    }*/







    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( customDialog!=null && customDialog.isShowing() ){
            customDialog.cancel();
        }

    }
    public void alertselectUser() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.dialog_select_user, null);
        alertDialog.setView(convertView);


      /*  alertDialog
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
//                        handler.removeCallbacks(runnable);
                        dialog.dismiss();
                        finish();


                    }
                });*/

        AlertDialog alert = alertDialog.create();
        alert.setCancelable(false);
        alert.show();
        LinearLayout patient_member= (LinearLayout) convertView.findViewById(R.id.patient_layout);
        LinearLayout staff_member= (LinearLayout) convertView.findViewById(R.id.staff_layout);
        LinearLayout layout_next= (LinearLayout) convertView.findViewById(R.id.layout_next);

        TextView txt_patient= (TextView) convertView.findViewById(R.id.txt_patient);
       TextView txt_staff= (TextView) convertView.findViewById(R.id.txt_staff);
        patient_member.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                patient_member.setBackground(getResources().getDrawable(R.drawable.rounded_corners_blue));
                staff_member.setBackground(getResources().getDrawable(R.drawable.rounded_corners_whitle_small));
                txt_patient.setTextColor(getResources().getColor(R.color.colorPrimaryBlueText));
                txt_staff.setTextColor(getResources().getColor(R.color.colorPrimaryText));
              //  patient ="other";
                role="patient";




            }
        });
        staff_member.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                  staff_member.setBackground(getResources().getDrawable(R.drawable.rounded_corners_blue));
                patient_member .setBackground(getResources().getDrawable(R.drawable.rounded_corners_whitle_small));
               txt_staff.setTextColor(getResources().getColor(R.color.colorPrimaryBlueText));
                txt_patient .setTextColor(getResources().getColor(R.color.colorPrimaryText));
                //  patient ="other";
                role="ROLE_STAFF";




            }
        });

        layout_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                alert.dismiss();
                if (role.equals("ROLE_STAFF"))
                    startActivity(new Intent(context, Stuff_Forgot_password_Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                else
                    startActivity(new Intent(context, Forgot_password_Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }








        });
       /* Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorAccent));
        pbutton.setTypeface(pbutton.getTypeface(), Typeface.BOLD);*/

        /*if (!((ServiceFlow) this).isFinishing()) {
            alert.show();
        }*/
    }
}