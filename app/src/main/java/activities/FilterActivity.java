package activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.ServerError;
import com.example.upbeat.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.filter_adapter;
import adapters.filters_items_adapter;
import adapters.sub_categries_adapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import controller.GetProfile;
import helper.CustomDialog;
import helper.GlobalData;
import models.Categories_Datum;
import models.Categories_response;
import models.Subcategory;
import models.filter_api_request;
import models.filter_api_response;
import network.ApiClient;
import network.ApiInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterActivity extends AppCompatActivity {

    @BindView(R.id.main_category_rv)
    RecyclerView main_category_rv;

    @BindView(R.id.filter_items_rv)
    RecyclerView filter_items_rv;


    @BindView(R.id.attributes_rv)
    RecyclerView attributes_rv;


    @BindView(R.id.layout_filters)
    RelativeLayout layout_filters;


    @BindView(R.id.txt_reset)
    TextView txt_reset;



    @BindView(R.id.layout_attributes)
    LinearLayout layout_attributes;

filters_items_adapter adapter;


    @BindView(R.id.icon)
    ImageView backImg;
    List<Categories_Datum> list;

    @BindView(R.id.icon_attibutes)
    ImageView icon_attibutes;

    int attibutes_open=0;
    filter_adapter testAdapter;

    CustomDialog customDialog;
ApiInterface apiInterface;

    @BindView(R.id.layout_add)
    LinearLayout layout_add;
int show_filter_items=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
customDialog= new CustomDialog(this);
apiInterface= ApiClient.getRetrofit().create(ApiInterface.class);
list = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("key");
           for(int i =0;i<GlobalData.categories_response.getData().size();i++){
               if(value.equals(GlobalData.categories_response.getData().get(i).getName())){
                   list.add(GlobalData.categories_response.getData().get(i));


               }
           }
            //The key argument here must match that used in the other activity
        }else {
            list= GlobalData.categories_response.getData();

        }
        prepareAdapter();

    }

    @OnClick({ R.id.icon,R.id.layout_add,R.id.icon_attibutes,R.id.txt_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.txt_reset:
                filter_items_rv.setVisibility(View.GONE);
                layout_filters.setVisibility(View.VISIBLE);
                layout_attributes.setVisibility(View.VISIBLE);
                layout_add.setVisibility(View.VISIBLE);
                prepareAdapter();
                break;
            case R.id.icon:
                if(show_filter_items==1){
                    txt_reset.setVisibility(View.VISIBLE);
                    backImg.setBackground(getResources().getDrawable(R.drawable.ic_filter_close_foreground));
                    show_filter_items=0;
                    filter_items_rv.setVisibility(View.GONE);
                    layout_filters.setVisibility(View.VISIBLE);
                    layout_attributes.setVisibility(View.VISIBLE);
                    layout_add.setVisibility(View.VISIBLE);
                }else
                {
                   // startActivity(new Intent(FilterActivity.this, MainActivity.class));
                    finish();
                }
                break;
            case R.id.layout_add:
                if(GlobalData.catlist!=null&&GlobalData.catlist.size()>0)
                { getFilter();}
                else {
                    Toast.makeText(FilterActivity.this, "Please apply some filters first", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.icon_attibutes:
                if(attibutes_open==0)
                { attributes_rv.setVisibility(View.VISIBLE);
               attibutes_open=1; }
                else {
                    attibutes_open=0;
                    attributes_rv.setVisibility(View.GONE);
                }
                break;


        }
    }


    private void prepareAdapter() {
        if (main_category_rv != null) {



            testAdapter = new filter_adapter(FilterActivity.this, list);

            main_category_rv.setLayoutManager(new LinearLayoutManager(FilterActivity.this, LinearLayoutManager.VERTICAL, false));}
        main_category_rv.setHasFixedSize(true);
        main_category_rv.setAdapter(testAdapter);
        //  productsAdapter.setProductAdapterListener(context);


    }

    public void getFilter(){

     String categoys="";
        if(customDialog!=null)
            customDialog.show();




    String listCat = GlobalData.catlist.toString();

   /* for(int i =0;i<GlobalData.catlist.size();i++){
       // GlobalData.catlist.add(GlobalData.Attributeslist.get(i));
        if(i==0){
            categoys="[";
        }
        categoys=categoys+GlobalData.catlist.get(i)+",";
    }*/



        HashMap<String, String> params = new HashMap<>();
        params.put("server_key", "1539874186");
        params.put("access_token", GlobalData.accessToken);
        params.put("user_id", "21");
        params.put("categories",listCat) ;
        params.put("attributes","[]");
        params.put("min_price", "0.0");
        params.put("max_price","2000000.0");
        Call<filter_api_response> call = apiInterface.getFilter(params);
        call.enqueue(new Callback<filter_api_response>() {
            @Override
            public void onResponse(@NonNull Call<filter_api_response> call, @NonNull Response<filter_api_response> response) {
                Log.d("Sccess", "onResponse: " + response.isSuccessful());
                if(customDialog!=null)
                    customDialog.hide();
                if (response.isSuccessful()) {
                    //     SharedHelper.putKey(MyApplication.getInstance(), Constants.PREF.PROFILE_ID, "" + "1");
                    //   SharedHelper.putKey(MyApplication.getInstance(), Constants.PREF.CURRENCY, "" + response.body().getCurrency());
                    Toast.makeText(FilterActivity.this, "List Success", Toast.LENGTH_SHORT).show();
                    GlobalData.catlist=null;
                    GlobalData.Attributeslist=null;
                    if(response.body().getData()!=null&&response.body().getData().size()>0) {
                        filter_items_rv.setVisibility(View.VISIBLE);
                        layout_filters.setVisibility(View.GONE);
                        layout_attributes.setVisibility(View.GONE);
                        txt_reset.setVisibility(View.GONE);
                        layout_add.setVisibility(View.GONE);
                        backImg.setBackground(getResources().getDrawable(R.drawable.ic_back_foreground));
                        adapter = new filters_items_adapter(FilterActivity.this, response.body().getData());
                        show_filter_items=1;

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(FilterActivity.this,2,GridLayoutManager.VERTICAL,false);
                        filter_items_rv.setLayoutManager(gridLayoutManager);

                        filter_items_rv.setHasFixedSize(true);
                       // adapter.setCatAdapterListener(this);
                        filter_items_rv.setAdapter(adapter);
                    }else {
                        Toast.makeText(FilterActivity.this, "List Empty", Toast.LENGTH_SHORT).show();
                    }

                  //  GlobalData.categories_response=response.body();

                    //   String s=response.body().getData().getRole();
                    //    GlobalData.ROLE = response.body().getData().getRole();
                    // SharedHelper.putKey(MyApplication.getInstance(), "role", response.body().getData().getRoles().get(0).getName());
                } else try {
                    if(customDialog!=null)
                        customDialog.hide();
                    ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);

                } catch (JsonSyntaxException e) {

                }
            }

            @Override
            public void onFailure(@NonNull Call<filter_api_response> call, @NonNull Throwable t) {
                if(customDialog!=null)
                    customDialog.hide();
                Log.d("onFailure", "onFailure: " + t.getMessage());

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(show_filter_items==1){
            txt_reset.setVisibility(View.VISIBLE);
            backImg.setBackground(getResources().getDrawable(R.drawable.ic_back_foreground));
            show_filter_items=0;
            filter_items_rv.setVisibility(View.GONE);
            layout_filters.setVisibility(View.VISIBLE);
            layout_attributes.setVisibility(View.VISIBLE);
            layout_add.setVisibility(View.VISIBLE);
        }else
        {
            // startActivity(new Intent(FilterActivity.this, MainActivity.class));

            finish();
        }

    }

}