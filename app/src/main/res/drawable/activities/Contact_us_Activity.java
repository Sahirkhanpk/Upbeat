package activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import model.contactUsResponse;
import network.ApiClient;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Contact_us_Activity extends AppCompatActivity {

    @BindView(R.id.back_img)
    ImageView back_img;

    @BindView(R.id.layout_buttons)
    LinearLayout layout_buttons;

    @BindView(R.id.email)
    LinearLayout email;

    @BindView(R.id.call)
    LinearLayout call;

    @BindView(R.id.layout_msg)
    LinearLayout layout_msg;

    @BindView(R.id.txt_msg)
    EditText et_msg;

    @BindView(R.id.call_button)
    LinearLayout call_button;

    @BindView(R.id.txt_msg_title)
    TextView txt_msg_title;

    @BindView(R.id.txt_msg_detail)
    TextView txt_msg_detail;


    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    boolean isInternetAvailable;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us_);
        ButterKnife.bind(this);

        context = Contact_us_Activity.this;
        activity = Contact_us_Activity.this;
        connectionHelper = new ConnectionHelper(context);
        isInternetAvailable = connectionHelper.isConnectingToInternet();
        customDialog = new CustomDialog(context);

    }


    @OnClick({R.id.back_img,R.id.email,R.id.call_button,R.id.call,R.id.layout_msg})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_img:
                finish();
                break;

            case R.id.email:
               et_msg.setVisibility(View.VISIBLE);
               layout_buttons.setVisibility(View.GONE);
               layout_msg.setVisibility(View.VISIBLE);
               call_button.setVisibility(View.VISIBLE);
               txt_msg_title.setText("Email us");
                txt_msg_detail.setText("Please write your complain or query. Our support team will contact you as soon as possible");

                break;


            case R.id.call_button:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+92211234567"));
                startActivity(intent);
                break;

            case R.id.call:
                Intent call_intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+92211234567"));
                startActivity(call_intent);
                break;

            case R.id.layout_msg:
                sendMessage();
                break;


        }
    }

   

    private void sendMessage() {
        HashMap<String, String> map = new HashMap<>();

        map.put("message", et_msg.getText().toString());

        if (customDialog != null)
            customDialog.show();


        String header = GlobalData.accessToken; /*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*/
        Call< contactUsResponse> call = apiInterface.contactUs(GlobalData.accessToken,map);
        call.enqueue(new Callback< contactUsResponse >() {
            @Override
            public void onResponse(@NonNull Call< contactUsResponse > call, @NonNull Response< contactUsResponse > response) {
                if (customDialog != null)
                    customDialog.cancel();
                if (response.isSuccessful()) {

                    et_msg.setText("");
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();

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
                        Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();



                    } catch (IOException | JSONException e) {
                        // handle failure to read error
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call< contactUsResponse > call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }
}