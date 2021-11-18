package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upbeat.R;

import java.util.ArrayList;
import java.util.List;

import activities.FilterActivity;
import helper.GlobalData;
import models.Categories_Datum;
import models.Categories_Datum;

public class filter_adapter extends RecyclerView.Adapter<adapters.filter_adapter.ViewHolder>{

    Context context;
    private List<Categories_Datum> list = new ArrayList<>();
    private List<Integer> list_needed = new ArrayList<>();
    private LayoutInflater inflater;
    adapters.filter_adapter.ProductAdapterListener productAdapterListener;


    public filter_adapter(Context context, List<Categories_Datum> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;


    }

    @Override
    public filter_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
                return new filter_adapter.ViewHolder(v, false);
        }
       /* View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_addons_list_item, parent, false);
        return new filter_adapter.ViewHolder(itemView,false);*/


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
    public void onBindHeaderViewHolder(filter_adapter.ViewHolder holder, final int section) {
      //  holder.header.setText(list.get(section).getHeader());
       *//* holder.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  System.out.println(list.get(section).getHeader());
            }
        });*//*
    }*/

    public void setList(List<Categories_Datum> list) {
        this.list = list;
    }

    @Override
    public void onBindViewHolder(filter_adapter.ViewHolder holder, int absolutePosition) {

        final Categories_Datum object = list.get(absolutePosition);
       /* holder.productName.setText(object.getName());
        String c = object.getCategoryName();
        holder.noAddOns.setText(object.getCategoryName());*/
//        holder.noAddOns.setVisibility(View.VISIBLE);
        holder.report_ready_txt.setText(object.getName());




        //in some cases, it will prevent unwanted situations
        holder.checkBox.setOnCheckedChangeListener(null);

        if(list.get(absolutePosition).getSubcategories()!=null&&list.get(absolutePosition).getSubcategories().size()>0) {
            filter_sub_adapter sub_adapter;
            sub_adapter = new filter_sub_adapter(context, list.get(absolutePosition).getSubcategories());
            holder.sub_category_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            holder.sub_category_rv.setHasFixedSize(true);
            holder.sub_category_rv.setAdapter(sub_adapter);
        }

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

                        holder.sub_category_rv.setVisibility(View.VISIBLE);}




                }else {
                    holder.sub_category_rv.setVisibility(View.GONE);

                }
                GlobalData.catlist=list_needed;
            }
        });



      /*  holder.closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productAdapterListener != null) {
                    AlertDialog.Builder cancelAlert = new AlertDialog.Builder(context);
                    cancelAlert.setTitle(context.getResources().getString(R.string.product));
                    cancelAlert.setMessage(context.getResources().getString(R.string.are_you_sure_want_to_delete_product));
                    cancelAlert.setPositiveButton(context.getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            productAdapterListener.onProductDeleteClick(object);
                        }
                    });
                    cancelAlert.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    cancelAlert.show();
                }
            }
        });*/

/*
        if (object.getImages() != null && object.getImages().size() > 0) {
            List<Image> images = object.getImages();
            if (images != null && images.size() > 0) {
                String img = images.get(0).getUrl();
                Glide.with(context).load(img)
                        .apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_place_holder_image).error(R.drawable.ic_place_holder_image).dontAnimate()).into(holder.productImg);
            }*/


       /* if (object.getFeaturedImage()!= null ) {

                String img = object.getFeaturedImage();
                Glide.with(context).load(img)
                        .apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_place_holder_image).error(R.drawable.ic_place_holder_image).dontAnimate()).into(holder.productImg);

        }*/
/*
        if (object.getAddons() != null && object.getAddons().size() > 0) {
            List<Addon> addonsList = object.getAddons();
            String addOnNames = "";
            for (int i = 0; i < addonsList.size(); i++) {
                if (addonsList.get(i).getAddon() != null) {
                    if (i == 0) {
                        addOnNames = addonsList.get(i).getAddon().getName();
                    } else {
                        addOnNames = addOnNames + "," + addonsList.get(i).getAddon().getName();
                    }
                }

            }
            holder.addOns.setVisibility(View.VISIBLE);
            holder.noAddOns.setVisibility(View.GONE);

            holder.addOns.setText(addOnNames);
        } else {
            holder.addOns.setVisibility(View.GONE);
            holder.noAddOns.setVisibility(View.VISIBLE);
        }*/

    }

    public void setProductAdapterListener(adapters.filter_adapter.ProductAdapterListener productAdapterListener) {
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

             /*   productName = (TextView) itemView.findViewById(R.id.product_name_txt);
                noAddOns = (TextView) itemView.findViewById(R.id.no_addons_txt);
                addOns = (TextView) itemView.findViewById(R.id.addons_name_txt);
                productImg = (ImageView) itemView.findViewById(R.id.product_img);
                closeImg = (ImageView) itemView.findViewById(R.id.close_img);*/


        }


    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<Categories_Datum> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        list = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }







}

