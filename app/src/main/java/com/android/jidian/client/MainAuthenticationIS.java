package com.android.jidian.client;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by hasee on 2017/6/6.
 */
@EActivity(R.layout.main_authentication_is)
public class MainAuthenticationIS extends BaseActivity {
    @ViewById
    TextView user_name, user_id, tv_title;
    @ViewById
    ImageView user_card_1_img, user_card_2_img;

    @AfterViews
    void afterViews() {
        tv_title.setText("实名认证");
        String user_id_str = getIntent().getStringExtra("id_card");
        String real_name_str = getIntent().getStringExtra("real_name");
        String front_img_str = getIntent().getStringExtra("front_img");
        String reverse_img_str = getIntent().getStringExtra("reverse_img");
        user_name.setText(real_name_str);
        user_id.setText(user_id_str);
        if (!front_img_str.isEmpty()) {
            Glide.with(activity).load(front_img_str).into(user_card_1_img);
        }
        if (!reverse_img_str.isEmpty()) {
            Glide.with(activity).load(reverse_img_str).into(user_card_2_img);
        }
    }

    @Click
    void iv_back() {
        this.finish();
    }
}