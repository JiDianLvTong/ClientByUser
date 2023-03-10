package com.android.jidian.client;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2017/6/11.
 */
@EActivity(R.layout.main_record_evaluate)
public class MainRecordEvaluate extends BaseActivity {
    @ViewById
    LinearLayout page_return, panel_1;
    @ViewById
    ImageView star_1, star_2, star_3, star_4, star_5;
    @ViewById
    TextView submit;
    @ViewById
    EditText my_text;

    private int star_count = 0;

    @AfterViews
    void afterViews() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //反馈页访问
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_FEEDBACK);
            }
        }, 500);
    }

    @Click({R.id.page_return, R.id.star_1, R.id.star_2, R.id.star_3, R.id.star_4, R.id.star_5, R.id.submit})
    void click(View v) {
        if (v.getId() == R.id.page_return) {
            this.finish();
        } else if (v.getId() == R.id.star_1) {
            setStar(1);
        } else if (v.getId() == R.id.star_2) {
            setStar(2);
        } else if (v.getId() == R.id.star_3) {
            setStar(3);
        } else if (v.getId() == R.id.star_4) {
            setStar(4);
        } else if (v.getId() == R.id.star_5) {
            setStar(5);
        } else if (v.getId() == R.id.submit) {
            //点击确定按钮
            BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_FEEDBACK_SURE);
            if (PubFunction.isConnect(activity, true)) {
                if (TextUtils.isEmpty(my_text.getText().toString().trim())) {
                    MyToast.showTheToast(activity, "提交信息不能为空！");
                } else {
                    HttpRecordEvaluate("8", star_count + "", my_text.getText().toString().trim());
                    progressDialog.show();
                }
            }
        }
    }

    private void setStar(int i) {
        star_1.setImageResource(R.drawable.xi1);
        star_2.setImageResource(R.drawable.xi1);
        star_3.setImageResource(R.drawable.xi1);
        star_4.setImageResource(R.drawable.xi1);
        star_5.setImageResource(R.drawable.xi1);
        if (i >= 1) {
            star_1.setImageResource(R.drawable.xi2);
        }
        if (i >= 2) {
            star_2.setImageResource(R.drawable.xi2);
        }
        if (i >= 3) {
            star_3.setImageResource(R.drawable.xi2);
        }
        if (i >= 4) {
            star_4.setImageResource(R.drawable.xi2);
        }
        if (i >= 5) {
            star_5.setImageResource(R.drawable.xi2);
        }
        star_count = i;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    /**
     * http接口：Service/getContact.html   获取电话信息
     */
    @Background
    void HttpRecordEvaluate(String id, String count, String content) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("tid", id));
        dataList.add(new ParamTypeData("stars", count));
        dataList.add(new ParamTypeData("content", content));
        new OkHttpConnect(activity, PubFunction.app + "Evaluate/addEvaluate.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpRecordEvaluate(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpRecordEvaluate(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
                System.out.println(jsonObject_response);
                if ("1".equals(status)) {
                    activity.finish();
                }
                MyToast.showTheToast(activity, msg);
            } catch (Exception e) {
                MyToast.showTheToast(activity, "JSON：" + e.toString());
            }
        }
    }
}