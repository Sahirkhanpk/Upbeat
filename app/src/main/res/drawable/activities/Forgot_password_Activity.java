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
import model.forgot_password_response;
import network.ApiClient;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Forgot_password_Activity extends AppCompatActivity {

    @BindView(R.id.back_img)
    ImageView back_img;

    @BindView(R.id.et_email)
    EditText et_email;


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

    @BindView(R.id.layout_send)
    LinearLayout layout_send;

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
        setContentView(R.layout.activity_forgot_password_);
        ButterKnife.bind(this);

        context = Forgot_password_Activity.this;
        activity = Forgot_password_Activity.this;
        connectionHelper = new ConnectionHelper(context);
        isInternetAvailable = connectionHelper.isConnectingToInternet();
        customDialog = new CustomDialog(context);


//        if (BuildConfig.DEBUG){
//            etEmail.setText("sundaram@appoets.com");
//            etPassword.setText("1234567");
//        }

        et_email.addTextChangedListener(new TextWatcher() {

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
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.getText()).matches())
                {  layout_send.setBackgroundResource(R.drawable.rounded_corners_dark);

                    layout_send.setEnabled(true);
                btnflag=1;}
                else
                {layout_send.setBackgroundResource(R.drawable.rounded_corners_white);
                    layout_send.setEnabled(false);
                btnflag=0;}
            }
        });
    }


    @OnClick({ R.id.layout_send, R.id.layout_register,R.id.back_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.layout_send:
                if(btnflag==1)
                {getForgotPassword();}
                else if(btnflag==2)
                { startActivity(new Intent(context,Create_new_password_Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();}
                break;
            case R.id.layout_register:
                startActivity(new Intent(context, SignUpActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;

            case R.id.back_img:
                finish();
                break;

        }
    }

    private void getForgotPassword() {
        HashMap<String, String> map = new HashMap<>();

        map.put("email", et_email.getText().toString());
        GlobalData.email=et_email.getText().toString();
        if (customDialog != null)
            customDialog.show();


        String header = GlobalData.accessToken; /*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*/
        Call<forgot_password_response> call = apiInterface.forgotPaswword(map);
        call.enqueue(new Callback<forgot_password_response >() {
            @Override
            public void onResponse(@NonNull Call<forgot_password_response > call, @NonNull Response<forgot_password_response > response) {
                if (customDialog != null)
                    customDialog.cancel();
                if (response.isSuccessful()) {

                    tv_text.setText("Check your email");
                    img_lock.getLayoutParams().height =300;
                    img_lock.getLayoutParams().width = 300;

                    img_lock.setBackground(getResources().getDrawable(R.drawable.email_sent));
                    tv_text_detail.setText("We have sent a password recovery instruction to your email.");
                    layout_sign_up.setVisibility(View.GONE);
                    tv_email.setVisibility(View.GONE);
                    et_email.setVisibility(View.GONE);
                    btnflag=2;
                    tv_btn.setText("Next");


                   // img_lock.setBackground(getResources().getDrawable(R.id.email));
                   /* if(response.body().getData()!=null&&response.body().getData().size()>0) {
                        layout_members.setVisibility(View.VISIBLE);
                        layout_add_family_member.setVisibility(View.GONE);
                        newTaskRv.setVisibility(View.VISIBLE);
                        orders.clear();
                        //   newTaskRv.getRecycledViewPool().clear();
                        orders.addAll(response.body().getData());
                        newTaskAdapter.notifyDataSetChanged();

                    }else {
                        layout_members.setVisibility(View.GONE);
                        layout_add_family_member.setVisibility(View.VISIBLE);
                        newTaskRv.setVisibility(View.GONE);

                    }*/

                   /* //  initView();
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toarst,
                            (ViewGroup) findViewById(R.id.layout_toarst));


                 *//*   TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Hello! This is a custom toast!");*//*

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                    //Toast.makeText(Create_family_profile_Activity.this, getResources().getString(R.string.success_member), Toast.LENGTH_SHORT).show();

                    finish();*/
                } else {
                   // Toast.makeText(Forgot_password_Activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Gson gson = new GsonBuilder().create();

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                    //  if (jObjError.has("error")) {
                         /*   startActivity(new Intent(Login.this, OTP.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK));*/
                            Toast.makeText(Forgot_password_Activity.this, jObjError.optString("message"), Toast.LENGTH_LONG).show();



                    } catch (IOException | JSONException e) {
                        // handle failure to read error
                        Toast.makeText(Forgot_password_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<forgot_password_response > call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(Forgot_password_Activity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }
}