package com.android.jidian.client;

import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.bumptech.glide.Glide;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.login_select_user_type)
public class LoginSelectUserType extends BaseActivity {


    @ViewById
    LinearLayout page_return;
    @ViewById
    ImageView image_01, image_02;

    private int type = 0;
    private int select_type = 0;

    @AfterViews
    void afterViews() {
        init();
    }

    private void init() {
        String urole_kuqi = sharedPreferences.getString("urole_kuqi", "");
        String urole_waimai = sharedPreferences.getString("urole_waimai", "");
        Glide.with(activity).load(urole_kuqi).into(image_01);
        Glide.with(activity).load(urole_waimai).into(image_02);
    }

    @Click
    void page_return() {
        this.finish();
    }

    @Click
    void image_01() {
        HttpGetUserType("10");
        progressDialog.show();
        select_type = 1;
    }

    @Click
    void image_02() {
        HttpGetUserType("20");
        progressDialog.show();
        select_type = 2;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (type == 0) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }

    }


    /**
     * http接口：User/selectRole.html    提交用户信息
     */
    @UiThread
    void onDataHttpGetUserType(String response, String type_str) {
        if (type_str.equals("0")) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String msg = jsonObject.getString("msg");
                String status = jsonObject.getString("status");
                if (status.equals("1")) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (select_type == 1) {
                        editor.putString("urole", "10");
                    } else if (select_type == 2) {
                        editor.putString("urole", "20");
                    }
                    editor.commit();

                    type = 1;
                    activity.finish();
                    MyToast.showTheToast(activity, msg);
                } else {
                    MyToast.showTheToast(activity, msg);
                }
            } catch (JSONException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    @Background
    void HttpGetUserType(String urole) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("urole", urole));
        new OkHttpConnect(activity, PubFunction.app + "User/selectRole.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetUserType(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }
}
