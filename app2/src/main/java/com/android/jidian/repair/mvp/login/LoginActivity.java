package com.android.jidian.repair.mvp.login;


import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.ActivityCollector;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.mvp.main.MainActivity;
import com.android.jidian.repair.utils.Md5;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivityByMvp<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.edit_login_number)
    public EditText editLoginNumber;
    @BindView(R.id.edit_login_pwd)
    public EditText editLoginPwd;
    @BindView(R.id.btn_login_go)
    public TextView btnLoginGo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);

    }

    @OnClick(R.id.btn_login_go)
    public void onClickBtnLogin() {
        String phone = editLoginNumber.getText().toString().trim();
        String pwd = editLoginPwd.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            showMessage("请输入手机号");
        }
        if (TextUtils.isEmpty(pwd)) {
            showMessage("请输入密码");
        }
        if (mPresenter != null) {
            String md5Pwd = Md5.stringToMD5(pwd);
            mPresenter.requestLoginJdLogin(phone, md5Pwd);
        }
    }

    @Override
    public void requestLoginJdLoginSuccess(LoginBean.DataBean bean) {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", bean.getId());
        editor.putString("dwrole", bean.getDwrole());
        editor.putString("apptoken", bean.getApptoken());
        editor.putString("adname", bean.getAdname());
        editor.putString("phone", bean.getPhone());
        editor.putString("is_dwfirst", bean.getIs_dwfirst());
        editor.putString("avater", bean.getAvater());
        editor.apply();
        apptoken = bean.getApptoken();
        ActivityCollector.finishAll();
        int[] i = new int[]{1, 1, 1, 1, 1};
        int iiii = i[9];
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void requestShowTips(String msg) {
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