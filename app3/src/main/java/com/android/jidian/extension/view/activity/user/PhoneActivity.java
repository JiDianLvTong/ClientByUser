package com.android.jidian.extension.view.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.jidian.extension.R;
import com.android.jidian.extension.base.BaseActivity;
import com.android.jidian.extension.bean.LoginGetUserInfoBean;
import com.android.jidian.extension.dao.sp.UserInfoSp;
import com.android.jidian.extension.net.BaseHttp;
import com.android.jidian.extension.net.BaseHttpParameterFormat;
import com.android.jidian.extension.net.HttpUrlMap;
import com.android.jidian.extension.view.activity.main.MainActivity;
import com.android.jidian.extension.view.commonPlug.dialog.DialogByEnter;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneActivity extends BaseActivity {

    @BindView(R.id.telView)
    public TextView telView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        ButterKnife.bind(activity);
        init();
    }

    private void init(){
        dialogLoading.showPopupWindow();
        List<BaseHttpParameterFormat> baseHttpParameterFormats = new ArrayList<>();
        BaseHttp baseHttp = new BaseHttp(activity, HttpUrlMap.getTel, baseHttpParameterFormats, new BaseHttp.BaseHttpListener() {
            @Override
            public void dataReturn(int code, String errorMessage , String message , String data) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogLoading.dismiss();
                        if(code == 1){
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                String tel = jsonObject.getString("telephone");
                                telView.setText(tel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else{
                            new DialogByEnter(activity,message).showPopupWindow();
                        }
                    }
                });
            }
        });
        baseHttp.onStart();
    }

    @OnClick(R.id.pageReturn)
    public void onClickPageReturn(){
        activity.finish();
    }

}
