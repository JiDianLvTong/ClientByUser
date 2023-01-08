package com.android.jidian.client.mvp.ui.activity.userInfo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.jidian.client.R;
import com.android.jidian.client.base.PermissionManager.PermissionManager;
import com.android.jidian.client.base.U6BaseActivityByMvp;
import com.android.jidian.client.bean.UserPersonalBean;
import com.android.jidian.client.mvp.contract.PersonalInfoContract;
import com.android.jidian.client.mvp.presenter.PersonalInfoPresenter;
import com.android.jidian.client.util.UserInfoHelper;
import com.android.jidian.client.widgets.MyToast;
import com.bumptech.glide.Glide;
import com.permissionx.guolindev.PermissionX;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.List;

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
    @BindView(R.id.change_phone)
    LinearLayout change_phone;
    @BindView(R.id.phoneView)
    TextView phoneView;
    @BindView(R.id.authentication_panel)
    LinearLayout authentication_panel;
    @BindView(R.id.authenticationView)
    TextView authenticationView;
    @BindView(R.id.smartRefreshLayout)
    public SmartRefreshLayout smartRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_personal_info);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mPresenter = new PersonalInfoPresenter();
        mPresenter.attachView(this);
        MaterialHeader materialHeader = new MaterialHeader(activity);
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"),Color.parseColor("#D7A64A"));
        smartRefreshLayout.setRefreshHeader(materialHeader);
        smartRefreshLayout.setEnableHeaderTranslationContent(true);
        smartRefreshLayout.setOnRefreshListener(new com.scwang.smart.refresh.layout.listener.OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull com.scwang.smart.refresh.layout.api.RefreshLayout refreshLayout) {
                if (!UserInfoHelper.getInstance().getUid().isEmpty()) {
                    mPresenter.requestUserPersonal(uid);
                } else {
                    smartRefreshLayout.finishRefresh();
                }

            }
        });
        smartRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.requestUserPersonal(uid);
    }

    @OnClick(R.id.pageReturn)
    public void OnClickPagerReturn() {
        finish();
    }

    @Override
    public void requestUserPersonalSuccess(UserPersonalBean bean) {
        smartRefreshLayout.finishRefresh();
        String img = bean.getData().getAvater();
        String phone = bean.getData().getPhone();
        String str1 = phone.substring(0, 3);
        String str2 = phone.substring(7, 11);
        String ali_face_url = bean.getData().getAli_face_url();
        String ali_face_appcode = bean.getData().getAli_face_appcode();
        change_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击手机号
                Intent intent = new Intent(activity, PersonalInfoPhoneActivity.class);
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
                    Intent intent = new Intent(activity, PersonalInfoAuthentication.class);
                    intent.putExtra("ali_face_url", ali_face_url);
                    intent.putExtra("ali_face_appcode", ali_face_appcode);
                    activity.startActivity(intent);
                }
            });
        } else {
            authentication_panel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, PersonalInfoAuthenticationIS.class);
                    intent.putExtra("id_card", bean.getData().getId_card());
                    intent.putExtra("real_name", bean.getData().getReal_name());
                    intent.putExtra("front_img", bean.getData().getFront_img());
                    intent.putExtra("reverse_img", bean.getData().getReverse_img());
                    activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public void requestUserPersonalFail(String msg) {
        smartRefreshLayout.finishRefresh();
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