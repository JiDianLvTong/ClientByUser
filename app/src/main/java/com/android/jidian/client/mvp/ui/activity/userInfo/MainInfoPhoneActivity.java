package com.android.jidian.client.mvp.ui.activity.userInfo;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainInfoPhoneActivity extends U6BaseActivity {


    @BindView(R.id.pageReturn)
    LinearLayout pageReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_persional_info_phone);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){

    }

    @OnClick(R.id.pageReturn)
    public void OnClickPagerReturn() {
        finish();
    }
}
