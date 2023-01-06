package com.android.jidian.client.mvp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.MainAuthenticationIS_;
import com.android.jidian.client.MainAuthentication_;
import com.android.jidian.client.MainInfoPhone_;
import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivityByMvp;
import com.android.jidian.client.bean.UserPersonalBean;
import com.android.jidian.client.mvp.contract.PersonalInfoContract;
import com.android.jidian.client.mvp.presenter.PersonalInfoPresenter;
import com.android.jidian.client.widgets.MyToast;
import com.bumptech.glide.Glide;
import com.permissionx.guolindev.PermissionX;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalInfoActivity extends U6BaseActivityByMvp<PersonalInfoPresenter> implements PersonalInfoContract.View {

    @BindView(R.id.pageReturn)
    LinearLayout pageReturn;
    @BindView(R.id.circleImageView)
    CircleImageView circleImageView;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.change_phoen)
    LinearLayout change_phoen;
    @BindView(R.id.phoneView)
    TextView phoneView;
    @BindView(R.id.authentication_panel)
    LinearLayout authentication_panel;
    @BindView(R.id.authenticationView)
    TextView authenticationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_persional_info);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mPresenter = new PersonalInfoPresenter();
        mPresenter.attachView(this);
        mPresenter.requestUserPersonal(uid);
    }

    @OnClick(R.id.pageReturn)
    public void OnClickPagerReturn() {
        finish();
    }

    @Override
    public void requestUserPersonalSuccess(UserPersonalBean bean) {

        String img = bean.getData().getAvater();
        String phone = bean.getData().getPhone();
        String str1 = phone.substring(0, 3);
        String str2 = phone.substring(7, 11);
        String ali_face_url = bean.getData().getAli_face_url();
        String ali_face_appcode = bean.getData().getAli_face_appcode();
        change_phoen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击手机号
                Intent intent = new Intent(activity, MainInfoPhone_.class);
                intent.putExtra("phone", phone);
                activity.startActivity(intent);
            }
        });
        phoneView.setText(str1 + " **** " + str2);
        Glide.with(activity).load(img).into(circleImageView);
        content.setText("吉电出行祝您换电无忧，一路畅行");
        authenticationView.setText(bean.getData().getId_auth_tip());
        if ("未实名认证".equals(authenticationView.getText().toString())) {
            authentication_panel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击实名认证
                    PermissionX.init(PersonalInfoActivity.this)
                            .permissions(Manifest.permission.CAMERA)
                            .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                            .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                            .request((allGranted, grantedList, deniedList) -> {
                                if (allGranted) {
                                    Intent intent = new Intent(activity, MainAuthentication_.class);
                                    intent.putExtra("ali_face_url", ali_face_url);
                                    intent.putExtra("ali_face_appcode", ali_face_appcode);
                                    activity.startActivity(intent);
                                } else {
                                    MyToast.showTheToast(PersonalInfoActivity.this, "当前应用缺少必要权限 ");
                                }
                            });
                }
            });
        } else {
            authentication_panel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击实名认证
                    PermissionX.init(PersonalInfoActivity.this)
                            .permissions(Manifest.permission.CAMERA)
                            .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                            .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                            .request((allGranted, grantedList, deniedList) -> {
                                if (allGranted) {
                                    Intent intent = new Intent(activity, MainAuthenticationIS_.class);
                                    intent.putExtra("id_card", bean.getData().getId_card());
                                    intent.putExtra("real_name", bean.getData().getReal_name());
                                    intent.putExtra("front_img", bean.getData().getFront_img());
                                    intent.putExtra("reverse_img", bean.getData().getReverse_img());
                                    activity.startActivity(intent);
                                } else {
                                    MyToast.showTheToast(PersonalInfoActivity.this, "当前应用缺少必要权限 ");
                                }
                            });
                }
            });
        }
    }

    @Override
    public void requestUserPersonalFail(String msg) {
        showMessage(msg);
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void onError(Throwable throwable) {
        showMessage("无网络链接，请检查您的网络设置！");
    }
}