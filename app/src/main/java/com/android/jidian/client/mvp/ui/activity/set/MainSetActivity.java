package com.android.jidian.client.mvp.ui.activity.set;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivityByMvp;
import com.android.jidian.client.bean.BaseBean;
import com.android.jidian.client.mvp.contract.MainSetContract;
import com.android.jidian.client.mvp.presenter.MainSetPresenter;
import com.android.jidian.client.mvp.ui.activity.h5.MainAbout;
import com.android.jidian.client.mvp.ui.activity.h5.MainAgreement;
import com.android.jidian.client.mvp.ui.activity.h5.MainPrivacyClause;
import com.android.jidian.client.mvp.ui.activity.main.MainActivityEvent;
import com.android.jidian.client.mvp.ui.dialog.DialogByChoice;
import com.android.jidian.client.mvp.ui.dialog.SplashAgainAgreeDialog;
import com.android.jidian.client.mvp.ui.dialog.DialogByLogoff;
import com.android.jidian.client.pub.PubFunction;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author : xiaoming
 * date: 2023/1/6 09:52
 * company: 兴达智联
 * description:
 */
public class MainSetActivity extends U6BaseActivityByMvp<MainSetPresenter> implements MainSetContract.View {

    @BindView(R.id.pageReturn)
    LinearLayout pageReturn;
    @BindView(R.id.about)
    LinearLayout About;
    @BindView(R.id.now_ver)
    TextView now_ver;
    @BindView(R.id.agreement)
    LinearLayout agreement;
    @BindView(R.id.privacy_clause)
    LinearLayout privacy_clause;
    @BindView(R.id.cancellation)
    LinearLayout cancellation;
    @BindView(R.id.logout)
    TextView logout;

    private String mMsgCode;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_main_set);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mPresenter = new MainSetPresenter();
        mPresenter.attachView(this);
        now_ver.setText("V " + PubFunction.getLocalVersionName(MainSetActivity.this));

    }

    @OnClick(R.id.about)
    public void OnClickAbout() {
        startActivity(new Intent(MainSetActivity.this, MainAbout.class));
    }

    @OnClick(R.id.agreement)
    public void OnClickAgreement() {
        startActivity(new Intent(MainSetActivity.this, MainAgreement.class));
    }

    @OnClick(R.id.privacy_clause)
    public void OnClickPrivacyClause() {
        startActivity(new Intent(MainSetActivity.this, MainPrivacyClause.class));
    }

    @OnClick(R.id.pageReturn)
    public void onClickPageReturn(){
        activity.finish();
    }

    @OnClick(R.id.cancellation)
    public void OnClickCancellation() {
        new DialogByLogoff(activity, new DialogByLogoff.DialogChoiceListener() {
            @Override
            public void enterReturn(String code) {
                if (mPresenter != null) {
                    mMsgCode = code;
                    mPresenter.requestUserCancel(uid,code,null);
                }
            }

            @Override
            public void cancelReturn() {

            }

            @Override
            public void verificationCodeClick() {
                if (mPresenter != null) {
                    mPresenter.requestUserSmsCancel();
                }
            }
        }).showPopupWindow();
    }

    @OnClick(R.id.logout)
    public void OnClickLogout() {
        new DialogByChoice(activity, "确定要退出登录吗？", new DialogByChoice.DialogChoiceListener() {
            @Override
            public void enterReturn() {
                if (mPresenter != null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_avatar", "");
                    editor.clear();
                    editor.commit();
                    EventBus.getDefault().post(new MainActivityEvent(MainActivityEvent.CHANGEMAIN, 0));
                    activity.finish();
                }
            }
            @Override
            public void cancelReturn() {

            }
        }).showPopupWindow();
    }

    @Override
    public void requestUserCancelSuccess(BaseBean bean) {
        if (2 == bean.status) {
            if (bean.msg.indexOf("如果确定注销将丢失") != -1 || bean.msg.indexOf("您确定注销") != -1) {
                new DialogByChoice(activity, bean.msg, new DialogByChoice.DialogChoiceListener() {
                    @Override
                    public void enterReturn() {
                        if (mPresenter != null) {
                            mPresenter.requestUserCancel(uid,mMsgCode,"1");
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_avatar", "");
                            editor.clear();
                            editor.commit();
                            activity.finish();
                        }
                    }

                    @Override
                    public void cancelReturn() {

                    }
                }).showPopupWindow();
            }
        }
    }

    @Override
    public void requestUserCancelFail(String msg) {
        showMessage(msg);
    }

    @Override
    public void requestUserSmsCancel(BaseBean bean) {
        showMessage(bean.msg);
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
