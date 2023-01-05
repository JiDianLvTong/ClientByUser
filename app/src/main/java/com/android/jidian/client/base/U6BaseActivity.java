package com.android.jidian.client.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.jidian.client.R;
import com.android.jidian.client.widgets.ProgressDialog;
import com.gyf.barlibrary.ImmersionBar;

import me.jessyan.autosize.AutoSizeConfig;

public class U6BaseActivity extends AppCompatActivity {

    protected static final String TAG = "BaseActivity";
    protected Activity activity;

    protected ProgressDialog progressDialog;
    protected SharedPreferences sharedPreferences;
    protected String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        sharedPreferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        uid = sharedPreferences.getString("id", "");
        progressDialog = new ProgressDialog(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}