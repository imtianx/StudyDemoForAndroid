package cn.imtianx.swrvdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RvAdapter rvAdapter;
    private List<String> datas = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        recyclerView = (RecyclerView) findViewById(R.id.relv);


        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));


        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary,
                R.color.test1, R.color.test2);
//        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        datas.add(0, "刷新的数据");
                        rvAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }

        });

        for (int i = 0; i < 30; i++) {
            datas.add("标题" + i);
        }
        rvAdapter = new RvAdapter(datas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(rvAdapter);

        rvAdapter.setItemClickListener(new RvAdapter.OnItemClickListener() {
            @Override
            public void onTextClick(View view, int position) {
                Toast.makeText(MainActivity.this, "你点击了item" + position+" 的文字", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "你点击了item（cardview）" + position, Toast.LENGTH_SHORT).show();
            }
        });

    }


}
