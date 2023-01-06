package com.android.jidian.client.mvp.ui.activity.message;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jidian.client.BaseActivity;
import com.android.jidian.client.MainAdv;
import com.android.jidian.client.R;
import com.android.jidian.client.bean.AdvicesLists2Bean;
import com.android.jidian.client.util.ViewUtil;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 活动详情 activity
 */
public class AdvicesShowActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_msg_detail_title)
    TextView tvMsgDetailTitle;
    @BindView(R.id.tv_msg_detail_time)
    TextView tvMsgDetailTime;
    @BindView(R.id.iv_msg_detail_content)
    ImageView ivImgContent;
    @BindView(R.id.tv_msg_detail_content)
    TextView tvContent;

    private AdvicesLists2Bean.DataBean dataBean;
    public static final String DATABEANBUNDLE = "dataBeanBundle";
    public static final String DATABEAN = "dataBean";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u6_activity_advices_detail);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            Bundle args = getIntent().getBundleExtra(DATABEANBUNDLE);
            if (args != null) {
                dataBean = (AdvicesLists2Bean.DataBean) args.getSerializable(DATABEAN);
            }
        }

        if (dataBean != null) {
            if ("2".equals(dataBean.getType())) {
                //活动消息
                tvTitle.setText("活动消息");
                SpannableString ss = new SpannableString(dataBean.getTitle() + "   ");
                int len = ss.length();
                int expire = dataBean.getExpire();
                Drawable d;
                if (expire == 0) {
                    d = ContextCompat.getDrawable(this, R.drawable.image032501);
                } else {
                    d = ContextCompat.getDrawable(this, R.drawable.image032502);
                }
                if (d != null) {
                    d.setBounds(0, 0, ViewUtil.dp2px(this, 32), ViewUtil.dp2px(this, 18));
                    ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                    ss.setSpan(span, len - 1, len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    tvMsgDetailTitle.setText(ss);
                }
            } else {
                //系统消息
                tvTitle.setText("系统消息");
                tvMsgDetailTitle.setText(dataBean.getTitle());
            }

            tvMsgDetailTime.setText(dataBean.getCreate_time());

            if ("2".equals(dataBean.getShow_type())) {
                //图片弹窗
                if (TextUtils.isEmpty(dataBean.getDetail_images())) {
                    ivImgContent.setImageResource(R.color.white);
                } else {
                    Glide.with(this).load(dataBean.getDetail_images()).into(ivImgContent);
                }
            } else {
                //文字弹窗
                tvContent.setText(dataBean.getContent());
            }
        } else {
            finish();
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_msg_detail_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_msg_detail_content:
                if ("1".equals(dataBean.getIs_jump())) {
                    String url = dataBean.getUrl();
                    Intent intent = new Intent(activity, MainAdv.class);
                    intent.putExtra("url", url);
                    activity.startActivity(intent);
                }
                break;
            default:
                break;
        }
    }
}