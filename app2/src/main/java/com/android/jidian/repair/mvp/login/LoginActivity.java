package com.android.jidian.repair.mvp.login;


import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.ActivityCollector;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.dao.sp.UserInfoSp;
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
    @BindView(R.id.pageReturn)
    public LinearLayout pageReturn;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
        if (UserInfoSp.getInstance().isLogin()) {
            startActivity(new Intent(activity, MainActivity.class));
            LoginActivity.this.finish();
        }
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

        UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.id ,  bean.getId());
        UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.dwrole ,  bean.getDwrole());
        UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.apptoken ,  bean.getApptoken());
        UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.adname ,  bean.getAdname());
        UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.phone ,  bean.getPhone());
        UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.is_dwfirst ,  bean.getIs_dwfirst());
        UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.avater ,  bean.getAvater());

        apptoken = bean.getApptoken();
        ActivityCollector.finishAll();
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

    @OnClick(R.id.pageReturn)
    void onClickPageReturn(){
        this.finish();
    }

}