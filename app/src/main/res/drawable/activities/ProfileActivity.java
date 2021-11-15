package activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.excellabs.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import config.AppConfigure;
import controller.GetProfile;
import controller.ProfileListener;
import de.hdodenhof.circleimageview.CircleImageView;
import helper.CustomDialog;
import helper.GlobalData;
import helper.SharedHelper;
import model.Profile;
import model.ProfileNew;
import model.UpdateProfile;
import network.ApiClient;
import network.ApiInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import utils.Utils;

public class ProfileActivity extends AppCompatActivity  implements ProfileListener {

    @BindView(R.id.user_avatar)
    CircleImageView userAvatar;
    @BindView(R.id.et_first_name)
    EditText first_name;

    @BindView(R.id.et_date_of_birth)
    EditText date_of_birth;

    @BindView(R.id.rateRadioGroup)
    RadioGroup rateRadioGroup;

    @BindView(R.id.et_password)
    EditText password;
    @BindView(R.id.mobile_no)
    EditText mobileNo;
    @BindView(R.id.et_email)
    EditText email;
    @BindView(R.id.layout_update)
    LinearLayout layout_update;
    CustomDialog customDialog;
    @BindView(R.id.back_img)
    ImageView back;
    @BindView(R.id.et_last_name)
    TextView last_name;
    @BindView(R.id.upload_profile)
    ImageView uploadProfile;
    String gender,username,city;

    final Calendar myCalendar = Calendar.getInstance();
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private int PICK_IMAGE_REQUEST = 1;
    File imgFile;
    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 0;
    String device_token, device_UDID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        getDeviceToken();

        customDialog = new CustomDialog(activities.ProfileActivity.this);
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
                new DatePickerDialog(ProfileActivity.this, date, myCalendar
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
        initView();
        //getProfile();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initView() {
        if (GlobalData.profileNew != null) {
            first_name.setText(GlobalData.profileNew.getData().getFirstName());
            last_name.setText(GlobalData.profileNew.getData().getLastName());
            email.setText(GlobalData.profileNew.getData().getEmail());
            mobileNo.setText(GlobalData.profileNew.getData().getPhoneNumber());
           // password.setText(String.valueOf(GlobalData.profileNew.getData().getPassword()));
         //   date_of_birth.setText(String.valueOf(GlobalData.profileNew.getData().getDateOfBirth()));
          /*  if(String.valueOf(GlobalData.profileNew.getData().getGender()).equals("male"))
            {  gender="male";
                rateRadioGroup.check(R.id.rb_male);}
            else {
                rateRadioGroup.check(R.id.rb_female);
                gender="female";
            }*/
            /*Glide.with(this)
                    .load(BuildConfigure.IMAGE_URL+GlobalData.profileNew.getData().getProfilePicture())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.man)
                            .error(R.drawable.man))
                    .into(userAvatar);*/
if(GlobalData.profileNew.getData().getProfilePicture()!=null)
            Glide.with(this)
                    .load(AppConfigure.IMAGE_BASE_URL+GlobalData.profileNew.getData().getProfilePicture())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(R.drawable.man)
                            .error(R.drawable.man))
                    .into(userAvatar);
//            SharedHelper.putKey(this, "currency_code", GlobalData.profile.getCurrencyCode());
        }
    }

   /* private void getProfile() {

        HashMap<String, String> map = new HashMap<>();
        map.put("device_type", "android");
        map.put("device_id", device_UDID);
        map.put("device_token", device_token);
        String header = SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");
        System.out.println("getProfile Header " + header);


        Call<ProfieResponse> call = GlobalData.api.getProfile(header, map);
        call.enqueue(new Callback<ProfieResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProfieResponse> call, @NonNull Response<ProfieResponse> response) {
                if (response.isSuccessful()) {
                    GlobalData.profieResponse.setTransporter(response.body().getTransporter());
                    initView();
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(activities.ProfileActivity.this, error.getError(), Toast.LENGTH_SHORT).show();
                    if (response.code() == 401) {
                        SharedHelper.putKey(activities.ProfileActivity.this, "logged_in", "0");
                        startActivity(new Intent(activities.ProfileActivity.this, Login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfieResponse> call, @NonNull Throwable t) {
                Toast.makeText(activities.ProfileActivity.this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
            }
        });
    }*/

    private void updateProfile() {

        if (first_name.getText().toString().isEmpty()){
            Toast.makeText(this, "The First name field is required.", Toast.LENGTH_SHORT).show();
            return;
        } else if(email.getText().toString().isEmpty()) {
            Toast.makeText(this, "The email field is required.", Toast.LENGTH_SHORT).show();
            return;
        }
            else if(last_name.getText().toString().isEmpty()) {
            Toast.makeText(this, "The Last name field is required.", Toast.LENGTH_SHORT).show();
            return;
        }
                else if(password.getText().toString().isEmpty()) {
            Toast.makeText(this, "The Password field is required.", Toast.LENGTH_SHORT).show();
            return;
        }
                    else if(date_of_birth.getText().toString().isEmpty()) {
                        Toast.makeText(this, "The DOB field is required.", Toast.LENGTH_SHORT).show();
                        return;

        }

        if (customDialog != null)
            customDialog.show();



        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("first_name", RequestBody.create(MediaType.parse("text/plain"), first_name.getText().toString()));
        map.put("last_name", RequestBody.create(MediaType.parse("text/plain"),last_name.getText().toString()));
        map.put("email", RequestBody.create(MediaType.parse("text/plain"), email.getText().toString()));
           if(GlobalData.profileNew.getData().getUsername()!= null && !GlobalData.profileNew.getData().getUsername() .isEmpty())
           { map.put("username", RequestBody.create(MediaType.parse("text/plain"), GlobalData.profileNew.getData().getUsername()));
           username=GlobalData.profileNew.getData().getUsername();}else {
               map.put("username", RequestBody.create(MediaType.parse("text/plain"),""));
               username="";
           }
        map.put("fullname", RequestBody.create(MediaType.parse("text/plain"), GlobalData.profileNew.getData().getFullname()));
        map.put("date_of_birth", RequestBody.create(MediaType.parse("text/plain"), date_of_birth.getText().toString()));
        map.put("gender", RequestBody.create(MediaType.parse("text/plain"), gender));
        if(GlobalData.profileNew.getData().getCity()!= null && !GlobalData.profileNew.getData().getCity().isEmpty())
        { map.put("city", RequestBody.create(MediaType.parse("text/plain"), GlobalData.profileNew.getData().getCity()));
        city=GlobalData.profileNew.getData().getCity();}
        else
        {map.put("city", RequestBody.create(MediaType.parse("text/plain"),""));
        city="";}
        RequestBody requestBody;
        if (imgFile != null) {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("first_name", first_name.getText().toString())
                    .addFormDataPart("last_name", last_name.getText().toString())
                    .addFormDataPart("email", email.getText().toString())
                    .addFormDataPart("username",username)
                    .addFormDataPart("fullname", GlobalData.profileNew.getData().getFullname())
                    .addFormDataPart("date_of_birth", date_of_birth.getText().toString())
                    .addFormDataPart("gender", gender)
                    .addFormDataPart("city", city)
                    .addFormDataPart("profile_picture",
                            imgFile.getName(), RequestBody.create(MediaType.parse("image/jpeg"), imgFile))


                    .build();
        }else {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("first_name", first_name.getText().toString())
                    .addFormDataPart("last_name", last_name.getText().toString())
                    .addFormDataPart("email", email.getText().toString())
                    .addFormDataPart("username", username)
                    .addFormDataPart("fullname", GlobalData.profileNew.getData().getFullname())
                    .addFormDataPart("date_of_birth", date_of_birth.getText().toString())
                    .addFormDataPart("gender", gender)
                    .addFormDataPart("city", city)



                    .build();
        }



        MultipartBody.Part filePart = null;

        if (imgFile != null) {
            filePart = MultipartBody.Part.createFormData("profile_picture", imgFile.getName(), RequestBody.create(MediaType.parse("image/*"), imgFile));
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), imgFile);
            map.put("profile_picture", fileBody);
        }
       // map.put("profile_picture",RequestBody.create(MediaType.parse("image/*"), imgFile));

        String header =GlobalData.accessToken; /*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*/
        Call<UpdateProfile> call = apiInterface.updateProfileWithImage(header, requestBody);
        call.enqueue(new Callback<UpdateProfile>() {
            @Override
            public void onResponse(@NonNull Call<UpdateProfile> call, @NonNull Response<UpdateProfile> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    new GetProfile(apiInterface, ProfileActivity.this);
                  //  initView();
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.success_profile), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ProfileActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<UpdateProfile> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(ProfileActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();

            Glide.with(this)
                    .load(imgDecodableString)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.man)
                            .error(R.drawable.man))
                    .into(userAvatar);
//            imgFile = new File(imgDecodableString);

            try {
                imgFile = new id.zelory.compressor.Compressor(activities.ProfileActivity.this).compressToFile(new File(imgDecodableString));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (resultCode == Activity.RESULT_CANCELED)
            Toast.makeText(this, getResources().getString(R.string.dont_pick_image), Toast.LENGTH_SHORT).show();
    }

    public void goToImageIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick({R.id.back_img, R.id.upload_profile, R.id.layout_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.upload_profile:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        goToImageIntent();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
                    }
                } else {
                    goToImageIntent();
                }
                break;
            case R.id.layout_update:
                updateProfile();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean permission1 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean permission2 = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (permission1 && permission2) {
                        goToImageIntent();
                    } else {
                        Snackbar.make(this.findViewById(android.R.id.content),
                                "Please Grant Permissions to upload Profile",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions(activities.ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
                                    }
                                }).show();
                    }
                }
                break;
        }
    }

    public void getDeviceToken() {
        String TAG = "FCM";
        try {
            if (!SharedHelper.getKey(this, "device_token").equals("") && SharedHelper.getKey(this, "device_token") != null) {
                device_token = SharedHelper.getKey(this, "device_token");
                Log.d(TAG, "GCM Registration Token: " + device_token);
            } else {
                device_token = FirebaseInstanceId.getInstance().getToken();
                SharedHelper.putKey(this, "device_token", "" + FirebaseInstanceId.getInstance().getToken());
                Log.d(TAG, "Failed to complete token refresh: " + device_token);
            }
        } catch (Exception e) {
            device_token = "COULD NOT GET FCM TOKEN";
            Log.d(TAG, "Failed to complete token refresh");
        }

        try {
            device_UDID = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            Log.d(TAG, "Device UDID:" + device_UDID);
            SharedHelper.putKey(this, "device_id", "" + device_UDID);
        } catch (Exception e) {
            device_UDID = "COULD NOT GET UDID";
            e.printStackTrace();
            Log.d(TAG, "Failed to complete device UDID");
        }
    }

    @Override
    public void onSuccess(ProfileNew profile) {
        customDialog.dismiss();
        SharedHelper.putKey(ProfileActivity.this, "logged", "true");
        GlobalData.profileNew = profile;
        startActivity(new Intent(ProfileActivity.this, Home.class));
        finish();
    }

    @Override
    public void onSuccess(Profile profile) {

    }

    @Override
    public void onFailure(String error) {
        customDialog.dismiss();
        if (error.isEmpty())
            Utils.displayMessage(ProfileActivity.this, getString(R.string.something_went_wrong));
        else Utils.displayMessage(ProfileActivity.this, getString(R.string.something_went_wrong));
    }




    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date_of_birth.setText(sdf.format(myCalendar.getTime()));
    }
}
