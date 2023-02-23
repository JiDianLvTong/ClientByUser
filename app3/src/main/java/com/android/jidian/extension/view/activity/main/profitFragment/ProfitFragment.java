package com.android.jidian.extension.view.activity.main.profitFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.jidian.extension.R;
import com.android.jidian.extension.base.BaseFragment;
import com.android.jidian.extension.bean.MainGetProfitInfoBean;
import com.android.jidian.extension.bean.MainGetUserInfoBean;
import com.android.jidian.extension.dao.sp.UserInfoSp;
import com.android.jidian.extension.net.BaseHttp;
import com.android.jidian.extension.net.BaseHttpParameterFormat;
import com.android.jidian.extension.net.HttpUrlMap;
import com.android.jidian.extension.view.commonPlug.dialog.DialogByEnter;
import com.android.jidian.extension.view.commonPlug.xLinearLayoutManager.XLinearLayoutManager;
import com.google.gson.Gson;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfitFragment extends BaseFragment {

    @BindView(R.id.nullDataPanel)
    public LinearLayout nullDataPanel;
    @BindView(R.id.refreshView)
    public SmartRefreshLayout refreshView;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.t_1)
    public TextView t_1;
    @BindView(R.id.t_2)
    public TextView t_2;


    private ProfitAdapter profitAdapter;
    private ArrayList<MainGetProfitInfoBean.Lists> dataList = new ArrayList<>();
    private int currentPage = 1;
    private Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_fragment_profit,container,false);
        unbinder = ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init(){
        //下拉刷新
        MaterialHeader materialHeader = new MaterialHeader(requireActivity());
        materialHeader.setColorSchemeColors(Color.parseColor("#b69873"), Color.parseColor("#b69873"));
        refreshView.setRefreshHeader(materialHeader);
        ClassicsFooter classicsFooter = new ClassicsFooter(requireActivity());
        classicsFooter.setAccentColor(Color.parseColor("#ffffff"));
        refreshView.setRefreshFooter(classicsFooter);
        refreshView.setEnableHeaderTranslationContent(true);
        refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentPage = 1;
                dataList.clear();
                getData();
            }
        });
        refreshView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                currentPage = currentPage + 1;
                getData();
            }
        });
        refreshView.autoRefresh();
    }

    private void getData(){

        List<BaseHttpParameterFormat> baseHttpParameterFormats = new ArrayList<>();
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("uid", UserInfoSp.getInstance().getId()));
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("page", currentPage+ ""));
        BaseHttp baseHttp = new BaseHttp(getContext(), HttpUrlMap.getProfitList, baseHttpParameterFormats, new BaseHttp.BaseHttpListener() {
            @Override
            public void dataReturn(int code, String errorMessage , String message , String data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshView.finishRefresh();
                        refreshView.finishLoadMore();
                        if(code == 1){
                            nullDataPanel.setVisibility(View.GONE);
                            MainGetProfitInfoBean mainGetProfitInfoBean = new Gson().fromJson(data , MainGetProfitInfoBean.class);
                            t_1.setText(mainGetProfitInfoBean.getMontht()+"元");
                            t_2.setText(mainGetProfitInfoBean.getIncomet()+"元");
                            ArrayList<MainGetProfitInfoBean.Lists> tempArrayList = mainGetProfitInfoBean.getLists();
                            for(int i = 0 ; i < tempArrayList.size() ; i++){
                                dataList.add(tempArrayList.get(i));
                            }
                            if(profitAdapter == null){
                                recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
                                XLinearLayoutManager linearLayoutManager = new XLinearLayoutManager(getActivity());
                                linearLayoutManager.setOrientation(XLinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                profitAdapter = new ProfitAdapter(getContext(),dataList);
                                recyclerView.setAdapter(profitAdapter);
                            }else{
                                profitAdapter.notifyDataSetChanged();
                            }

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
        getData();
    }


}
