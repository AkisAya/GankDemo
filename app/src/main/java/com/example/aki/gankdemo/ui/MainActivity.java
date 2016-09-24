package com.example.aki.gankdemo.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.aki.gankdemo.R;
import com.example.aki.gankdemo.adpter.MeiziAdpter;
import com.example.aki.gankdemo.model.Meizi;
import com.example.aki.gankdemo.util.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private Toolbar mToolbar;
    private RecyclerView mGankContent; // only girls now
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Meizi> mMeiziList;
    private int mPage = 1;
    private boolean isRefreshing = false;
    private boolean isLoading = false;
    private final int LIMITPICS = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMeiziList = new ArrayList<>();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGankContent.smoothScrollToPosition(0);
            }
        });


        mGankContent = (RecyclerView) findViewById(R.id.recycler_view);
        mGankContent.setLayoutManager(new LinearLayoutManager(this));

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        // 刷新状态的颜色，每秒一种颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.orange, R.color.green);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        if (!isRefreshing) {
            mSwipeRefreshLayout.setRefreshing(true);
            loadData();
        }

        mGankContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                int first = layoutManager.findFirstVisibleItemPosition();
                int last = layoutManager.findLastVisibleItemPosition();
                int total = layoutManager.getItemCount();

                Log.d("onScroll", "Last Position: " + last + " Total: " + total);

                if (!isLoading && last + 2 >= total && total < LIMITPICS) {
                    loadMore();
                } else if (last == total - 1) {
                    Snackbar.make(mGankContent, "没有更多图了", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Snackbar.make(mGankContent, "TODO", Snackbar.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        if (!isRefreshing) {
            loadData();
        }
    }

//    private void test() {
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeRefreshLayout.setRefreshing(false);
//
//            }
//        }, 3000);
//    }

    private void loadData() {
        isRefreshing = true;
        String urlString = "http://gank.io/api/data/福利/10/1";

        NetworkUtils.FetchDataFromUrl(this, urlString, new NetworkUtils.DataCallbackListener() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                Type type = new TypeToken<List<Meizi>>() {}.getType();
                mMeiziList = gson.fromJson(jsonArray.toString(), type);
                mGankContent.setAdapter(new MeiziAdpter(MainActivity.this, mMeiziList));

                isRefreshing = false;
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Exception e) {
                mSwipeRefreshLayout.setRefreshing(false);
                Snackbar.make(mGankContent, "网络异常", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    private void loadMore() {
        mSwipeRefreshLayout.setRefreshing(true);
        isLoading = true;
        mPage++;
        String urlString = "http://gank.io/api/data/福利/10/" + mPage;

        NetworkUtils.FetchDataFromUrl(this, urlString, new NetworkUtils.DataCallbackListener() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                Type type = new TypeToken<List<Meizi>>() {}.getType();
//                    int positionStart = mMeiziList.size();
                ArrayList<Meizi> newList = gson.fromJson(jsonArray.toString(), type);
                mMeiziList.addAll(newList);
                mGankContent.getAdapter().notifyDataSetChanged();
                isLoading = false;
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, "Load More " + mMeiziList.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Snackbar.make(mGankContent, "网络异常", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
