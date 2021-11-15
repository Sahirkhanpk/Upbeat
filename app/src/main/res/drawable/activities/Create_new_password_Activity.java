package activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import model.change_password_response;
import network.ApiClient;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Create_new_password_Activity extends AppCompatActivity {

    @BindView(R.id.back_img)
    ImageView back_img;

    @BindView(R.id.et_new_password)
    EditText new_password;


    @BindView(R.id.et_confirm_password)
    EditText confirm_password;


    @BindView(R.id.img_lock)
    ImageView img_lock;

    @BindView(R.id.tv_text)
    TextView tv_text;


    @BindView(R.id.tv_btn)
    TextView tv_btn;


    @BindView(R.id.tv_text_detail)
    TextView tv_text_detail;


    @BindView(R.id.tv_email)
    TextView tv_email;

    @BindView(R.id.layout_sign_up)
    LinearLayout layout_sign_up;

    @BindView(R.id.layout_create_password)
    LinearLayout create_password;

    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    boolean isInternetAvailable;


    int btnflag=0;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_password_);
        ButterKnife.bind(this);

        context = Create_new_password_Activity.this;
        activity = Create_new_password_Activity.this;
        connectionHelper = new ConnectionHelper(context);
        isInternetAvailable = connectionHelper.isConnectingToInternet();
        customDialog = new CustomDialog(context);


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
                // if(s.length() > 6&&et_email.getText().length()==11)
                if(new_password.getText().toString().equals(confirm_password.getText().toString()))
                {  create_password.setBackgroundResource(R.drawable.rounded_corners_dark);

                    create_password.setEnabled(true);
                    btnflag=1;}
                else
                {create_password.setBackgroundResource(R.drawable.rounded_corners_white);
                    create_password.setEnabled(false);
                    btnflag=0;}
            }
        });


        confirm_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // if(s.length() > 6&&et_email.getText().length()==11)
                if(new_password.getText().toString().equals(confirm_password.getText().toString()))
                {  create_password.setBackgroundResource(R.drawable.rounded_corners_dark);

                    create_password.setEnabled(true);
                    btnflag=1;}
                else
                {create_password.setBackgroundResource(R.drawable.rounded_corners_white);
                    create_password.setEnabled(false);
                    btnflag=0;}
            }
        });
    }

    @OnClick({ R.id.layout_create_password, R.id.layout_register,R.id.back_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.layout_create_password:
                getChangePassword();
                break;
            case R.id.layout_register:
                startActivity(new Intent(Create_new_password_Activity.this, SignUpActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;

            case R.id.back_img:
                finish();
                break;

        }
    }


    private void getChangePassword() {
        HashMap<String, String> map = new HashMap<>();

        map.put("email",GlobalData.email);
        map.put("password", new_password.getText().toString());

        if (customDialog != null)
            customDialog.show();


        String header = GlobalData.accessToken; /*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*/
        Call<change_password_response> call = apiInterface.changePaswword(map);
        call.enqueue(new Callback<change_password_response>() {
            @Override
            public void onResponse(@NonNull Call<change_password_response > call, @NonNull Response<change_password_response> response) {
                if (customDialog != null)
                    customDialog.cancel();
                if (response.isSuccessful()) {

                    Toast.makeText(Create_new_password_Activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context, Password_chnaged_Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
                        Toast.makeText(Create_new_password_Activity.this, jObjError.optString("message"), Toast.LENGTH_LONG).show();



                    } catch (IOException | JSONException e) {
                        // handle failure to read error
                        Toast.makeText(Create_new_password_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<change_password_response > call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(Create_new_password_Activity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }
}