package com.android.jidian.repair.mvp.user.userInfo;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.ActivityCollector;
import com.android.jidian.repair.base.BaseActivity;
import com.android.jidian.repair.dao.sp.UserInfoSp;
import com.android.jidian.repair.mvp.login.LoginActivity;
import com.android.jidian.repair.mvp.user.userInfo.password.ChangePasswordActivity;
import com.android.jidian.repair.widgets.dialog.DialogByChoice;

import butterknife.BindView;
import butterknife.OnClick;

public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.userName)
    public TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_info);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        userName.setText(UserInfoSp.getInstance().getName());
    }

    @OnClick(R.id.pageReturn)
    public void onClickPageReturn(){
        this.finish();
    }
    @OnClick(R.id.changePasswordPanel)
    public void onClickChangePasswordPanel(){
        Intent intent = new Intent(activity, ChangePasswordActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.isAuthPanel)
    public void onClickIsAuthPanel(){

    }
    @OnClick(R.id.logoutView)
    public void onClickLogoutView(){
        DialogByChoice dialog = new DialogByChoice(activity, "确定要退出登录？", new DialogByChoice.DialogChoiceListener() {
            @Override
            public void enterReturn() {
                UserInfoSp.getInstance().cleanUserInfoData();
                Intent intent = new Intent(activity, LoginActivity.class);
                //下面2个flags ,可以将原有任务栈清空
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }

            @Override
            public void cancelReturn() {

            }
        });
        dialog.showPopupWindow();
    }
}
