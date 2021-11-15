package activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.excellabs.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fragment.SettingFragment;
import fragment.Stuff_HomeFragment;
import helper.ConnectionHelper;
import helper.CustomDialog;
import helper.GlobalData;
import helper.SharedHelper;
import model.CompletedTask;
import model.TodayTask;
import model.TomorrowTask;
import network.ApiClient;
import network.ApiInterface;

public class Stuff_Home_Activity extends AppCompatActivity {



    @BindView(R.id.main_container)
    FrameLayout mainContainer;

    @BindView(R.id.bottomNavigationView)
   BottomNavigationView bottomNavigationView;

    @BindView(R.id.back_img)
    ImageView back_img;

    @BindView(R.id. title)
    TextView  title;



 //   Stuff_TaskAdapter stuff_taskAdapter;
  //  Stuff_pending_task_adapter stuff_pending_task_adapter;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    FragmentTransaction transaction;
//   Stuff_completed_task_Adapter stuff_completec_task_adapter;


/*
String Keyword="";

    @BindView(R.id.   idSearchViewStuff)
    SearchView idSearchViewStuff;

    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    @BindView(R.id.text_show_percentage)
   TextView text_percentage;

    @BindView(R.id.tv_completed)
    TextView tv_completed;

    @BindView(R.id.new_task_rv)
    RecyclerView new_task_rv;

    @BindView(R.id.layout_today)
    RelativeLayout layoutToday;

    @BindView(R.id.layout_completed)
    RelativeLayout layoutCompleted;

    @BindView(R.id.layout_tomorrow)
    RelativeLayout layoutTomorrow;

    @BindView(R.id.new_task_date)
    TextView new_task_date;


    @BindView(R.id.tomorrow_task_rv)
    RecyclerView tomorrow_task_rv;

    @BindView(R.id.new_task_date_tommorrow)
    TextView new_task_date_tommorrow;

    @BindView(R.id.completed_task_rv)
    RecyclerView completed_task_rv;

    @BindView(R.id.new_task_date_completed)
    TextView new_task_date_completed;
*/


    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    boolean isInternetAvailable;
    List<TomorrowTask> pendingTasks;
    List<CompletedTask> completedTasks;
    List<TodayTask> todayTasks;
    int btnflag=0;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    final LinearLayoutManager mLayoutManager  = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuff__home_);
        ButterKnife.bind(this);


        context =Stuff_Home_Activity.this;
        activity = Stuff_Home_Activity.this;
        connectionHelper = new ConnectionHelper(context);
        isInternetAvailable = connectionHelper.isConnectingToInternet();
        customDialog = new CustomDialog(context);


        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        fragment = new Stuff_HomeFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();



     /*   idSearchViewStuff.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Keyword=query;
                getAlltasks();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });*/

        if(GlobalData.ROLE!="null")
            SharedHelper.putKey(Stuff_Home_Activity.this, "role", GlobalData.ROLE);

   /*    todayTasks = new ArrayList<>();
        stuff_taskAdapter = new Stuff_TaskAdapter (todayTasks, Stuff_Home_Activity.this, true);

        completedTasks= new ArrayList<>();

       stuff_completec_task_adapter=new Stuff_completed_task_Adapter (completedTasks, Stuff_Home_Activity.this, true);

        completed_task_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        // tomorrow_task_rv.setLayoutManager(mLayoutManager);
        completed_task_rv.setItemAnimator(new DefaultItemAnimator());
        completed_task_rv.setAdapter(stuff_completec_task_adapter);

        //  newTaskRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        new_task_rv.setLayoutManager(mLayoutManager);
        new_task_rv.setItemAnimator(new DefaultItemAnimator());
        new_task_rv.setAdapter(stuff_taskAdapter);


        pendingTasks = new ArrayList<>();
        stuff_pending_task_adapter = new Stuff_pending_task_adapter(pendingTasks, Stuff_Home_Activity.this, true);
        //  newTaskRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        tomorrow_task_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
       // tomorrow_task_rv.setLayoutManager(mLayoutManager);
        tomorrow_task_rv.setItemAnimator(new DefaultItemAnimator());
        tomorrow_task_rv.setAdapter(stuff_pending_task_adapter);

        getAlltasks();*/


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.mi_account:
                        back_img.setVisibility(View.VISIBLE);
                        title.setText("Back");
                         fragment = new SettingFragment();
                         transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_container, fragment).commit();

                       // startActivity(new Intent(Stuff_Home_Activity.this,Stuff_Acount_Activity.class));
                        break;

                    case R.id.mi_home:
                        back_img.setVisibility(View.GONE);
                        title.setText("Home");
                        fragment = new Stuff_HomeFragment();
                        transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_container, fragment).commit();
                        break;

                   /* case R.id.mi_contact:
                        startActivity(new Intent(Home.this,Contact_us_Activity.class));
                        break;*/


                }


                return true;
            }
        });
    }





   /* private void getAlltasks() {
        HashMap<String, String> map = new HashMap<>();

        map.put("keywords", Keyword);
        if (customDialog != null)
            customDialog.show();
       // map.put("keywords", RequestBody.create(MediaType.parse("text/plain"), "test"));

        String header = GlobalData.accessToken; *//*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*//*
        Call<Stuff_task_response> call = apiInterface.gettasks(header,map);
        call.enqueue(new Callback<Stuff_task_response>() {
            @Override
            public void onResponse(@NonNull Call<Stuff_task_response> call, @NonNull Response<Stuff_task_response> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    if (!Keyword.isEmpty() && !Keyword.equals("")) {

                    } else {

                        text_percentage.setText(String.valueOf(response.body().getData().getStatistics().getPercentage()) + "%");
                        progress_bar.setProgress(response.body().getData().getStatistics().getPercentage());
                        tv_completed.setText(response.body().getData().getCompletedTasks().size() + " of " + response.body().getData().getAllTasks().size() + " completed");
                        if (response.body().getData().getTodayTasks() != null && response.body().getData().getTodayTasks().size() > 0) {
                            new_task_date.setText(response.body().getData().getTodayTasks().get(0).getTest_date());
                            todayTasks.clear();
                            todayTasks.addAll(response.body().getData().getTodayTasks());
                            layoutToday.setVisibility(View.VISIBLE);
                            stuff_taskAdapter.notifyDataSetChanged();
                        } else {
                            layoutToday.setVisibility(View.GONE);
                        }
                        if (response.body().getData().getTomorrowTasks() != null && response.body().getData().getTomorrowTasks().size() > 0) {
                            new_task_date_tommorrow.setText(response.body().getData().getTomorrowTasks().get(0).getTestDate());
                            pendingTasks.clear();
                            pendingTasks.addAll(response.body().getData().getTomorrowTasks());
                            layoutTomorrow.setVisibility(View.VISIBLE);
                            stuff_pending_task_adapter.notifyDataSetChanged();
                        } else {
                            layoutTomorrow.setVisibility(View.GONE);

                        }
                        if (response.body().getData().getCompletedTasks() != null && response.body().getData().getCompletedTasks().size() > 0) {
                            GlobalData.completedTasks_order_list = response.body().getData().getCompletedTasks();
                            new_task_date_completed.setText(response.body().getData().getCompletedTasks().get(0).getTest_date());
                            completedTasks.clear();
                            completedTasks.addAll(response.body().getData().getCompletedTasks());
                            layoutCompleted.setVisibility(View.VISIBLE);
                            stuff_completec_task_adapter.notifyDataSetChanged();
                        } else {
                            layoutCompleted.setVisibility(View.GONE);

                        }




                   *//* }else {


                    }*//*

                   *//* //  initView();
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toarst,
                            (ViewGroup) findViewById(R.id.layout_toarst));


                 *//**//*   TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Hello! This is a custom toast!");*//**//*

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                    //Toast.makeText(Create_family_profile_Activity.this, getResources().getString(R.string.success_member), Toast.LENGTH_SHORT).show();

                    finish();*//*
                    }
                }else{
                        Toast.makeText(Stuff_Home_Activity.this, "List Empty", Toast.LENGTH_SHORT).show();
                   *//* Gson gson = new GsonBuilder().create();
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
                    }*//*
                    }

            }

            @Override
            public void onFailure(@NonNull Call<Stuff_task_response> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(Stuff_Home_Activity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }*/
   @OnClick({ R.id.back_img})
   public void onViewClicked(View view) {
       switch (view.getId()) {

           case R.id.back_img:
               fragment = new Stuff_HomeFragment();
               transaction = fragmentManager.beginTransaction();
               transaction.replace(R.id.main_container, fragment).commit();
               bottomNavigationView.setSelectedItemId(R.id.mi_home);
               back_img.setVisibility(View.GONE);


               //  new updateButtomNavigation(0,);



               //   Stuff_Home_Activity. bottomNavigationView.getMenu().getItem(0).setChecked(true);


       }
   }


}