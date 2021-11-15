package activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.excellabs.R;

import adapter.Stuff_completed_task_Adapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import helper.GlobalData;

public class Completed_tasks_Activity extends AppCompatActivity {

    @BindView(R.id.back_img)
    ImageView back_img;


    @BindView(R.id.completed_task_rv)
    RecyclerView completed_task_rv;

    Stuff_completed_task_Adapter stuff_completec_task_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_tasks_);
        ButterKnife.bind(this);

if(GlobalData.completedTasks_order_list!=null&&GlobalData.completedTasks_order_list.size()>0) {
    stuff_completec_task_adapter = new Stuff_completed_task_Adapter(GlobalData.completedTasks_order_list, Completed_tasks_Activity.this, true);

    completed_task_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    // tomorrow_task_rv.setLayoutManager(mLayoutManager);
    completed_task_rv.setItemAnimator(new DefaultItemAnimator());
    completed_task_rv.setAdapter(stuff_completec_task_adapter);
    back_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });
}
    }

  /*  @OnClick({R.id.back_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_img:
                finish();
                break;









        }
    }*/
}