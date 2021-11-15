package activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.excellabs.R;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helper.CustomDialog;
import helper.GlobalData;
import model.getPinResponse;
import network.ApiClient;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utils;

public class SignUpActivity extends AppCompatActivity {




    @BindView(R.id.et_phone)
    EditText phone;
    @BindView(R.id.txt_login)
    TextView txt_login;
    @BindView(R.id.layout_login)
    LinearLayout layout_login;
    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @BindView(R.id.getPin)
    Button getPin;
    
    String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        customDialog = new CustomDialog(SignUpActivity.this);
        phone.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(phone.getText().length()==10)
                {  getPin.setBackgroundResource(R.drawable.rounded_corners_dark);
                    getPin.setEnabled(true);
                }
                else
                { getPin.setBackgroundResource(R.drawable.rounded_corners_white);
                    getPin.setEnabled(false);}
            }
        });
    }

    @OnClick({ R.id.layout_login, R.id.getPin})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.layout_login:
                 startActivity(new Intent(SignUpActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;
            case R.id.getPin:
               getPin(phone.getText().toString());
                break;

        }



    }

    private void getPin(String phone) {
        HashMap<String, String> map = new HashMap<>();
//                map.put("username", email);
        map.put("phone_number", phone);
        customDialog.show();
        Call<getPinResponse> call = apiInterface.getPin(map);
        call.enqueue(new Callback<getPinResponse>() {
            @Override
            public void onResponse(@NonNull Call<getPinResponse> call, @NonNull Response<getPinResponse> response) {
                Log.d(TAG, "onResponse: " + response.isSuccessful());
                if (response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "" +getString(R.string.verification_code_send), Toast.LENGTH_SHORT).show();
                    GlobalData.phone= phone;
                    startActivity(new Intent(SignUpActivity.this, OTP.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();

                } else {
                    try {
                   /*     ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(SignUpActivity.this, getString(R.string.something_went_wrong));*/

                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //  if (jObjError.has("error")) {
                         /*   startActivity(new Intent(Login.this, OTP.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK));*/
                        Toast.makeText(SignUpActivity.this, jObjError.optString("message"), Toast.LENGTH_LONG).show();

                    } catch (JsonSyntaxException | IOException | JSONException e) {
                        Utils.displayMessage(SignUpActivity.this, getString(R.string.something_went_wrong));
                    }

                    customDialog.dismiss();
                }
                //customDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<getPinResponse> call, @NonNull Throwable t) {
                customDialog.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
                Utils.displayMessage(SignUpActivity.this, getString(R.string.something_went_wrong));
            }
        });

    }



}