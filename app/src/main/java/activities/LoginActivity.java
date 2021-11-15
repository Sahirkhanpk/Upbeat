package activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.ServerError;


import controller.GetProfile;
import controller.ProfileListener;
import helper.ConnectionHelper;
import helper.CustomDialog;
import helper.GlobalData;
import helper.SharedHelper;
import helper.SuccessDialog;
import models.AuthToken;
import models.Categories_response;
import models.Profile;
import models.ProfileNew;
import network.ApiClient;
import network.ApiInterface;
import utils.LocaleUtils;
import utils.Utils;

import com.example.upbeat.R;
import com.google.gson.Gson;

import com.google.gson.JsonSyntaxException;


import java.util.HashMap;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static utils.TextUtils.isValidEmail;


public class LoginActivity extends AppCompatActivity implements ProfileListener {


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
    int fingerflag = 0;
    String role = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(LoginActivity.this, "language").equals("ar"))
            LocaleUtils.setLocale(LoginActivity.this, "ar");
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        context = LoginActivity.this;
        activity = LoginActivity.this;
        connectionHelper = new ConnectionHelper(context);
        isInternetAvailable = connectionHelper.isConnectingToInternet();
        customDialog = new CustomDialog(context);
        successDialog = new SuccessDialog(context);


        role = SharedHelper.getKey(context, "role");

        etPasswordEyeImg.setTag(1);

        etPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });


    }

    @OnClick({R.id.et_password_eye_img, R.id.login_btn/*, R.id.layout_register, R.id.txt_forgot_password,R.id.img_finger_print,R.id.layout_facilities_page*/})
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
            case R.id.login_btn:
                validateLogin();
                break;

        }
    }

    private void validateLogin() {
        email = etEmail.getText().toString();

        password = etPassword.getText().toString();
        if (email.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_email_address));
        else if (!isValidEmail(email))
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_valid_email_address));
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
                if (fingerflag == 1) {
                    map.put("device_token", device_UDID);
                }

//                map.put("username", email);
                map.put("server_key", "1539874186");
                map.put("username", email);
                map.put("password", password);

                fingerflag = 0;
                login(map);
            } else {
                Utils.displayMessage(activity, getResources().getString(R.string.oops_no_internet));
            }

        }
    }

    private void login(HashMap<String, String> map) {
        customDialog.show();
        Call<AuthToken> call = apiInterface.login(map, "test");
        call.enqueue(new Callback<AuthToken>() {
            @Override
            public void onResponse(@NonNull Call<AuthToken> call, @NonNull Response<AuthToken> response) {
                Log.d(TAG, "onResponse: " + response.isSuccessful());
                if (response.isSuccessful()) {
                    //  Log.d(TAG, "onResponse: access_token: " + response.body().getTokenType() + " "+response.body().getAccessToken());
                    SharedHelper.putKey(context, "access_token", /*response.body().getTokenType() + " " +*/ response.body().getData().getAccessToken());
                    GlobalData.accessToken = response.body().getData().getAccessToken();

                    GlobalData.authToken = response.body();
                    new GetProfile(apiInterface, LoginActivity.this);

                } else {
                    try {
                        ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                        if (serverError != null) {
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
    public void onSuccess(Profile profile) {

    }

    @Override
    public void onSuccess(Categories_response profile) {
        customDialog.dismiss();
        SharedHelper.putKey(context, "logged", "true");
        SharedHelper.putKey(context, "access_token", GlobalData.accessToken);
        GlobalData.categories_response = profile;
        startActivity(new Intent(context, MainActivity.class));
        finish();

    }


    @Override
    public void onFailure(String error) {
        customDialog.dismiss();
        Utils.displayMessage(activity, getString(R.string.something_went_wrong));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.cancel();
        }

    }

}