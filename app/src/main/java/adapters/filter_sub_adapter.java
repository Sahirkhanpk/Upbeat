package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.ServerError;
import com.example.upbeat.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import helper.CustomDialog;
import helper.GlobalData;
import models.Categories_response;
import models.Subcategory;
import models.Subcategory;
import network.ApiClient;
import network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class filter_sub_adapter extends RecyclerView.Adapter<adapters.filter_sub_adapter.ViewHolder>{

    Context context;
    private List<Subcategory> list = new ArrayList<>();
    private List<Integer> list_needed = new ArrayList<>();
    private LayoutInflater inflater;
    adapters.filter_sub_adapter.ProductAdapterListener productAdapterListener;
    CustomDialog customDialog;

    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    public filter_sub_adapter(Context context, List<Subcategory> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        customDialog = new CustomDialog(context);


    }

    @Override
    public filter_sub_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            /*case VIEW_TYPE_HEADER:
                v = inflater.inflate(R.layout.product_list_item, parent, false);
                return new ViewHolder(v, false);*/
        /*    case VIEW_TYPE_ITEM:
                v = inflater.inflate(R.layout.product_list_item, parent, false);
                return new ViewHolder(v, false);*/
            default:
                v = inflater.inflate(R.layout.item_filter_cat, parent, false);
                return new filter_sub_adapter.ViewHolder(v, false);
        }
       /* View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_addons_list_item, parent, false);
        return new filter_sub_adapter.ViewHolder(itemView,false);*/


    }




    @Override
    public int getItemCount() {
        return list.size();
    }



/*
    @Override
    public int getItemCount(int section) {
        return list.size();
    }*/

/*    @Override
    public void onBindHeaderViewHolder(filter_sub_adapter.ViewHolder holder, final int section) {
      //  holder.header.setText(list.get(section).getHeader());
       *//* holder.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  System.out.println(list.get(section).getHeader());
            }
        });*//*
    }*/

    public void setList(List<Subcategory> list) {
        this.list = list;
    }

    @Override
    public void onBindViewHolder(filter_sub_adapter.ViewHolder holder, int absolutePosition) {

        final Subcategory object = list.get(absolutePosition);
       /* holder.productName.setText(object.getName());
        String c = object.getCategoryName();
        holder.noAddOns.setText(object.getCategoryName());*/
//        holder.noAddOns.setVisibility(View.VISIBLE);
        holder.report_ready_txt.setText(object.getName());
        holder.report_ready_txt.setTextColor(context.getResources().getColor(R.color.primaryTextColor));


        if(list.get(absolutePosition).getSubcategories()!=null&&list.get(absolutePosition).getSubcategories().size()>0) {
            filter_sub_adapter sub_adapter;
            sub_adapter = new filter_sub_adapter(context, list.get(absolutePosition).getSubcategories());
            holder.sub_category_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            holder.sub_category_rv.setHasFixedSize(true);
            holder.sub_category_rv.setAdapter(sub_adapter);
        }

        //in some cases, it will prevent unwanted situations
        holder.checkBox.setOnCheckedChangeListener(null);
  /*      filter_sub_adapter sub_adapter;
        sub_adapter = new filter_sub_adapter(context, list.get(absolutePosition).getSubcategories());

        main_category_rv.setLayoutManager(new LinearLayoutManager(FilterActivity.this, LinearLayoutManager.VERTICAL, false));
        main_category_rv.setHasFixedSize(true);
        main_category_rv.setAdapter(testAdapter);*/

        //if true, your checkbox will be selected, else unselected


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Integer pos = holder.getAdapterPosition();
                    //Toast.makeText(context, "Oops ! Connect your Internet"+pos, Toast.LENGTH_LONG).show();
                  list_needed.add(list.get(pos).getId());
                    if(list.get(pos).getSubcategories()!=null&&list.get(pos).getSubcategories().size()>0)
                    {
                           getAttributes(list.get(pos).getId());
                        holder.sub_category_rv.setVisibility(View.VISIBLE);}




                }else {
                    holder.sub_category_rv.setVisibility(View.GONE);
                }
                 GlobalData.Attributeslist=list_needed;
            }
        });







    }

    public void setProductAdapterListener(adapters.filter_sub_adapter.ProductAdapterListener productAdapterListener) {
        this.productAdapterListener = productAdapterListener;
    }

    public interface ProductAdapterListener {
        void onProductClick(String category);

        void onProductDeleteClick(String category);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView report_ready_txt,rs_txt;

        RelativeLayout itemLayout;
        RecyclerView sub_category_rv;
        CheckBox checkBox;
        public ViewHolder(View itemView, boolean isHeader) {
            super(itemView);

            itemLayout = (RelativeLayout) itemView.findViewById(R.id.test_item_layout);
            checkBox = (CheckBox) itemView.findViewById(R.id.check_box);
            report_ready_txt = (TextView) itemView.findViewById(R.id.report_ready_txt);
            sub_category_rv= (RecyclerView) itemView.findViewById(R.id.sub_category_rv);
            rs_txt=(TextView) itemView.findViewById(R.id.rs_txt);




        }


    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<Subcategory> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        list = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }



public void getAttributes(int cat){

    // params.put("device_id", device_id);
      /*  params.put("device_type", device_type);
        params.put("device_token", device_token);*/
if(customDialog!=null)
    customDialog.show();

    Call<Categories_response> call = apiInterface.getAttributes("1539874186",GlobalData.accessToken,cat);
    call.enqueue(new Callback<Categories_response>() {
        @Override
        public void onResponse(@NonNull Call<Categories_response> call, @NonNull Response<Categories_response> response) {
            Log.d("Sccess", "onResponse: " + response.isSuccessful());
            if(customDialog!=null)
                customDialog.hide();
            if (response.isSuccessful()) {
                //     SharedHelper.putKey(MyApplication.getInstance(), Constants.PREF.PROFILE_ID, "" + "1");
                //   SharedHelper.putKey(MyApplication.getInstance(), Constants.PREF.CURRENCY, "" + response.body().getCurrency());

                GlobalData.categories_response=response.body();

                //   String s=response.body().getData().getRole();
                //    GlobalData.ROLE = response.body().getData().getRole();
                // SharedHelper.putKey(MyApplication.getInstance(), "role", response.body().getData().getRoles().get(0).getName());
            } else try {
                ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);

            } catch (JsonSyntaxException e) {

            }
        }

        @Override
        public void onFailure(@NonNull Call<Categories_response> call, @NonNull Throwable t) {
            Log.d("onFailure", "onFailure: " + t.getMessage());

        }
    });
}



}
