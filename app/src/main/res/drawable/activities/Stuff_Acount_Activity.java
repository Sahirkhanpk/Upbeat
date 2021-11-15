package activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.excellabs.R;

import java.util.ArrayList;
import java.util.List;

import adapter.SettingAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import controller.GetProfile;
import controller.ProfileListener;
import helper.ConnectionHelper;
import helper.CustomDialog;
import helper.GlobalData;
import model.Profile;
import model.ProfileNew;
import model.Setting;
import network.ApiClient;
import network.ApiInterface;
import utils.Utils;

import static config.AppConfigure.IMAGE_BASE_URL;

public class Stuff_Acount_Activity extends AppCompatActivity implements ProfileListener {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.setting_rv)
    RecyclerView settingRv;
    Unbinder unbinder;

    Context context;
    Activity activity;
    List<Setting> settingArrayList;
    SettingAdapter settingAdapter;
    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.user_pic)
    ImageView user_pic;

    @BindView(R.id.phone)
    TextView phone;



    @BindView(R.id.email)
    TextView email;




    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuff__acount_);
        ButterKnife.bind(this);
        context = Stuff_Acount_Activity.this;
        activity = Stuff_Acount_Activity.this;
       // initViews();
        if (GlobalData.profileNew!= null) {
            name.setText(GlobalData.profileNew.getData().getFullname());
            phone.setText(String.valueOf(GlobalData.profileNew.getData().getPhoneNumber()));
            email.setText(String.valueOf(GlobalData.profileNew.getData().getEmail()));
            if(GlobalData.profileNew.getData().getProfilePicture()!=null) Glide.with(this)
                    .load(IMAGE_BASE_URL+GlobalData.profileNew.getData().getProfilePicture())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(R.drawable.man)
                            .error(R.drawable.man))
                    .into(user_pic);


        }



        settingArrayList = new ArrayList<>();
        settingArrayList.add(new Setting("Update Password", R.drawable.update_password_icon));

    /*    if (GlobalData.ROLE.equals("Master") || GlobalData.ROLE.equals("Manager")) {
            settingArrayList.add(new Setting(getString(R.string.edit_restaurant), R.drawable.ic_edit));
            settingArrayList.add(new Setting(getString(R.string.edit_contacts_info), R.drawable.ic_edit));
            settingArrayList.add(new Setting(getString(R.string.documents), R.drawable.ic_edit));
            settingArrayList.add(new Setting(getString(R.string.bank_details), R.drawable.ic_edit));
            settingArrayList.add(new Setting(getString(R.string.edit_location), R.drawable.ic_edit_time));
        }*/
        settingArrayList.add(new Setting("Completed Task", R.drawable.completed_task_icon));

        settingArrayList.add(new Setting(getString(R.string.logout), R.drawable.logout));


        settingRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        settingRv.setItemAnimator(new DefaultItemAnimator());
        settingRv.setHasFixedSize(true);
        settingAdapter = new SettingAdapter(settingArrayList, context, activity);
        settingRv.setAdapter(settingAdapter);
    }

    @Override
    public void onSuccess(Profile profile) {

        if (GlobalData.profileNew!= null) {
            name.setText(GlobalData.profileNew.getData().getFullname());
            phone.setText(String.valueOf(GlobalData.profileNew.getData().getPhoneNumber()));
            email.setText(String.valueOf(GlobalData.profileNew.getData().getEmail()));
            if(GlobalData.profileNew.getData().getProfilePicture()!=null) Glide.with(this)
                    .load(IMAGE_BASE_URL+GlobalData.profileNew.getData().getProfilePicture())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(R.drawable.man)
                            .error(R.drawable.man))
                    .into(user_pic);


        }

    }

    @Override
    public void onSuccess(ProfileNew profile) {
        if (GlobalData.profileNew!= null) {
            name.setText(GlobalData.profileNew.getData().getFullname());
            phone.setText(String.valueOf(GlobalData.profileNew.getData().getPhoneNumber()));
            email.setText(String.valueOf(GlobalData.profileNew.getData().getEmail()));
            if(GlobalData.profileNew.getData().getProfilePicture()!=null) Glide.with(this)
                    .load(IMAGE_BASE_URL+GlobalData.profileNew.getData().getProfilePicture())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(R.drawable.man)
                            .error(R.drawable.man))
                    .into(user_pic);


        }

    }

    @Override
    public void onFailure(String error) {

    }



    private void initViews() {
        connectionHelper = new ConnectionHelper(Stuff_Acount_Activity.this);
        customDialog = new CustomDialog(Stuff_Acount_Activity.this);

        if (connectionHelper.isConnectingToInternet()) {
            getProfile();

        } else
            Utils.displayMessage(Stuff_Acount_Activity.this, getString(R.string.oops_no_internet));

    }


    private void getProfile() {
        if (connectionHelper.isConnectingToInternet()) {
            customDialog.show();
            new GetProfile(apiInterface, this);
        } else {
            Utils.displayMessage(Stuff_Acount_Activity.this, getResources().getString(R.string.oops_no_internet));
        }
    }
}