package activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.ServerError;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.excellabs.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import fragment.HomeFragment;
import helper.ConnectionHelper;
import helper.CustomDialog;
import helper.GlobalData;
import helper.SharedHelper;
import model.Excel_lab_test_response;
import model.logoutResponse;
import network.ApiClient;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utils;

import static config.AppConfigure.IMAGE_BASE_URL;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.main_container)
    FrameLayout mainContainer;
    @BindView(R.id.bottom_navigation)
    BottomAppBar bottomNavigation;

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;

    TextView textCartItemCount;
    int mCartItemCount = 10;

    @BindView(R.id.nav_view_drawer)
    NavigationView nav_view_drawer;

    CircleImageView userAvatar;
    TextView name,update;
    TextView phone;
    TextView email;
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private CustomDialog customDialog;
    FragmentTransaction transaction;
    private ConnectionHelper connectionHelper;
    private Fragment fragment;
    private FragmentManager fragmentManager;

    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        customDialog = new CustomDialog(Home.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
       if(GlobalData.ROLE!="null")
        SharedHelper.putKey(Home.this, "role", GlobalData.ROLE);
        getExcelLabAvaibleTest();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this,Order_a_test_Activity.class));
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu_humbarg, Home.this.getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        toggle.syncState();
     //   getSupportActionBar().setTitle(getResources().getString(R.string.live_tasks));



        getSupportActionBar().setTitle("Exel labs");
        userAvatar = nav_view_drawer.getHeaderView(0).findViewById(R.id.user_pic);
        RelativeLayout nav_header = nav_view_drawer.getHeaderView(0).findViewById(R.id.nav_header);
        nav_header.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // startActivity(new Intent(Home.this, ProfileActivity.class));
            }
        });
        name = nav_view_drawer.getHeaderView(0).findViewById(R.id.name);
        phone = nav_view_drawer.getHeaderView(0).findViewById(R.id.phone);
        email= nav_view_drawer.getHeaderView(0).findViewById(R.id.email);
        update= nav_view_drawer.getHeaderView(0).findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, ProfileActivity.class));
            }
        });
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
                    .into(userAvatar);


        }
        nav_view_drawer.setNavigationItemSelectedListener(this);
        nav_view_drawer.getMenu().getItem(0).setActionView(R.layout.drawer_item);
        nav_view_drawer.getMenu().getItem(1).setActionView(R.layout.drawer_item);
        nav_view_drawer.getMenu().getItem(2).setActionView(R.layout.drawer_item);
        nav_view_drawer.getMenu().getItem(3).setActionView(R.layout.drawer_item);
        nav_view_drawer.getMenu().getItem(4).setActionView(R.layout.drawer_item);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.mi_location:
                       // fragment = new HomeFragment();
                        startActivity(new Intent(Home.this,LocationsActivity.class));
                        break;

                    case R.id.mi_order:
                        fragment = new HomeFragment();
                        break;

                    case R.id.mi_contact:
                        startActivity(new Intent(Home.this,Contact_us_Activity.class));
                        break;


                }

                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });



        ////////////////////////////////

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();



        // Create items

        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Order a Test",R.drawable.white_radius, R.color.md_white_1000);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Locations", R.drawable.location_icon, R.color.grey);
      //  AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.revenue, R.drawable.cash, R.color.grey);
      //  AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.title_Menu, R.drawable.options, R.color.grey);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Contact us", R.drawable.contact_icon, R.color.grey);

     /*   // Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);

        bottomNavigation.addItem(item3);
    *//*    bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);*//*

        // Set background color
        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.colorWhite));

        // Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(true);
        bottomNavigation.setTranslucentNavigationEnabled(true);

        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

// Change colors
        bottomNavigation.setAccentColor(getResources().getColor(R.color.colorPrimary));
        bottomNavigation.setInactiveColor(getResources().getColor(R.color.bottomSheetInActiveColor));*/


        // Set current item programmatically
        fragment = new HomeFragment();

      /*  if (isfromNotification != 1)
            bottomNavigation.setCurrentItem(0);
        else {
            if (notification.equals("from Addons")) {
                bottomNavigation.setCurrentItem(3);
            } else {
                bottomNavigation.setCurrentItem(4);
            }

            fragment = new SettingFragment();
        }*/
        transaction.add(R.id.main_container, fragment).commit();
        // Set listeners
      /*  bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            // Do something cool here...
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new HomeFragment();
                    break;
                case 2:
                    fragment = new SettingFragment();
                    break;

            }

            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, fragment).commit();
            return true;
        });*/



    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }





   /* if (GlobalData.profieResponse != null) {
        name.setText(GlobalData.profieResponse.getTransporter().getName());
        userId.setText(String.valueOf(GlobalData.profieResponse.getTransporter().getId()));
        Glide.with(this)
                .load(GlobalData.profieResponse.getTransporter().getAvatar())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .placeholder(R.drawable.man)
                        .error(R.drawable.man))
                .into(userAvatar);
    }*/
   @Override
   public boolean onNavigationItemSelected(MenuItem item) {
       // Handle navigation view item clicks here.
       int id = item.getItemId();

       if (id == R.id.nav_profile) {
           startActivity(new Intent(Home.this, Manage_sub_profiles.class));
       } else if (id == R.id.nav_logout) {
           showLogoutAlertDialog();
       }
       else if (id == R.id.manage_payments) {
           startActivity(new Intent(Home.this, ManagePaymentsActivity.class));
       }
       else if (id == R.id.nav_loyalty_points) {
           startActivity(new Intent(Home.this, Loyalty_points_Activity.class));
       }
       else if (id == R.id.nav_payemnt_history) {
           startActivity(new Intent(Home.this, PaymentHistoryctivity.class));
       }
       else if (id == R.id.manage_promocodes) {
           startActivity(new Intent(Home.this, PromoCodesActivity.class));
       }/* else if (id == R.id.nav_loyalty_points) {
           startActivity(new Intent(Home.this, OrderHistory.class));
       } else if (id == R.id.nav_terms_conditions) {
           startActivity(new Intent(Home.this, TermsAndConditions.class));
       } else if (id == R.id.nav_language) {
           changeLanguage();
       } else if (id == R.id.nav_logout) {
           logout();
       }*/
       DrawerLayout drawer = findViewById(R.id.drawer_layout);
       drawer.closeDrawer(GravityCompat.START);
       return false;
   }
    @Override
    protected void onResume() {
        super.onResume();
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
                    .into(userAvatar);
        }

    }

    private void showLogoutAlertDialog() {
       /* AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);*/
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Home.this);
        // builder.setTitle(context.getString(R.string.confirm));

        TextView title = new TextView(Home.this);
// You Can Customise your Title here
        title.setText(Home.this.getString(R.string.confirm));
        title.setPadding(10, 10, 10, 10);
        title.setTextColor(Home.this.getResources().getColor(R.color.colorPrimaryText));
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);

        builder.setCustomTitle(title);

        LinearLayout diagLayout = new LinearLayout(Home.this);
        diagLayout.setOrientation(LinearLayout.VERTICAL);
        final TextView text = new TextView(Home.this);
        text.setText(Home.this.getResources().getString(R.string.alert_log_out));
        text.setPadding(10, 10, 10, 10);
        text.setGravity(Gravity.CENTER);
        text.setTextSize(15);
        diagLayout.addView(text);
        builder.setView(diagLayout);


        // builder.setMessage(context.getResources().getString(R.string.alert_log_out));
        builder.setPositiveButton(Home.this.getResources().getString(R.string.logout), (dialog, which) -> {
            dialog.dismiss();
            logOut();
        });
        builder.setNegativeButton(Home.this.getResources().getString(R.string.cancelled), (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void logOut() {
        customDialog.show();
        // String shop_id = SharedHelper.getKey(context, Constants.PREF.PROFILE_ID);
        String header = GlobalData.accessToken;
        Call<logoutResponse> call = apiInterface.logOut(header);
        call.enqueue(new Callback<logoutResponse>() {
            @Override
            public void onResponse(Call<logoutResponse> call, Response<logoutResponse> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    clearAndExit();
                } else {
                    Gson gson = new Gson();
                    try {
                        ServerError serverError = gson.fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(Home.this, "serverError.getError()");
                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(Home.this, Home.this.getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<logoutResponse> call, Throwable t) {
                customDialog.dismiss();
                Utils.displayMessage(Home.this, Home.this.getString(R.string.something_went_wrong));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_notification);

        View actionView = menuItem.getActionView();
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_notification: {
                // Do something
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void clearAndExit() {
        String language= SharedHelper.getKey(Home.this, "language");
        SharedHelper.clearSharedPreferences(Home.this);
        GlobalData.accessToken = "";
        SharedHelper.putKey(Home.this, "language", language);
        if(GlobalData.ROLE!="null")
            SharedHelper.putKey(Home.this, "role", GlobalData.ROLE);
        Home.this.startActivity(new Intent(Home.this, LoginActivity.class));
      finish();
    }


    private void getExcelLabAvaibleTest(){


        if (customDialog != null)
            customDialog.show();


        String header = GlobalData.accessToken; /*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*/
        Call<Excel_lab_test_response> call = apiInterface.getExcelLabTest(header);
        call.enqueue(new Callback<Excel_lab_test_response>() {
            @Override
            public void onResponse(@NonNull Call<Excel_lab_test_response> call, @NonNull Response<Excel_lab_test_response> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    if(response.body().getData()!=null&&response.body().getData().size()>0) {


                        GlobalData.test_order_list =response.body().getData();
                        /*btn_current.setBackground(getResources().getDrawable(R.drawable.churus_button));
                        btn_past.setBackground(getResources().getDrawable(R.drawable.churus_button_blue));
                        txt_current.setTextColor(getResources().getColor(R.color.colorPrimaryText));
                        txt_past.setTextColor(getResources().getColor(R.color.colorWhite));
                        //layout_members.setVisibility(View.VISIBLE);*/
                        //  layout_add_family_member.setVisibility(View.GONE);
                       /* promo_rv.setVisibility(View.VISIBLE);
                        orders.clear();
                        //   newTaskRv.getRecycledViewPool().clear();
                        orders.addAll(response.body().getData());
                        Wallet_adapter.notifyDataSetChanged();*/


                    }else {
                        // layout_members.setVisibility(View.GONE);
                        //   layout_add_family_member.setVisibility(View.VISIBLE);
                        //    promo_rv.setVisibility(View.GONE);
                        Toast.makeText(Home.this, "List is Empty", Toast.LENGTH_LONG).show();

                    }

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
                    //  Toast.makeText(PromoCodesActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    Gson gson = new GsonBuilder().create();

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //  if (jObjError.has("error")) {
                         /*   startActivity(new Intent(Login.this, OTP.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK));*/
                        Toast.makeText(Home.this, jObjError.optString("message"), Toast.LENGTH_LONG).show();



                    } catch (IOException | JSONException e) {
                        // handle failure to read error
                        Toast.makeText(Home.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
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
            public void onFailure(@NonNull Call<Excel_lab_test_response> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(Home.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }
}