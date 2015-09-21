package com.cjj.android_materialdelete;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.cjj.MaterialDeleteLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecyclerViewActivity extends BaseActivity {
    private LinearLayout fl_view;
    static List<String> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);
        initsToolbar();
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerview);
        setupRecyclerView(rv);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initsToolbar() {
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        data.add("John Palmer");
        data.add("William Owens");
        data.add("Terry Murray");
        data.add("Terry Medina");
        data.add("Hello Cjj");
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(RecyclerViewActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {


        public static class ViewHolder extends RecyclerView.ViewHolder {
            private MaterialDeleteLayout layout;
//            public final ImageView mImageView;
            private TextView tv;
            public ViewHolder(View view) {
                super(view);
//                mImageView = (ImageView) view.findViewById(R.id.avatar);
                tv = (TextView) view.findViewById(R.id.id_content_tv);
                layout = (MaterialDeleteLayout) view.findViewById(R.id.id_delete_layout);
            }


        }

        public SimpleStringRecyclerViewAdapter(Context context) {
            super();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
//            if(position==0)
//            {
//                holder.mImageView.setImageResource(R.drawable.a6);
//            }else if(position == 1)
//            {
//                holder.mImageView.setImageResource(R.drawable.a5);
//            }

            holder.layout.setDeleteListener(new MaterialDeleteLayout.SwipeDeleteListener() {
                @Override
                public void onDelete() {

//                    data.remove(position);
//                   notifyDataSetChanged();
                    Log.i("log_cjj", "position----->" + position);
                    int pos = (int) holder.layout.getTag();
                    notifyItemRemoved(pos);
                    Log.i("log_cjj", "pos----->" + pos);
                    data.remove(pos);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.layout.closeItem();
                            notifyDataSetChanged();
                        }
                    }, 1000);

                }
            });

          holder.tv.setText(data.get(position));
            holder.layout.setTag(position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
