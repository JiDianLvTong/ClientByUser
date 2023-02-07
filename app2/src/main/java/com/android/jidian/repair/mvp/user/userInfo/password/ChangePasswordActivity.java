package com.android.jidian.repair.mvp.user.userInfo.password;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.dao.sp.UserInfoSp;
import com.android.jidian.repair.mvp.login.LoginActivity;
import com.android.jidian.repair.utils.Md5;

public class ChangePasswordActivity extends BaseActivityByMvp<ChangePasswordPresenter> implements ChangePasswordContract.View {


    @Override
    public int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    public void initView() {
        mPresenter = new ChangePasswordPresenter();
        mPresenter.attachView(this);
        ((TextView) findViewById(R.id.phoneText)).setText(UserInfoSp.getInstance().getPhone());
        findViewById(R.id.pageReturn).setOnClickListener(v -> {
            finish();
        });

        EditText newpsdEdit = findViewById(R.id.newpsdEdit);
        EditText codeEdit = findViewById(R.id.codeEdit);

        //点击获取验证码
        findViewById(R.id.getcode).setOnClickListener(v -> {
            if (mPresenter != null) {
                mPresenter.requestVerificationCode(UserInfoSp.getInstance().getPhone());
            }
        });

        //点击确认更换
        findViewById(R.id.submitText).setOnClickListener(v -> {
            if (TextUtils.isEmpty(newpsdEdit.getText().toString().trim())) {
                showMessage("请输入新的登录密码");
                return;
            }
            if (newpsdEdit.getText().toString().trim().length() < 8) {
                showMessage("最少输入8位字符");
                return;
            }
            if (newpsdEdit.getText().toString().trim().length() > 30) {
                showMessage("最多输入30位字符");
                return;
            }
            if (TextUtils.isEmpty(codeEdit.getText().toString().trim())) {
                showMessage("请输入验证码");
                return;
            }

            //密码8-30位，以字母开头且包含数字、特殊字（~!@#%&）


            if (mPresenter != null) {
                mPresenter.requestResetLoginPwd(UserInfoSp.getInstance().getPhone(), Md5.stringToMD5(newpsdEdit.getText().toString().trim()), codeEdit.getText().toString().trim());
            }
        });
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

    @Override
    public void requestResetLoginPwdSuccess(BaseBean bean) {
        if (bean != null) {
            showMessage(bean.getMsg());
            UserInfoSp.getInstance().cleanUserInfoData();
            Intent intentMain = new Intent(ChangePasswordActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentMain);
        }
    }

    @Override
    public void requestShowTips(String msg) {
        showMessage(msg);
    }
}