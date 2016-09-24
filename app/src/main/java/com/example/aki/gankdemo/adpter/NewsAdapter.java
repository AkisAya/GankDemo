package com.example.aki.gankdemo.adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aki.gankdemo.R;
import com.example.aki.gankdemo.model.BaseData;
import com.example.aki.gankdemo.ui.WebViewActivity;

import java.util.List;

/**
 * Created by Aki on 2016/9/24.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    private Context mContext;
    private List<BaseData> mNewsList;
    private LayoutInflater mLayoutInflater;

    public NewsAdapter(Context context, List<BaseData> newsList) {
        mContext = context;
        mNewsList = newsList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public class NewsHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public NewsHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.textview);
        }

        public void bindData(final BaseData news) {
            mTextView.setText("●  " + news.getDesc());
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = WebViewActivity.newIntent(mContext, news.getUrl(), news.getDesc());
                    mContext.startActivity(intent);
                }
            });
        }

    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_recycler_view_news, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        holder.bindData(mNewsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }


}
