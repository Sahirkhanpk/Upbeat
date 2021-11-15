package activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.ServerError;
import com.example.excellabs.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import helper.ConnectionHelper;
import helper.CustomDialog;
import helper.GlobalData;
import model.SignUpResponse;
import network.ApiClient;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utils;

public class ResgisterActivity extends AppCompatActivity {
    @BindView(R.id.et_full_name)
    EditText full_name;



    @BindView(R.id.img_loc)
    ImageView img_loc;
    @BindView(R.id.et_add_address)
    TextView et_add_address;

    @BindView(R.id.et_email)
    EditText et_email;

    @BindView(R.id.et_date_of_birth)
    EditText date_of_birth;

    @BindView(R.id.et_select_city)
    Spinner et_select_city;



    @BindView(R.id.layout_address)
    RelativeLayout layout_address;

    @BindView(R.id.ed_password)
    EditText ed_password;

    @BindView(R.id.confrom_ed_password)
    EditText confrom_ed_password;

    @BindView(R.id.et_first_name)
    EditText et_first_name;

    @BindView(R.id.et_last_name)
    EditText et_last_name;



    @BindView(R.id.signUp_btn)
    Button signUp_btn;
String device_UDID;
  /*  @BindView(R.id.et_date_of_birth)
   EditText et_date_of_birth;*/

    final Calendar myCalendar = Calendar.getInstance();

    @BindView(R.id.radio_male)
    RadioButton radio_male;
    @BindView(R.id.radio_female)
    RadioButton radio_female;
    boolean isInternet;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    String name,email,date_of_Birth,gender,city,password,c_password,role,fristname,lastname,userAddress;
    String TAG = "OTP";
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgister);
        ButterKnife.bind(this);



       // EditText edittext= (EditText) findViewById(R.id.Birthday);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        date_of_birth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ResgisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


img_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResgisterActivity.this,EditLocatoinActivity.class));
            }
        });


        connectionHelper = new ConnectionHelper(ResgisterActivity.this);
        customDialog = new CustomDialog(ResgisterActivity.this);
        isInternet = connectionHelper.isConnectingToInternet();
       signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupCall();
            }
        });}

    private void signupCall() {


      /*  "first_name":"m",
                "last_name":"asif",
                "username":"masi8f",
                "profile_picture":null,
                "email":"masif2024@gmail.com",
                "phone_number":"03438255549",
                "date_of_birth":"1991-08-20",
                "password":"12345789",
                "gender":"male",
                "roles":["patient"],
        "city":"lahore",
                "isActive":true*/




        name = et_first_name.getText().toString().trim()+et_last_name.getText().toString().trim();
        fristname = et_first_name.getText().toString().trim();
        lastname = et_last_name.getText().toString().trim();
        email = et_email.getText().toString().trim();
        date_of_Birth = date_of_birth.getText().toString().trim();
userAddress = et_add_address.getText().toString();
        password = ed_password.getText().toString().trim();
        c_password = confrom_ed_password.getText().toString().trim();
        city = et_select_city.getSelectedItem().toString();
        //city= "lahore";
role = "patient";

        if (radio_male.isChecked())
        {gender="male";
        }else if(radio_female.isChecked()) {
            gender="female";
        }

        if (name.isEmpty())
            Utils.displayMessage(ResgisterActivity.this, getResources().getString(R.string.please_enter_name));
        else if (email.isEmpty())
            Utils.displayMessage(ResgisterActivity.this, getResources().getString(R.string.please_enter_mail_id));
        else if (fristname.isEmpty())
            Utils.displayMessage(ResgisterActivity.this,"Please enter first name");
        else if (lastname.isEmpty())
            Utils.displayMessage(ResgisterActivity.this,"Please enter last name");
        else if (userAddress.isEmpty())
            Utils.displayMessage(ResgisterActivity.this, "Please select your address");
       /* else if (!isValidEmail(email))
            Utils.displayMessage(ResgisterActivity.this, getResources().getString(R.string.please_enter_valid_mail_id));*/
      /*  else if (mobile.isEmpty()||mobile.length()!=10)
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_phone_number));*/
        else if (password.isEmpty())
            Utils.displayMessage(ResgisterActivity.this, getResources().getString(R.string.please_enter_password));
        else if (!password.isEmpty() && password.length() < 6)
            Utils.displayMessage(ResgisterActivity.this, getResources().getString(R.string.please_enter_minimum_length_password));
        else if (c_password.isEmpty())
            Utils.displayMessage(ResgisterActivity.this, getResources().getString(R.string.please_enter_confirm_password));
        else if (!c_password.isEmpty() && c_password.length() < 6)
            Utils.displayMessage(ResgisterActivity.this, getResources().getString(R.string.please_enter_minimum_length_password));
        else if (!c_password.equals(password))
            Utils.displayMessage(ResgisterActivity.this, getResources().getString(R.string.password_and_confirm_password_doesnot_match));


        else if (city.isEmpty())
            Utils.displayMessage(ResgisterActivity.this, getResources().getString(R.string.please_select_city));


        else {
            if (isInternet) {

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
                map.put("fullname", name);
                map.put("first_name",  fristname);
                map.put("last_name", lastname);
                map.put("username",  name);
                map.put("email",  email);
                map.put("password", password);
                map.put("role",  role);
                map.put("address",et_add_address.getText().toString() );

             //   map.put("device_token", device_UDID);
             //   map.put("password_confirmation", RequestBody.create(MediaType.parse("text/plain"), confirmPassword));
             //   map.put("pure_veg", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(veg.isChecked() ? 1 : 0)));
                // map.put("default_banner", RequestBody.create(MediaType.parse("text/plain"), ""));
                map.put("date_of_birth",  date_of_Birth);

                map.put("gender",  gender);


                map.put("city", city);


                map.put("phone_number",  GlobalData.phone);
              SignUP(map);
            } else {
                Utils.displayMessage(ResgisterActivity.this, getResources().getString(R.string.oops_no_internet));
            }
        }

    }

    private void SignUP(HashMap<String, String> map) {



        customDialog.show();
        Call<SignUpResponse> call = apiInterface.SignUp(map);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(@NonNull Call<SignUpResponse> call, @NonNull Response<SignUpResponse> response) {
                Log.d(TAG, "onResponse: " + response.isSuccessful());
                if (response.isSuccessful()) {

                    Utils.displayMessage( ResgisterActivity .this,getString(R.string.success_sign_up));
                    startActivity(new Intent( ResgisterActivity  .this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();

                } else {
                    try {
                        ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage( ResgisterActivity .this, getString(R.string.something_went_wrong));

                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage( ResgisterActivity  .this, getString(R.string.something_went_wrong));
                    }

                    customDialog.dismiss();
                }
                //customDialog.dismiss();
            }






            @Override
            public void onFailure(@NonNull Call<SignUpResponse> call, @NonNull Throwable t) {
                customDialog.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
                Utils.displayMessage( ResgisterActivity .this, getString(R.string.something_went_wrong));
            }
        });

    }



    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date_of_birth.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (GlobalData.location != null) {
            et_add_address.setText(GlobalData.location);
        }
    }
}