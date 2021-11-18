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

import models.filter_response_Datum;


public class filters_items_adapter extends RecyclerView.Adapter<adapters.filters_items_adapter.ViewHolder> {

    Context context;
    private List<filter_response_Datum> list = new ArrayList<>();
    private List<filter_response_Datum> listreset = new ArrayList<>();
    private LayoutInflater inflater;
    filters_items_adapter.CategoryAdapterListener categoryAdapterListener;



    public filters_items_adapter(Context context, List<filter_response_Datum> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;

    }

    @Override
    public filters_items_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            default:
                v = inflater.inflate(R.layout.item_searched_layout, parent, false);
                return new filters_items_adapter.ViewHolder(v, false);
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public void setList(List<filter_response_Datum> list) {
        this.list = list;
    }

    @Override
    public void onBindViewHolder(filters_items_adapter.ViewHolder holder, int absolutePosition) {

        final filter_response_Datum object = list.get(absolutePosition);

        holder.category_name.setText(object.getCategoryName());
        if(object.getPrice()!=null)
     holder.category_price.setText(object.getPrice().toString()+" AED");
        String url="";
        if (object.getImages() != null)
            Glide.with(context).load(object.getImages().get(0))
                    .apply(new RequestOptions()
                            .centerCrop().
                                    placeholder(R.drawable.man)
                            .error(R.drawable.man).dontAnimate()).
                    into(holder.icon_category);
          /*  Glide.with(context)
                    .load(object.getImages().get(0))
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .skipMemoryCache(false)
                            .placeholder(R.drawable.man)
                            .error(R.drawable.man))
                    .into(holder.icon_category);*/


    }

    public void setCatAdapterListener(filters_items_adapter.CategoryAdapterListener categoryAdapterListener) {
        this.categoryAdapterListener = categoryAdapterListener;
    }

    public interface CategoryAdapterListener {
        void onCatClick(int category, List<filter_response_Datum> list, String catName);

        void onProductDeleteClick(String category);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView category_name, category_price;

        ImageView  icon_category;
        //    RelativeLayout itemLayout;
        LinearLayout critical;
        View v;
        CardView itemLayout;

        public ViewHolder(View itemView, boolean isHeader) {
            super(itemView);

            itemLayout = (CardView) itemView.findViewById(R.id.task_card);

            category_name = (TextView) itemView.findViewById(R.id.category_name);
            icon_category = (ImageView) itemView.findViewById(R.id.icon_category);
            category_price = (TextView) itemView.findViewById(R.id.category_price);


        }


    }

    public void filterList(List<filter_response_Datum> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        listreset = list;
        list = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    public void filterListreset(List<filter_response_Datum> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        list = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
}

