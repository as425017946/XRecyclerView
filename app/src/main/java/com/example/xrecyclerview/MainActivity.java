package com.example.xrecyclerview;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    XRecyclerView xRecyclerView;
    //数据集合
    private List<String >list=new ArrayList<>();
    //获取数据的开始
    private int curr;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xRecyclerView = findViewById(R.id.RV_all);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);

        //第一次获取数据
        curr=0;
        getData(curr);

        //允许上拉加载
        xRecyclerView.setLoadingMoreEnabled(true);
        //允许下拉刷新
        xRecyclerView.setPullRefreshEnabled(true);

        /**
         *    可以设置加载更多的样式，很多种
          */
        //这个是下拉加载展示
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        //这个是上拉加载更多展示
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallGridBeat);
        //下载加载的时候展示
        xRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
        // 如果设置上这个，下拉刷新的时候会显示上次刷新的时间
        xRecyclerView
                .getDefaultRefreshHeaderView()
                .setRefreshTimeVisible(true);
        //自定义文字
        xRecyclerView.getDefaultFootView().setLoadingHint("自定义加载中提示");
        xRecyclerView.getDefaultFootView().setNoMoreHint("自定义加载完毕提示");
        //下拉刷新和上拉加载
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //当下拉刷新的时候，重新获取数据，所有curr要变回0，并且把集合list清空
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       curr=0;
                       list.clear();
                       getData(curr);
                       xRecyclerView.refreshComplete();
                       xRecyclerView.refreshComplete();
                   }
               },1000);

            }

            @Override
            public void onLoadMore() {
                //当上拉加载的时候，因为一次获取是30个数据，所也在获取的时候就要在加10的地方开始获取
//                如：第一次0——30；
//                    第二次30——59；
//                SystemClock.sleep(1000);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        curr=curr+30;
                        getData(curr);
                        xRecyclerView.loadMoreComplete();
                    }
                },2000);

            }
        });
    }
    private void getData(int number){
        for (int i=number;i<number+30;i++){
            list.add("数据是第"+i+"个");
        }

        //调用Adapter展示数据，这个判断是为了不重复创建MyAdapter的对象
        if (adapter==null){
            adapter=new MyAdapter(list,MainActivity.this);
            xRecyclerView.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }

    }

    private  class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<String > list=new ArrayList<>();
        private Context context;

        public MyAdapter(List<String> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //给Adapter添加布局，bq把这个view传递给HoldView，让HoldView找到空间
            View view= LayoutInflater.from(context).inflate(R.layout.xrecyc_adapter, parent,false);
            HoldView holdView=new HoldView(view);
            return holdView;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            //position为Adapter的位置，数据从list里面可以拿出来。
            String s=list.get(position);
            ((HoldView)holder).textView.setText(s);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
        private class HoldView extends RecyclerView.ViewHolder{
            private TextView textView;
            public HoldView(View itemView) {
                super(itemView);
                //根据onCreateViewHolder的HoldView所添加的xml布局找到空间
                textView= (TextView) itemView.findViewById(R.id.xrecyc_text);
            }
        }
    }
}
