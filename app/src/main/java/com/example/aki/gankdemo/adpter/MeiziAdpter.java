package com.example.aki.gankdemo.adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.print.PrintHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aki.gankdemo.R;
import com.example.aki.gankdemo.model.Meizi;
import com.example.aki.gankdemo.ui.MainActivity;
import com.example.aki.gankdemo.ui.MeiziActivity;

import java.util.List;

import static com.example.aki.gankdemo.ui.MeiziActivity.newIntent;

/**
 * Created by Aki on 2016/9/21.
 */

public class MeiziAdpter extends RecyclerView.Adapter<MeiziAdpter.MeiziHolder> {


    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Meizi> mMeiziList;
    public static int mPosition = RecyclerView.NO_POSITION;

    public MeiziAdpter(Context context, List<Meizi> meiziList) {
        mContext = context;
        mMeiziList = meiziList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public static int getItemClickPosition() {
        return mPosition;
    }

    // ViewHolder
    public class MeiziHolder extends RecyclerView.ViewHolder {

        private ImageView mMeiziView;
        private TextView mDescText;
        private TextView mWho;

        public MeiziHolder(View itemView) {
            super(itemView);

            mMeiziView = (ImageView) itemView.findViewById(R.id.meizi);
            mDescText = (TextView) itemView.findViewById(R.id.pic_desc);
            mWho = (TextView) itemView.findViewById(R.id.text_who);

        }

        public void BindData(Meizi meizi, final int position) {
            final String url = meizi.getUrl();
            final String desc = meizi.getDesc();
            String who = meizi.getWho();

            mDescText.setText(desc);
            mWho.setText("From: " + who);

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

            mDescText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPosition = position;
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
        Meizi meizi = mMeiziList.get(position);
        holder.BindData(meizi, position);
    }

    @Override
    public int getItemCount() {
        return mMeiziList.size();
    }
}

