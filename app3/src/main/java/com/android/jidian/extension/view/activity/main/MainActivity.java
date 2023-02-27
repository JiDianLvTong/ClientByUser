package com.android.jidian.extension.view.activity.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.jidian.extension.R;
import com.android.jidian.extension.base.BaseActivity;
import com.android.jidian.extension.base.permissionManager.PermissionManager;
import com.android.jidian.extension.bean.LoginGetUserInfoBean;
import com.android.jidian.extension.bean.MainGetUserInfoBean;
import com.android.jidian.extension.dao.sp.UserInfoSp;
import com.android.jidian.extension.net.BaseHttp;
import com.android.jidian.extension.net.BaseHttpParameterFormat;
import com.android.jidian.extension.net.HttpUrlMap;
import com.android.jidian.extension.util.Public;
import com.android.jidian.extension.view.activity.login.LoginActivity;
import com.android.jidian.extension.view.activity.main.extensionFragment.ExtensionFragment;
import com.android.jidian.extension.view.activity.main.profitFragment.ProfitFragment;
import com.android.jidian.extension.view.activity.user.PayInfoActivity;
import com.android.jidian.extension.view.activity.user.PhoneActivity;
import com.android.jidian.extension.view.commonPlug.dialog.DialogByChoice;
import com.android.jidian.extension.view.commonPlug.dialog.DialogByEnter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawerView)
    public DrawerLayout drawerView;

    @BindView(R.id.slideView)
    public LinearLayout slideView;
    @BindView(R.id.slideUserNameView)
    public TextView slideUserNameView;
    @BindView(R.id.slideP1)
    public LinearLayout slideP1;
    @BindView(R.id.logoutView)
    public TextView logoutView;

    @BindView(R.id.mainView)
    public LinearLayout mainView;
    @BindView(R.id.userPanel)
    public LinearLayout userPanel;

    @BindView(R.id.mainPanel)
    public LinearLayout mainPanel;
    @BindView(R.id.userNameView)
    public TextView userNameView;

    @BindViews({R.id.t_1, R.id.t_2})
    public List<TextView> footTextViewList;

    @BindViews({R.id.i_1, R.id.i_2})
    public List<ImageView> footImageViewList;
    private int[] isSelectIcons = new int[]{R.drawable.activity_main_extension_select_is, R.drawable.activity_main_profit_select_is};
    private int[] notSelectIcons = new int[]{R.drawable.activity_main_extension_select_not, R.drawable.activity_main_profit_select_not};
    private int localPage = -1;

    private ExtensionFragment extensionFragment;
    private ProfitFragment profitFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        getUserInfo();
    }

    private void initView(){
        //页卡设置
        changeMain(0);
        setFragmentRefresh();
        checkAcc();
    }

    public void setFragmentRefresh(){
        userNameView.setText(UserInfoSp.getInstance().getUserInfoData(UserInfoSp.UserInfoEnum.realname));
        slideUserNameView.setText(UserInfoSp.getInstance().getUserInfoData(UserInfoSp.UserInfoEnum.realname));
    }

    public void checkAcc(){
        List<BaseHttpParameterFormat> baseHttpParameterFormats = new ArrayList<>();
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("mid",UserInfoSp.getInstance().getId()));
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("apptoken",UserInfoSp.getInstance().getAppToken()));
        BaseHttp baseHttp = new BaseHttp(activity, HttpUrlMap.checkAccToken, baseHttpParameterFormats, new BaseHttp.BaseHttpListener() {
            @Override
            public void dataReturn(int code, String errorMessage , String message , String data) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(code == 1){

                        }else{
                            new DialogByEnter(activity, message, new DialogByEnter.DialogChoiceListener() {
                                @Override
                                public void enterReturn() {
                                    activity.startActivity(new Intent(activity , LoginActivity.class));
                                    UserInfoSp.getInstance().cleanUserInfoData();
                                    activity.finish();
                                }
                            }).showPopupWindow();
                        }
                    }
                });
            }
        });
        baseHttp.onStart();
    }

    public void getUserInfo(){
        List<BaseHttpParameterFormat> baseHttpParameterFormats = new ArrayList<>();
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("uid",UserInfoSp.getInstance().getId()));
        BaseHttp baseHttp = new BaseHttp(activity, HttpUrlMap.getUserInfo, baseHttpParameterFormats, new BaseHttp.BaseHttpListener() {
            @Override
            public void dataReturn(int code, String errorMessage , String message , String data) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(code == 1){
                            MainGetUserInfoBean mainGetUserInfoBean = new Gson().fromJson(data , MainGetUserInfoBean.class);
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.uid , mainGetUserInfoBean.getId());
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.apptoken , mainGetUserInfoBean.getApptoken());
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.realname , mainGetUserInfoBean.getRealname());
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.phone , mainGetUserInfoBean.getPhone());
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.qrcode , mainGetUserInfoBean.getQrcode());
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.uptoken , mainGetUserInfoBean.getUptoken());
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.astatus , mainGetUserInfoBean.getAstatus());
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.idcard , mainGetUserInfoBean.getIdcard());
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.zfb , mainGetUserInfoBean.getZfb());
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.astater , mainGetUserInfoBean.getAstater());
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.ucardzurl , mainGetUserInfoBean.getUcardzurl());
                            UserInfoSp.getInstance().setUserInfoData(UserInfoSp.UserInfoEnum.ucardfurl , mainGetUserInfoBean.getUcardfurl());
                        }else{
                            new DialogByEnter(activity,message).showPopupWindow();
                        }
                    }
                });
            }
        });
        baseHttp.onStart();
    }

    @OnClick({R.id.p_1, R.id.p_2})
    public void onClickFoot(LinearLayout layout) {
        if (layout.getId() == R.id.p_1) {
            changeMain(0);
        } else if (layout.getId() == R.id.p_2) {
            changeMain(1);
        }
    }

    @OnClick(R.id.userPanel)
    public void onClickUserPanel(){
        drawerView.openDrawer(slideView);
    }

    @OnClick(R.id.slideP1)
    public void onClickSlideP1(){
        activity.startActivity(new Intent(activity , PayInfoActivity.class));
    }
    @OnClick(R.id.slideP2)
    public void onClickSlideP2(){
        activity.startActivity(new Intent(activity , PhoneActivity.class));
    }


    @OnClick(R.id.logoutView)
    public void onClickLogoutView(){
        DialogByChoice dialog = new DialogByChoice(activity, "是否要退出登录", new DialogByChoice.DialogChoiceListener() {
            @Override
            public void enterReturn() {
                activity.startActivity(new Intent(activity , LoginActivity.class));
                UserInfoSp.getInstance().cleanUserInfoData();
                activity.finish();
            }
            @Override
            public void cancelReturn() {
            }
        });
        dialog.showPopupWindow();
    }


    private void changeMain(int page) {
        if (page == localPage) {
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (extensionFragment != null) {
            ft.hide(extensionFragment);
        }
        if (profitFragment != null) {
            ft.hide(profitFragment);
        }
        for (int i = 0; i < footTextViewList.size(); i++) {
            footTextViewList.get(i).setTextColor(0xff999999);
            footImageViewList.get(i).setImageResource(notSelectIcons[i]);
        }
        footTextViewList.get(page).setTextColor(activity.getResources().getColor(R.color.white_ffffff));
        footImageViewList.get(page).setImageResource(isSelectIcons[page]);
        if (page == 0) {
            if (extensionFragment == null) {
                extensionFragment = new ExtensionFragment(this);
                ft.add(R.id.mainPanel, extensionFragment);
            } else {
                ft.show(extensionFragment);
                extensionFragment.setFragmentRefresh();
            }
        } else if (page == 1) {
            if (profitFragment == null) {
                profitFragment = new ProfitFragment();
                ft.add(R.id.mainPanel, profitFragment);
            } else {
                ft.show(profitFragment);
                profitFragment.setFragmentRefresh();
            }
        }
        ft.commitAllowingStateLoss();
        localPage = page;
    }

}
