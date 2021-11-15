package activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.excellabs.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helper.ConnectionHelper;
import helper.CustomDialog;
import helper.GlobalData;
import model.stuff_update_password_model;
import network.ApiClient;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Update_password_Activity extends AppCompatActivity {


    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    boolean isInternetAvailable;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @BindView(R.id.et_password)
    EditText old_password;


    @BindView(R.id.et_new_password)
    EditText new_password;

    @BindView(R.id.layout_update_password)
    LinearLayout layout_update_password;

    @BindView(R.id.et_password_eye_img)
    ImageView etPasswordEyeImg;

    int maches=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password_);
        ButterKnife.bind(this);

        context =Update_password_Activity.this;
        activity =Update_password_Activity.this;
        connectionHelper = new ConnectionHelper(context);
        isInternetAvailable = connectionHelper.isConnectingToInternet();
        customDialog = new CustomDialog(context);

        etPasswordEyeImg.setTag(1);

        old_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() >= 6&&new_password.getText().equals(s))
                {  maches=1;}
                else
                { maches=0;}
            }
        });

       new_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() >= 6&&old_password.getText().equals(s))
                {  maches=1;}
                else
                { maches=0;}
            }
        });

    }



    private void getChangePassword() {
        HashMap<String, String> map = new HashMap<>();

        map.put("password_confirmation",  old_password.getText().toString());
        map.put("password", new_password.getText().toString());

        if (customDialog != null)
            customDialog.show();


        String header = GlobalData.accessToken; /*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*/
        Call<stuff_update_password_model> call = apiInterface.changePaswword_stuff(map);
        call.enqueue(new Callback<stuff_update_password_model >() {
            @Override
            public void onResponse(@NonNull Call<stuff_update_password_model > call, @NonNull Response<stuff_update_password_model > response) {
                if (customDialog != null)
                    customDialog.cancel();
                if (response.isSuccessful()) {

                    Toast.makeText(Update_password_Activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context, Stuff_Password_Updated_successfull.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else {
                    // Toast.makeText(Forgot_password_Activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Gson gson = new GsonBuilder().create();

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //  if (jObjError.has("error")) {
                         /*   startActivity(new Intent(Login.this, OTP.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK));*/
                        Toast.makeText(Update_password_Activity.this, jObjError.optString("message"), Toast.LENGTH_LONG).show();



                    } catch (IOException | JSONException e) {
                        // handle failure to read error
                        Toast.makeText(Update_password_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<stuff_update_password_model > call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(Update_password_Activity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }



    @OnClick({R.id.et_password_eye_img, R.id.layout_update_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_password_eye_img:
                if (etPasswordEyeImg.getTag().equals(1)) {
                    old_password.setTransformationMethod(null);
                    etPasswordEyeImg.setImageResource(R.drawable.ic_eye_close);
                    etPasswordEyeImg.setTag(0);
                } else {
                    old_password.setTransformationMethod(new PasswordTransformationMethod());
                    etPasswordEyeImg.setImageResource(R.drawable.ic_eye_open);
                    etPasswordEyeImg.setTag(1);
                }
                break;


            case R.id.layout_update_password:
                String a = old_password.getText().toString();
                String b = new_password.getText().toString();
                if(b.equals(a)&&!b.isEmpty())
                getChangePassword();
                else {
                    if(a.isEmpty())
                        Toast.makeText(Update_password_Activity.this, "Enter new password", Toast.LENGTH_SHORT).show();
                    else  if(b.isEmpty())
                        Toast.makeText(Update_password_Activity.this, "Enter confirm password", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Update_password_Activity.this, "confirm password does not match", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}