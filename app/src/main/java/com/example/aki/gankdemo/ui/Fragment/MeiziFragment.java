package com.example.aki.gankdemo.ui.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aki.gankdemo.R;
import com.example.aki.gankdemo.adpter.MeiziAdpter;
import com.example.aki.gankdemo.model.BaseData;
import com.example.aki.gankdemo.util.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aki on 2016/9/24.
 */

public class MeiziFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView mGankContent;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<BaseData> mMeiziList;
    private MeiziAdpter mMeiziAdpter;
    private int mPage = 1;
    private boolean isRefreshing = false;
    private boolean isLoading = false;
    private final int LIMITPICS = 40;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_base, container, false);

        mMeiziList = new ArrayList<>();
        mGankContent = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mGankContent.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
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

        return rootView;

    }

    @Override
    public void onRefresh() {
        if (!isRefreshing) {
            loadData();
        }
    }

    private void loadData() {
        isRefreshing = true;
        String urlString = "http://gank.io/api/data/福利/10/1";

        NetworkUtils.FetchDataFromUrl(getActivity(), urlString, new NetworkUtils.DataCallbackListener() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                Type type = new TypeToken<List<BaseData>>() {}.getType();
                mMeiziList = gson.fromJson(jsonArray.toString(), type);
                Log.d("loadData", "Load + " + mMeiziList.size());
                mGankContent.setAdapter(new MeiziAdpter(getActivity(), mMeiziList));

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

        NetworkUtils.FetchDataFromUrl(getActivity(), urlString, new NetworkUtils.DataCallbackListener() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                Type type = new TypeToken<List<BaseData>>() {}.getType();
//                    int positionStart = mMeiziList.size();
                ArrayList<BaseData> newList = gson.fromJson(jsonArray.toString(), type);
                mMeiziList.addAll(newList);
                mGankContent.getAdapter().notifyDataSetChanged();
                isLoading = false;
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "Load More " + mMeiziList.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Snackbar.make(mGankContent, "网络异常", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
