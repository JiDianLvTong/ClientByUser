package com.android.jidian.client.mvp.ui.activity.userInfo;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivity;
import com.android.jidian.client.mvp.ui.dialog.DialogByEnter;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalInfoPhoneActivity extends U6BaseActivity {

    @BindView(R.id.pageReturn)
    LinearLayout pageReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_personal_info_phone);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        DialogByEnter dialogByEnter = new DialogByEnter(activity, "您好，目前该功能正在维护，如果需要修改手机号，您可以联系商家，找商家协商换绑手机号，更换手机号后，员账号相关信息保持不变", new DialogByEnter.DialogChoiceListener() {
            @Override
            public void enterReturn() {
                activity.finish();
            }
        });
        dialogByEnter.showPopupWindow();
    }

    @OnClick(R.id.pageReturn)
    public void OnClickPagerReturn() {
        finish();
    }
}
