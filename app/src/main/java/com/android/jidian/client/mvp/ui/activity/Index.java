package com.android.jidian.client.mvp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.widget.ImageView;

import com.android.jidian.client.BaseActivity;
import com.android.jidian.client.Main_;
import com.android.jidian.client.R;
import com.android.jidian.client.mvp.ui.dialog.SplashAgainAgreeDialog;
import com.android.jidian.client.mvp.ui.dialog.SplashAgreeDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.permissionx.guolindev.PermissionX;

public class Index extends BaseActivity {
    private final static int DELAY_MILLIS = 900;
//    protected String[] needPermissions = {
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.CAMERA
//    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImmersionBar.with(this).fitsSystemWindows(false).statusBarColor(R.color.color_F9F9F9).statusBarDarkFont(false).init();

        ImageView ivIndex = findViewById(R.id.iv_index);
        ivIndex.setImageResource(R.drawable.index_logo);

        String isFirstAgree = spNotClear.getString("isFirstSplashAgree", "");
        if ("1".equals(isFirstAgree)) {
            setPermission();
        } else {
            showSplashAgree();
        }
    }

    /**
     * 弹起隐私保护提示
     */
    private void showSplashAgree() {
        new SplashAgreeDialog().init(this, getResources().getString(R.string.str_splash_agree), "不同意", "同意", new SplashAgreeDialog.OnDismissListener() {
            @Override
            public void onSureDismiss() {
                setFirstAgree();
            }

            @Override
            public void onCancelDismiss() {
                showSplashAgainAgree();
            }
        }).setPosition(Gravity.CENTER).setWidth(1).setOutCancel(false).setKeyBackCancel(false).show(getSupportFragmentManager());
    }

    /**
     * 再次弹起隐私保护提示
     */
    private void showSplashAgainAgree() {
        new SplashAgainAgreeDialog().init(getResources().getString(R.string.str_splash_again_agree), "退出应用", "再次查看", new SplashAgainAgreeDialog.OnDismissListener() {
            @Override
            public void onSureDismiss() {
                showSplashAgree();
            }

            @Override
            public void onCancelDismiss() {
                finish();
                System.exit(0);
            }
        }).setPosition(Gravity.CENTER).setWidth(1).setOutCancel(false).setKeyBackCancel(false).show(getSupportFragmentManager());

    }

    private void setPermission() {
        PermissionX.init(Index.this)
                .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                .request((allGranted, grantedList, deniedList) -> {
//                    if (allGranted) {
                        startActivity();
//                    } else {
//                        MyToast.showTheToast(this, "当前应用缺少必要权限 ");
//                    }
                });
    }

    /**
     * 通过判断跳转不同页面
     */
    private void startActivity() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(Index.this, Main_.class));
            finish();
        }, DELAY_MILLIS);
    }

    private void setFirstAgree() {
        SharedPreferences.Editor editor = spNotClear.edit();
        editor.putString("isFirstSplashAgree", "1");
        editor.apply();
        setPermission();
    }
}