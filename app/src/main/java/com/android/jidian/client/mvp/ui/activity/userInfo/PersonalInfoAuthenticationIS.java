package com.android.jidian.client.mvp.ui.activity.userInfo;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivity;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalInfoAuthenticationIS extends U6BaseActivity {

    @BindView(R.id.userNameView)
    public TextView userNameView;
    @BindView(R.id.userIdView)
    public TextView userIdView;
    @BindView(R.id.userCardFrontImgView)
    public ImageView userCardFrontImgView;
    @BindView(R.id.userCardBackImgView)
    public ImageView userCardBackImgView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_personal_info_authentication_is);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        String user_id_str = getIntent().getStringExtra("id_card");
        String real_name_str = getIntent().getStringExtra("real_name");
        String front_img_str = getIntent().getStringExtra("front_img");
        String reverse_img_str = getIntent().getStringExtra("reverse_img");

        userNameView.setText(real_name_str);
        userIdView.setText(user_id_str);
        if (!front_img_str.isEmpty()) {
            Glide.with(activity).load(front_img_str).into(userCardFrontImgView);
        }
        if (!reverse_img_str.isEmpty()) {
            Glide.with(activity).load(reverse_img_str).into(userCardBackImgView);
        }
    }

    @OnClick(R.id.pageReturn)
    void onClickPageReturn(){
        activity.finish();
    }

}
