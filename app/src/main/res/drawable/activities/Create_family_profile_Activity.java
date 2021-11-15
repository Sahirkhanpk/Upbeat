package activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.excellabs.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helper.CustomDialog;
import helper.GlobalData;
import model.Add_family_member_response;
import network.ApiClient;
import network.ApiInterface;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Create_family_profile_Activity extends AppCompatActivity {



    @BindView(R.id.et_first_name)
    EditText first_name;

    @BindView(R.id.et_date_of_birth)
    EditText date_of_birth;

    @BindView(R.id.rateRadioGroup)
    RadioGroup rateRadioGroup;


    @BindView(R.id.txt_action)
    TextView txt_action;
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.rb_female)
    RadioButton rb_female;


    @BindView(R.id.rb_male)
    RadioButton rb_male;

    @BindView(R.id.mobile_no)
    EditText mobileNo;
    @BindView(R.id.et_email)
    EditText email;
    @BindView(R.id.layout_add_member)
    LinearLayout layout_add_member;
    CustomDialog customDialog;
    @BindView(R.id.back_img)
    ImageView back;
    @BindView(R.id.et_last_name)
    TextView last_name;
    @BindView(R.id.upload_profile)
    ImageView uploadProfile;
    String gender="male";
    String action="add";
    final Calendar myCalendar = Calendar.getInstance();
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_family_profile_);

        ButterKnife.bind(this);

        customDialog = new CustomDialog(Create_family_profile_Activity.this);
        Intent intent = getIntent();
        String name = intent.getStringExtra("from");
        if (name != null && !name.isEmpty() && !name.equals("null"))
            if(name.equals("manag")&&GlobalData.data_family_members!=null)
        {

            title.setText("Update Family Profile");

            first_name.setText(GlobalData.data_family_members.getFullname());
            last_name.setText(GlobalData.data_family_members.getFullname());
            email.setText(GlobalData.data_family_members.getUserId());
            mobileNo.setText(GlobalData.data_family_members.getPhone());
            date_of_birth.setText(GlobalData.data_family_members.getDateOfBirth());

            if(GlobalData.data_family_members.getGender().equals("male"))
            {
                rb_male.setChecked(true);
            }else {
                rb_female.setChecked(true);
            }
            txt_action.setText("Update");
            action ="Update";


        }
       /* String dd = SharedHelper.getKey(this, "language");
        switch (dd) {
            case "English":
                LocaleUtils.setLocale(this, "en");
                break;
            case "Japanese":
                LocaleUtils.setLocale(this, "ja");
                break;
            default:
                LocaleUtils.setLocale(this, "en");
                break;
        }*/


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
                new DatePickerDialog(Create_family_profile_Activity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        rateRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                //rating = i;

                if (i == R.id.rb_male) {
                    gender="male";
                    //do work when radioButton1 is active
                    // rating = 1;
                } else if (i == R.id.radio_female) {
                    gender = "female";
                    //do work when radioButton2 is active
                    // rating = 2;
                }

            }
        });
    }


    @OnClick({R.id.back_img,R.id.layout_add_member})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_img:
                finish();
                break;

            case R.id.layout_add_member:
               addMember();

                break;




        }
    }
    private void addMember() {

        if (first_name.getText().toString().isEmpty()){
            Toast.makeText(this, "The First name field is required.", Toast.LENGTH_SHORT).show();
            return;
        } else if(email.getText().toString().isEmpty()) {
            Toast.makeText(this, "The email field is required.", Toast.LENGTH_SHORT).show();
            return;
        }
        else    if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches())
        {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(last_name.getText().toString().isEmpty()) {
            Toast.makeText(this, "The Last name field is required.", Toast.LENGTH_SHORT).show();
            return;
        }

        else if(date_of_birth.getText().toString().isEmpty()) {
            Toast.makeText(this, "The DOB field is required.", Toast.LENGTH_SHORT).show();
            return;

        }

        if (customDialog != null)
            customDialog.show();




        RequestBody requestBody;

            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("first_name", first_name.getText().toString())
                    .addFormDataPart("last_name", last_name.getText().toString())
                    .addFormDataPart("email", email.getText().toString())
                    .addFormDataPart("phone", mobileNo.getText().toString())
                    .addFormDataPart("date_of_birth", date_of_birth.getText().toString())
                    .addFormDataPart("gender", gender)



                    .build();

        HashMap<String, String> map = new HashMap<>();
        map.put("fullname", first_name.getText().toString());
        map.put("last_name", last_name.getText().toString());
        map.put("email",  email.getText().toString());
        map.put("phone", mobileNo.getText().toString());
        map.put("date_of_birth", date_of_birth.getText().toString());
        map.put("gender",  gender);
        String header =GlobalData.accessToken; /*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*/
        Call<Add_family_member_response> call = apiInterface.addFamilyMember(header, map);
        if(action.equals("Update"))
            call = apiInterface.UpdateFamilyMembers(header,map);
        call.enqueue(new Callback<Add_family_member_response>() {
            @Override
            public void onResponse(@NonNull Call<Add_family_member_response> call, @NonNull Response<Add_family_member_response> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {

                    //  initView();
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toarst,
                            (ViewGroup) findViewById(R.id.layout_toarst));


                    TextView text = (TextView) layout.findViewById(R.id.text);
                    if(action.equals("Update"))
                    text.setText("Family profile updated successfully to your list");

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                    //Toast.makeText(Create_family_profile_Activity.this, getResources().getString(R.string.success_member), Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    Toast.makeText(Create_family_profile_Activity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                   /* Gson gson = new GsonBuilder().create();
                    try {
                        ProfileError error = gson.fromJson(response.errorBody().string(),ProfileError.class);
                        System.out.println("error "+error.toString());
                        if (error.getName() != null){
                            Toast.makeText(ProfileActivity.this, error.getName().get(0), Toast.LENGTH_SHORT).show();
                        }else if(error.getEmail() != null){
                            Toast.makeText(ProfileActivity.this, error.getEmail().get(0), Toast.LENGTH_SHORT).show();
                        }else if(error.getAvatar() != null){
                            Toast.makeText(ProfileActivity.this, error.getAvatar().get(0), Toast.LENGTH_SHORT).show();
                            initView();
                        }
                    } catch (IOException e) {
                        // handle failure to read error
                        Toast.makeText(ProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }*/
                }
            }

            @Override
            public void onFailure(@NonNull Call<Add_family_member_response> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(Create_family_profile_Activity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date_of_birth.setText(sdf.format(myCalendar.getTime()));
    }
}