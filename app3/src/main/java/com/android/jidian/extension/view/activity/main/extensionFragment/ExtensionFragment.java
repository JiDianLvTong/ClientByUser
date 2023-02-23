package com.android.jidian.extension.view.activity.main.extensionFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.jidian.extension.R;
import com.android.jidian.extension.base.BaseFragment;
import com.android.jidian.extension.bean.LoginGetUserInfoBean;
import com.android.jidian.extension.bean.MainGetUserInfoBean;
import com.android.jidian.extension.dao.sp.UserInfoSp;
import com.android.jidian.extension.net.BaseHttp;
import com.android.jidian.extension.net.BaseHttpParameterFormat;
import com.android.jidian.extension.net.HttpUrlMap;
import com.android.jidian.extension.util.Public;
import com.android.jidian.extension.view.activity.main.MainActivity;
import com.android.jidian.extension.view.activity.main.profitFragment.ProfitFragment;
import com.android.jidian.extension.view.commonPlug.dialog.DialogByEnter;
import com.google.gson.Gson;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ExtensionFragment extends BaseFragment {

    @BindView(R.id.qrCodeView)
    public ImageView qrCodeView;
    @BindView(R.id.refreshView)
    public SmartRefreshLayout refreshView;

    private MainActivity mainActivity;

    private Unbinder unbinder;

    public ExtensionFragment(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_fragment_extension,container,false);
        unbinder = ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init(){
        //下拉刷新
        MaterialHeader materialHeader = new MaterialHeader(requireActivity());
        materialHeader.setColorSchemeColors(Color.parseColor("#b69873"), Color.parseColor("#b69873"));
        refreshView.setRefreshHeader(materialHeader);
        refreshView.setEnableHeaderTranslationContent(true);
        refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getUserInfo();
            }
        });
        refreshView.setEnableAutoLoadMore(false);
        refreshView.autoRefresh();
    }

    private void setView(){
        mainActivity.setFragmentRefresh();
        qrCodeView.setImageBitmap(Public.generateBitmap(UserInfoSp.getInstance().getUserInfoData(UserInfoSp.UserInfoEnum.qrcode), 400, 400 , 0xff000000 , 0xffffffff));
    }


    private void getUserInfo(){
        List<BaseHttpParameterFormat> baseHttpParameterFormats = new ArrayList<>();
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("uid",UserInfoSp.getInstance().getId()));
        BaseHttp baseHttp = new BaseHttp(getContext(), HttpUrlMap.getUserInfo, baseHttpParameterFormats, new BaseHttp.BaseHttpListener() {
            @Override
            public void dataReturn(int code, String errorMessage , String message , String data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshView.finishRefresh();
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
                            setView();
                        }else{
                            new DialogByEnter(getActivity(),message).showPopupWindow();
                        }
                    }
                });
            }
        });
        baseHttp.onStart();
    }


    public void setFragmentRefresh(){

    }

}
