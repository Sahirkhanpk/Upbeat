package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.upbeat.R;

import java.util.ArrayList;
import java.util.List;

import helper.GlobalData;
import models.Categories_Datum;
import models.Subcategory;

public class sub_categries_adapter extends RecyclerView.Adapter<adapters.sub_categries_adapter.ViewHolder>{

    Context context;
    private List<Subcategory> list = new ArrayList<>();
    private List<Subcategory> listreset = new ArrayList<>();
    private LayoutInflater inflater;
    sub_categries_adapter.CategoryAdapterListener categoryAdapterListener;

    int checked_Position=0;
    public sub_categries_adapter(Context context, List<Subcategory> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;

    }

    @Override
    public sub_categries_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            /*case VIEW_TYPE_HEADER:
                v = inflater.inflate(R.layout.product_list_item, parent, false);
                return new ViewHolder(v, false);*/
        /*    case VIEW_TYPE_ITEM:
                v = inflater.inflate(R.layout.product_list_item, parent, false);
                return new ViewHolder(v, false);*/
            default:
                v = inflater.inflate(R.layout.sub_categries_item, parent, false);
                return new sub_categries_adapter.ViewHolder(v, false);
        }
       /* View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_addons_list_item, parent, false);
        return new sub_categries_adapter.ViewHolder(itemView,false);*/
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
    public void onBindHeaderViewHolder(sub_categries_adapter.ViewHolder holder, final int section) {
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
    public void onBindViewHolder(sub_categries_adapter.ViewHolder holder, int absolutePosition) {

        final Subcategory object = list.get(absolutePosition);
       /* holder.productName.setText(object.getName());
        String c = object.getCategoryName();
        holder.noAddOns.setText(object.getCategoryName());*/
//        holder.noAddOns.setVisibility(View.VISIBLE);
        holder.category_name.setText(object.getName());

        if(object.getImage()!=null)
            Glide.with(context)
                    .load(object.getImage())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .skipMemoryCache(false)
                            .placeholder(R.drawable.man)
                            .error(R.drawable.man))
                    .into(holder.icon_category);



  holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  if (categoryAdapterListener != null)
                  {

int a = absolutePosition;
                      categoryAdapterListener.onCatClick(absolutePosition,list.get(absolutePosition).getSubcategories(),list.get(absolutePosition).getName());}
            }
        });


       /* holder.Report_ready_txt.setText(object.getScheduleDateTime());
        holder.Txt_account_number.setText(object.getActualAmount());
        holder.Txt_status.setText(object.getOrderStatus());
        holder.Txt_status_code.setText(object.getPromocode().toString());
        if(object.getOrderStatus().equals("In progress"))
            holder.Txt_status.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        else if(object.getOrderStatus().equals("Completed"))
            holder.Txt_status.setTextColor(context.getResources().getColor(R.color.colorGreen));
        else if(object.getOrderStatus().equals("Cancelled"))
            holder.Txt_status.setTextColor(context.getResources().getColor(R.color.colorRedPure));
        else if(object.getOrderStatus().equals("Scheduled"))
            holder.Txt_status.setTextColor(context.getResources().getColor(R.color.colorRed));*/


        //  holder.Test_name_txt.setText(object.getTestBy());
       /* holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  if (productAdapterListener != null)
                holder.itemLayout.setCardBackgroundColor(context.getResources().getColor(R.color.jangleGreen));
                holder.icon_category.setColorFilter(context.getResources().getColor(R.color.colorWhite));
                holder.category_name.setTextColor(context.getResources().getColor(R.color.colorWhite));


                //  productAdapterListener.onProductClick(object);
            }
        });*/


      /*  holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checked_Position=absolutePosition;
                notifyDataSetChanged();
            }
        });
        if(checked_Position==absolutePosition){
            holder.itemLayout.setCardBackgroundColor(context.getResources().getColor(R.color.jangleGreen));
            holder.icon_category.setColorFilter(context.getResources().getColor(R.color.colorWhite));
            holder.category_name.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }
        else
        {
            holder.itemLayout.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            holder.icon_category.setColorFilter(context.getResources().getColor(R.color.colorTransparent));
            holder.category_name.setTextColor(context.getResources().getColor(R.color.primaryTextColor));
        }
*/
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

    public void setCatAdapterListener(sub_categries_adapter.CategoryAdapterListener categoryAdapterListener) {
        this.categoryAdapterListener = categoryAdapterListener;
    }

    public interface CategoryAdapterListener {
        void onCatClick(int  category,List<Subcategory> list,String catName);

        void onProductDeleteClick(String category);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView header;
        TextView noAddOns;
        TextView category_name,Txt_status,Test_name_txt,Txt_status_code;
        TextView Report_ready_txt;
        ImageView productImg;
        ImageView closeImg,icon_category;
        //    RelativeLayout itemLayout;
        LinearLayout critical;
        View v;
        CardView itemLayout;

        public ViewHolder(View itemView, boolean isHeader) {
            super(itemView);

            itemLayout = (CardView) itemView.findViewById(R.id.task_card);
         /*   Report_ready_txt=(TextView) itemView.findViewById(R.id.report_ready_txt);
            Txt_account_number=(TextView) itemView.findViewById(R.id.txt_account_number);
            Txt_status =(TextView) itemView.findViewById(R.id.txt_status);
            Test_name_txt=(TextView) itemView.findViewById(R.id.test_name_txt);*/
            category_name=(TextView) itemView.findViewById(R.id. category_name);
            icon_category = (ImageView) itemView.findViewById(R.id.icon_category);



            //    v =(View) itemView.findViewById(R.id.view);
             /*   productName = (TextView) itemView.findViewById(R.id.product_name_txt);
                noAddOns = (TextView) itemView.findViewById(R.id.no_addons_txt);
                addOns = (TextView) itemView.findViewById(R.id.addons_name_txt);
                productImg = (ImageView) itemView.findViewById(R.id.product_img);
                closeImg = (ImageView) itemView.findViewById(R.id.close_img);*/


        }


    }

    public void filterList(List<Subcategory> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        listreset=list;
        list = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
    public void filterListreset(List<Subcategory> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
    list= filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
}
