package com.android.jidian.client;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jidian.client.bean.ContactV2Bean;
import com.android.jidian.client.net.RetrofitClient;
import com.android.jidian.client.net.RxScheduler;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.util.BuryingPointManager;
import com.permissionx.guolindev.PermissionX;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Modified by fight on 2020/06/26.
 */
@EActivity(R.layout.main_customer)
public class MainCustomer extends BaseActivity {
    @ViewById
    LinearLayout  phone_panel, phone_panel1;
    @ViewById
    TextView phone_num, phone_num1;

    private Disposable disposable;

    @AfterViews
    void afterViews() {
        disposable = RetrofitClient.getInstance().getApiService().getContactV2()
                .compose(RxScheduler.Flo_io_main())
                .subscribe(this::onSuccess, throwable -> {
                            progressDialog.dismiss();
                            throwable.printStackTrace(System.err);
                        }
                );
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //客服页访问
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_CUSTOMER_SERVICE);
            }
        }, 500);
    }

    private void onSuccess(ContactV2Bean contactV2Bean) {
        if (contactV2Bean.getStatus() == 1) {
            List<ContactV2Bean.DataBean> dataBeans = contactV2Bean.getData();
            ContactV2Bean.DataBean dataBean0 = dataBeans.get(0);
            phone_panel.setVisibility(View.GONE);
            phone_panel1.setVisibility(View.GONE);
            if (dataBean0 != null && dataBean0.getIs_show() == 1) {
                phone_panel.setVisibility(View.VISIBLE);
                phone_num.setText(dataBean0.getName() + "：" + dataBean0.getContact());
                phone_panel.setOnClickListener(v -> callPhone(dataBean0.get_contact()));
            }
            ContactV2Bean.DataBean dataBean1 = dataBeans.get(1);
            if (dataBean1 != null && dataBean1.getIs_show() == 1) {
                phone_panel1.setVisibility(View.VISIBLE);
                phone_num1.setText(dataBean1.getName() + "：" + dataBean1.getContact());
                phone_panel1.setOnClickListener(v -> callPhone(dataBean1.get_contact()));
            }
        } else {
            MyToast.showTheToast(this, contactV2Bean.getMsg());
        }
        progressDialog.dismiss();
    }

    @SuppressLint("MissingPermission")
    private void callPhone(String phoneNumber) {
        PermissionX.init(this)
                .permissions(Manifest.permission.CALL_PHONE)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        if (!TextUtils.isEmpty(phoneNumber)) {
                            if (phoneNumber.contains("010")) {
                                //点击拨打010客服电话
                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_CUSTOMER_SERVICE_010);
                            } else if (phoneNumber.contains("400")) {
                                //点击拨打400客服电话
                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_CUSTOMER_SERVICE_400);
                            }
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(this, "您拒绝了如下权限： " + deniedList, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Click
    void page_return() {
        this.finish();
    }

    @Click
    void to_c() {
        //点击反馈按钮
        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_CUSTOMER_SERVICE_FEEDBACK);
        Intent intent = new Intent(activity, MainRecordEvaluate_.class);
        intent.putExtra("id", uid);
        activity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}