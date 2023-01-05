package com.android.jidian.client;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;

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

/**
 * Created by hasee on 2017/6/6.
 */
@EActivity(R.layout.main_info_phone)
public class MainInfoPhone extends BaseActivity {
    @ViewById
    LinearLayout page_return, shadow;
    @ViewById
    EditText new_phone, old_phone, code;
    @ViewById
    TextView getcode, submit, my_phone, shadow_textView;

    @AfterViews
    void afterVies() {
        my_phone.setText(getIntent().getStringExtra("phone"));
        isSetPhone();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        uid = sharedPreferences.getString("id", "");
    }

    @Click({R.id.page_return, R.id.getcode, R.id.submit})
    void click(View v) {
        if (v.getId() == R.id.page_return) {
            this.finish();
        } else if (R.id.getcode == v.getId()) {
            String str = new_phone.getText().toString();
            int str_l = str.length();
            if (TextUtils.isEmpty(str)) {
                MyToast.showTheToast(this, "手机号不能为空！");
            } else if (str_l == 11) {
                if (PubFunction.isConnect(activity, true)) {
                    HttpGetCode(str);
                    progressDialog.show();
                }
            } else {
                MyToast.showTheToast(this, "请输入正确的手机格式！");
            }
        } else if (R.id.submit == v.getId()) {
            String old_phone_str = old_phone.getText().toString();
            String new_phone_str = new_phone.getText().toString();
            String code_str = code.getText().toString();
            int str_p_l_o = old_phone_str.length();
            int str_p_l_n = new_phone_str.length();

            if (TextUtils.isEmpty(new_phone_str) || TextUtils.isEmpty(old_phone_str)) {
                MyToast.showTheToast(this, "手机号不能为空！");
            } else if (TextUtils.isEmpty(code_str)) {
                MyToast.showTheToast(this, "验证码不能为空！");
            } else if (str_p_l_o != 11 || str_p_l_n != 11) {
                MyToast.showTheToast(this, "请输入正确的手机格式！");
            } else {
                if (PubFunction.isConnect(activity, true)) {
                    HttpChangePhone(new_phone_str, old_phone_str, code_str);
                    progressDialog.show();
                }
            }
        }
    }

    /**
     * http接口：Sms/msgSend.html    获取短信验证码
     */
    @Background
    void HttpGetCode(String mobile) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("phone", mobile));
        new OkHttpConnect(activity, PubFunction.api + "Sms/msgSend.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetCode(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpGetCode(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                System.out.println(jsonObject);
                MyToast.showTheToast(activity, jsonObject.getString("msg"));
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
                MyToast.showTheToast(activity, "JSON：" + e);
            }
        }
    }

    /**
     * http接口：User/setPhone.html    更改手机号
     */
    @Background
    void HttpChangePhone(String new_phone_str, String old_phone_str, String code_str) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("phone", new_phone_str));
        dataList.add(new ParamTypeData("vcode", code_str));
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("old_phone", old_phone_str));
        new OkHttpConnect(activity, PubFunction.app + "User/setPhone.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpChangePhone(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpChangePhone(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
                System.out.println(jsonObject_response);
                if ("1".equals(status)) {
                    activity.finish();
                }
                MyToast.showTheToast(activity, msg);
            } catch (Exception e) {
                MyToast.showTheToast(activity, "JSON：" + e.toString());
            }
        }
    }

    /**
     * http接口：Sms/msgSend.html    获取短信验证码
     */
    @Background
    void isSetPhone() {
        List<ParamTypeData> dataList = new ArrayList<>();
        new OkHttpConnect(activity, PubFunction.app + "User/isSetPhone.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onIsSetPhone(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onIsSetPhone(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                int status = jsonObject_response.optInt("status");
                String msg = jsonObject_response.optString("msg");
                int show = jsonObject_response.optInt("show");
                if (status == 1) {
                    if (show == 1) {
                        shadow.setVisibility(View.VISIBLE);
                        old_phone.setEnabled(false);
                        new_phone.setEnabled(false);
                        code.setEnabled(false);
                        getcode.setEnabled(false);
                        submit.setEnabled(false);
                        shadow_textView.setText(msg);
                    } else {
                        shadow.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();
                }
                System.out.println(jsonObject_response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}