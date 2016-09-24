package com.example.aki.gankdemo.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.aki.gankdemo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


/**
 * Created by Aki on 2016/9/22.
 */

public class MeiziActivity extends AppCompatActivity {

    public static final String IMG_URL = "img_url";
    public static final String IMG_TITLE = "img_title";

    private String mImgUrl;
    private String mImgTitle;
    private Toolbar mToolbar;
    private ImageView mImageView;

    public static Intent newIntent (Context context, String url, String desc) {
        Intent intent = new Intent(context, MeiziActivity.class);
        intent.putExtra(MeiziActivity.IMG_URL, url);
        intent.putExtra(MeiziActivity.IMG_TITLE, desc);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meizi);
        mImgUrl = getIntent().getStringExtra(IMG_URL);
        mImgTitle = getIntent().getStringExtra(IMG_TITLE);
        mToolbar = (Toolbar) findViewById(R.id.meizi_tb);
        mImageView = (ImageView) findViewById(R.id.meizi_ori);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setTitle(mImgTitle);

        Glide.with(this)
                .load(mImgUrl)
                .into(mImageView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_meizi, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save_picture:
                new Thread(new DownloadPicture()).start();

        }
        return super.onOptionsItemSelected(item);
    }

    public class DownloadPicture implements Runnable{

        @Override
        public void run() {

            Bitmap bitmap = null;

            try {
                bitmap = Glide.with(MeiziActivity.this)
                        .load(mImgUrl)
                        .asBitmap()
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();             // cannot run on main thread


                if (bitmap != null) {
                    saveToFolder(bitmap);
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

//            try {
//                byte[] bitmapBytes = NetworkUtils.getUrlBytes(mImgUrl);
//                bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
//
//                if (bitmap != null) {
//                    saveToFolder(bitmap);
//                }
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }



        private void saveToFolder(Bitmap bitmap) {
            String albumName = "GankMeizi";
            File fileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);

            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            String filename = "Gank " + mImgTitle + ".jpg";
            final File imgFile = new File(fileDir, filename);

            FileOutputStream fos = null;

            if (!imgFile.exists()) {

                try {
                    fos = new FileOutputStream(imgFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();

                    Snackbar.make(mImageView, imgFile.getAbsolutePath(), Snackbar.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                Snackbar.make(mImageView, "already exists", Snackbar.LENGTH_SHORT).show();
            }

            MeiziActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(imgFile)));
        }


    }
}
