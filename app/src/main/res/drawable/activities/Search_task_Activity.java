package activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.excellabs.R;

import adapter.Stuff_completed_task_Adapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import helper.GlobalData;

public class Search_task_Activity extends AppCompatActivity {
    @BindView(R.id.completed_task_rv)
    RecyclerView completed_task_rv;

    Stuff_completed_task_Adapter stuff_completec_task_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_task_);
        ButterKnife.bind(this);

        if(GlobalData.completedTasks_order_list!=null&&GlobalData.completedTasks_order_list.size()>0) {
            stuff_completec_task_adapter = new Stuff_completed_task_Adapter(GlobalData.completedTasks_order_list, Search_task_Activity.this, true);

            completed_task_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            // tomorrow_task_rv.setLayoutManager(mLayoutManager);
            completed_task_rv.setItemAnimator(new DefaultItemAnimator());
            completed_task_rv.setAdapter(stuff_completec_task_adapter);
        }
    }

}