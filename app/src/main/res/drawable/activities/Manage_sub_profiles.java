package activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.excellabs.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import adapter.TaskAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import helper.CustomDialog;
import helper.GlobalData;
import model.Data_family_members;
import model.family_member_model;
import network.ApiClient;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Manage_sub_profiles extends AppCompatActivity {


    @BindView(R.id.back_img)
    ImageView back_img;


    @BindView(R.id.layout_add_family_member)
    LinearLayout layout_add_family_member;

    @BindView(R.id.layout_members)
    RelativeLayout layout_members;



    @BindView(R.id.new_task_rv)
    RecyclerView newTaskRv;

    TaskAdapter newTaskAdapter;

    List<Data_family_members> orders;

    @BindView(R.id.fabb)
    FloatingActionButton fabb;

    @BindView(R.id.txt_add)
    TextView txt_add;
    CustomDialog customDialog;

    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    final LinearLayoutManager mLayoutManager  = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_sub_profiles);

        ButterKnife.bind(this);

        customDialog = new CustomDialog(Manage_sub_profiles.this);


        orders = new ArrayList<>();
        newTaskAdapter = new TaskAdapter(orders, Manage_sub_profiles.this, true);
        //  newTaskRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        newTaskRv.setLayoutManager(mLayoutManager);
        newTaskRv.setItemAnimator(new DefaultItemAnimator());
        newTaskRv.setAdapter(newTaskAdapter);

        fabb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Manage_sub_profiles.this,Create_family_profile_Activity.class));
            }
        });
        getFamilyMembers();
    }




    @OnClick({R.id.back_img,R.id.txt_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_img:
                finish();
                break;

            case R.id.txt_add:
              showAddMembersDialog();

                break;




        }
    }

    private void showAddMembersDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Manage_sub_profiles.this);
        LayoutInflater inflater = (LayoutInflater) Manage_sub_profiles.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.dialog_add_new_member, null);
        alertDialog.setView(convertView);


       /* alertDialog
                .setPositiveButton(getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
//                        handler.removeCallbacks(runnable);
                        dialog.dismiss();
                        finish();


                    }
                });
*/
        AlertDialog alert = alertDialog.create();

        alert.show();
        LinearLayout add_member= (LinearLayout) convertView.findViewById(R.id.add_new_member);
        add_member.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

             alert.dismiss();
                startActivity(new Intent(Manage_sub_profiles.this,Create_family_profile_Activity.class));




            }
        });
    }


    private void getFamilyMembers() {


        if (customDialog != null)
            customDialog.show();


        String header =GlobalData.accessToken; /*SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");*/
        Call<family_member_model> call = apiInterface.getFamilyMembers(header);
        call.enqueue(new Callback<family_member_model>() {
            @Override
            public void onResponse(@NonNull Call<family_member_model> call, @NonNull Response<family_member_model> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
 if(response.body().getData()!=null&&response.body().getData().size()>0) {
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
                    Toast.makeText(Manage_sub_profiles.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<family_member_model> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(Manage_sub_profiles.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    public void onResume(){
        super.onResume();

        getFamilyMembers();
        // put your code here...

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
getFamilyMembers();
    }

}