package com.example.aki.gankdemo.adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aki.gankdemo.R;
import com.example.aki.gankdemo.model.BaseData;
import com.example.aki.gankdemo.ui.MeiziActivity;

import java.util.List;

/**
 * Created by Aki on 2016/9/21.
 */

public class MeiziAdpter extends RecyclerView.Adapter<MeiziAdpter.MeiziHolder> {


    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<BaseData> mMeiziList;
    public static int mPosition = RecyclerView.NO_POSITION;

    public MeiziAdpter(Context context, List<BaseData> meiziList) {
        mContext = context;
        mMeiziList = meiziList;
        mLayoutInflater = LayoutInflater.from(context);
    }


    // ViewHolder
    public class MeiziHolder extends RecyclerView.ViewHolder {

        private ImageView mMeiziView;

        public MeiziHolder(View itemView) {
            super(itemView);

            mMeiziView = (ImageView) itemView.findViewById(R.id.meizi);

        }

        public void BindData(BaseData meizi, final int position) {
            final String url = meizi.getUrl();
            final String desc = meizi.getDesc();

            Glide.with(mContext)
                    .load(url)
                    .into(mMeiziView);

            mMeiziView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPosition = position;
                    Intent intent = MeiziActivity.newIntent(mContext, url, desc);
                    mContext.startActivity(intent);
                }
            });

        }
    }


    @Override
    public MeiziHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_recycler_view, parent, false);
        return new MeiziHolder(view);
    }

    @Override
    public void onBindViewHolder(MeiziHolder holder, int position) {
        BaseData meizi = mMeiziList.get(position);
        holder.BindData(meizi, position);
    }

    @Override
    public int getItemCount() {
        return mMeiziList.size();
    }
}

