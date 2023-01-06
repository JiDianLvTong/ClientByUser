package com.android.jidian.client;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
import com.bumptech.glide.Glide;
import com.permissionx.guolindev.PermissionX;

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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hasee on 2017/6/6.
 * 个人信息
 */
@EActivity(R.layout.main_info)
public class MainInfo extends BaseActivity {
    @ViewById
    LinearLayout authentication_panel, change_phoen;
    @ViewById
    TextView phoneView, authenticationView, content;
    @ViewById
    CircleImageView circleImageView;

    @AfterViews
    void afterViews() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //个人信息页面访问
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_PERSONAL_INFO);
            }
        }, 500);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        uid = sharedPreferences.getString("id", "");
        if (PubFunction.isConnect(activity, true)) {
            HttpGetUserInfo_2();
            progressDialog.show();
        }
    }

    @Click
    void page_return() {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    /**
     * http接口：User/personal.html   获取用户信息
     */
    @Background
    void HttpGetUserInfo_2() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        new OkHttpConnect(activity, PubFunction.app + "User/personal.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetUserInfo_2(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpGetUserInfo_2(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
                System.out.println(jsonObject_response);
                if ("1".equals(status)) {
                    final JSONObject jsonObject = jsonObject_response.getJSONObject("data");
                    final String img = jsonObject.getString("avater");
                    final String phone = jsonObject.getString("phone");
                    String str1 = phone.substring(0, 3);
                    String str2 = phone.substring(7, 11);
                    String ali_face_url = jsonObject.optString("ali_face_url");
                    String ali_face_appcode = jsonObject.optString("ali_face_appcode");
                    change_phoen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //点击手机号
                        }
                    });
                    phoneView.setText(str1 + " **** " + str2);
                    Glide.with(activity).load(img).into(circleImageView);
                    content.setText("吉电出行祝您换电无忧，一路畅行");
                    authenticationView.setText(jsonObject.getString("id_auth_tip"));
                    if ("未实名认证".equals(authenticationView.getText().toString())) {
                        authentication_panel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //点击实名认证
                                PermissionX.init(MainInfo.this)
                                        .permissions(Manifest.permission.CAMERA)
                                        .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                                        .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                                        .request((allGranted, grantedList, deniedList) -> {
                                            if (allGranted) {
                                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_PERSONAL_INFO_AUTH);
                                                Intent intent = new Intent(activity, MainAuthentication_.class);
                                                intent.putExtra("ali_face_url", ali_face_url);
                                                intent.putExtra("ali_face_appcode", ali_face_appcode);
                                                activity.startActivity(intent);
                                            } else {
                                                MyToast.showTheToast(MainInfo.this, "当前应用缺少必要权限 ");
                                            }
                                        });
                            }
                        });
                    } else {
                        authentication_panel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //点击实名认证
                                PermissionX.init(MainInfo.this)
                                        .permissions(Manifest.permission.CAMERA)
                                        .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                                        .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                                        .request((allGranted, grantedList, deniedList) -> {
                                            if (allGranted) {
                                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_PERSONAL_INFO_AUTH);
                                                String id_card = "";
                                                String real_name = "";
                                                String front_img = "";
                                                String reverse_img = "";
                                                try {
                                                    real_name = jsonObject.getString("real_name");
                                                    id_card = jsonObject.getString("id_card");
                                                    reverse_img = jsonObject.getString("reverse_img");
                                                    front_img = jsonObject.getString("front_img");
                                                } catch (JSONException e) {
                                                    System.out.println(e.getLocalizedMessage());
                                                }
                                                Intent intent = new Intent(activity, MainAuthenticationIS_.class);
                                                intent.putExtra("id_card", id_card);
                                                intent.putExtra("real_name", real_name);
                                                intent.putExtra("front_img", front_img);
                                                intent.putExtra("reverse_img", reverse_img);
                                                activity.startActivity(intent);
                                            } else {
                                                MyToast.showTheToast(MainInfo.this, "当前应用缺少必要权限 ");
                                            }
                                        });
                            }
                        });
                    }
                } else {
                    MyToast.showTheToast(activity, msg);
                }
            } catch (Exception e) {
                MyToast.showTheToast(activity, "JSON：" + e.toString());
            }
        }
    }
}