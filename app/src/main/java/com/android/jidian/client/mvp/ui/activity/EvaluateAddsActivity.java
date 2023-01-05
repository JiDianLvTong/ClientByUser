package com.android.jidian.client.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.base.BaseActivity2;
import com.android.jidian.client.R;
import com.android.jidian.client.bean.EvaluateAddsBean;
import com.android.jidian.client.bean.EvaluateLabsBean;
import com.android.jidian.client.mvp.contract.EvaluateAddsContract;
import com.android.jidian.client.widgets.FlowLayoutCenter;
import com.android.jidian.client.widgets.MyLinesEditView;
import com.android.jidian.client.mvp.presenter.EvaluateAddsPresenter;
import com.android.jidian.client.widgets.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EvaluateAddsActivity extends BaseActivity2<EvaluateAddsPresenter> implements EvaluateAddsContract.View {
    @BindView(R.id.page_return)
    LinearLayout pageReturn;
    @BindView(R.id.activity_evaluation_adds_textView1)
    TextView activityEvaluationAddsTextView1;
    @BindView(R.id.activity_evaluation_adds_imageView1)
    ImageView activityEvaluationAddsImageView1;
    @BindView(R.id.activity_evaluation_adds_imageView2)
    ImageView activityEvaluationAddsImageView2;
    @BindView(R.id.activity_evaluation_adds_imageView3)
    ImageView activityEvaluationAddsImageView3;
    @BindView(R.id.activity_evaluation_adds_imageView4)
    ImageView activityEvaluationAddsImageView4;
    @BindView(R.id.activity_evaluation_adds_imageView5)
    ImageView activityEvaluationAddsImageView5;
    @BindView(R.id.activity_evaluation_adds_flowLayoutCenter1)
    FlowLayoutCenter activityEvaluationAddsFlowLayoutCenter1;
    @BindView(R.id.activity_evaluation_adds_myLinesEditView1)
    MyLinesEditView activityEvaluationAddsMyLinesEditView1;
    @BindView(R.id.activity_evaluation_adds_textView2)
    TextView activityEvaluationAddsTextView2;
    @BindView(R.id.data_panelnew)
    LinearLayout dataPanelnew;
    @BindView(R.id.none_panelnew)
    LinearLayout nonePanelnew;
    @BindView(R.id.textView2)
    TextView textView2;

    private int cabid, stars = 5;
    private String name = "";//电柜名称
    private String repair = "";
    private List<Boolean> booleans = new ArrayList<>();//辅助记录labs选中
    private int booleansTrueSize = 0;//辅助记录booleans中true元素的size
    private List<EvaluateLabsBean.DataBean> dataBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        cabid = intent.getIntExtra("cabid", -1);
        name = intent.getStringExtra("name");
        repair = intent.getStringExtra("repair");
        setContentView(R.layout.activity_evaluate_adds);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mPresenter = new EvaluateAddsPresenter();
        mPresenter.attachView(this);
        mPresenter.evaluateLabs(cabid, repair);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityEvaluationAddsTextView1.setText(name);
    }

    @Override
    public void onSuccess1(EvaluateAddsBean bean) {
        showMessage(bean.getMsg());
        if (bean.getStatus() == 1) {
            finish();
        }
    }

    @Override
    public void onSuccess2(EvaluateLabsBean bean) {
        dataPanelnew.setVisibility(View.VISIBLE);
        nonePanelnew.setVisibility(View.GONE);
        if (bean.getStatus() == 1) {
            dataBeans = bean.getData();
            for (int i = 0; i < dataBeans.size(); i++) {
                booleans.add(false);//初始化标记为未选中
                EvaluateLabsBean.DataBean dataBean = dataBeans.get(i);
                TextView textView = (TextView) View.inflate(this, R.layout.text_item, null);
                textView.setText(dataBean.getName());
                int finalI = i;
                textView.setOnClickListener(v -> {
                    if (booleans.get(finalI)) {
                        booleans.set(finalI, false);
                        booleansTrueSize--;
                    } else {
                        if (booleansTrueSize >= 5) {
                            MyToast.showTheToast(this, "最多选择5个标签哦~");
                        } else {
                            booleans.set(finalI, true);
                            booleansTrueSize++;
                        }
                    }
                    if (booleans.get(finalI)) {
                        textView.setBackgroundResource(R.drawable.flowlayout_item_selector2);
                        textView.setTextColor(Color.parseColor("#EB5628"));
                    } else {
                        textView.setBackgroundResource(R.drawable.flowlayout_item_selector);
                        textView.setTextColor(Color.parseColor("#888888"));
                    }

                });
                activityEvaluationAddsFlowLayoutCenter1.addView(textView);
            }
        } else {
            showMessage(bean.getMsg());
        }
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
//        showMessage(throwable.getLocalizedMessage());
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected()) {//onNetDisconnected
                dataPanelnew.setVisibility(View.GONE);
                nonePanelnew.setVisibility(View.VISIBLE);
                textView2.setOnClickListener(v -> mPresenter.evaluateLabs(cabid, repair));
            }
        }
    }

    @OnClick({R.id.page_return, R.id.activity_evaluation_adds_imageView1, R.id.activity_evaluation_adds_imageView2, R.id.activity_evaluation_adds_imageView3, R.id.activity_evaluation_adds_imageView4, R.id.activity_evaluation_adds_imageView5, R.id.activity_evaluation_adds_textView2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.page_return:
                finish();
                break;
            case R.id.activity_evaluation_adds_imageView1:
                receiveStars(1);
                break;
            case R.id.activity_evaluation_adds_imageView2:
                receiveStars(2);
                break;
            case R.id.activity_evaluation_adds_imageView3:
                receiveStars(3);
                break;
            case R.id.activity_evaluation_adds_imageView4:
                receiveStars(4);
                break;
            case R.id.activity_evaluation_adds_imageView5:
                receiveStars(5);
                break;
            case R.id.activity_evaluation_adds_textView2:
                String content = activityEvaluationAddsMyLinesEditView1.getContentText();
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < booleans.size(); i++) {
                    if (booleans.get(i)) {
                        builder.append(dataBeans.get(i).getId());
                        builder.append(",");
                    }
                }
                if (builder.length() > 0) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                String labids = builder.toString();
                mPresenter.evaluateAdds(cabid, stars, labids, content, repair);
                break;
        }
    }

    private void receiveStars(int stars) {
        this.stars = stars;
        zeroStars();
        switch (stars) {
            case 5:
                activityEvaluationAddsImageView5.setImageResource(R.mipmap.yellow_stars);
            case 4:
                activityEvaluationAddsImageView4.setImageResource(R.mipmap.yellow_stars);
            case 3:
                activityEvaluationAddsImageView3.setImageResource(R.mipmap.yellow_stars);
            case 2:
                activityEvaluationAddsImageView2.setImageResource(R.mipmap.yellow_stars);
            default:
                activityEvaluationAddsImageView1.setImageResource(R.mipmap.yellow_stars);
                break;
        }
    }

    private void zeroStars() {
        activityEvaluationAddsImageView1.setImageResource(R.mipmap.gray_stars);
        activityEvaluationAddsImageView2.setImageResource(R.mipmap.gray_stars);
        activityEvaluationAddsImageView3.setImageResource(R.mipmap.gray_stars);
        activityEvaluationAddsImageView4.setImageResource(R.mipmap.gray_stars);
        activityEvaluationAddsImageView5.setImageResource(R.mipmap.gray_stars);
    }
}
