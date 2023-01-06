package com.android.jidian.client.mvp.ui.activity.main.mainShopFragment;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.MainAuthentication_;
import com.android.jidian.client.Newshoppay_;
import com.android.jidian.client.R;
import com.android.jidian.client.ShopRentZhima_;
import com.android.jidian.client.base.BaseFragment;
import com.android.jidian.client.bean.ShopBuyBean;
import com.android.jidian.client.bean.ShopRentBean;
import com.android.jidian.client.bean.UserPersonalBean;
import com.android.jidian.client.mvp.contract.MainShopContract;
import com.android.jidian.client.mvp.presenter.MainShopPresenter;
import com.android.jidian.client.util.UserInfoHelper;
import com.android.jidian.client.widgets.MyToast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;

public class MainShopFragment extends BaseFragment<MainShopPresenter> implements MainShopContract.View{

    @BindView(R.id.smartRefreshLayout)
    public SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.mullDataPanel)
    public LinearLayout mullDataPanel;

    private ArrayList<MainShopBean> arrayList = new ArrayList<>();
    private MainShopAdapter mainShopAdapter;

    //获取地理位置
    private String mLng = "";
    private String mLat = "";

    //获取用户信息 - 是否实名认证
    private String idAuthTip = "";

    @Override
    public int getLayoutId() {
        return R.layout.u6_activity_main_fragment_shop;
    }

    @Override
    public void initView(View view) {
        //mvp初始化
        mPresenter = new MainShopPresenter();
        mPresenter.attachView(this);
        //下拉刷新
        MaterialHeader materialHeader = new MaterialHeader(getActivity());
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"),Color.parseColor("#D7A64A"));
        smartRefreshLayout.setRefreshHeader(materialHeader);
        smartRefreshLayout.setEnableHeaderTranslationContent(true);
        smartRefreshLayout.setOnRefreshListener(new com.scwang.smart.refresh.layout.listener.OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull com.scwang.smart.refresh.layout.api.RefreshLayout refreshLayout) {
                if (!UserInfoHelper.getInstance().getUid().isEmpty()) {
                    requestData();
                } else {
                    smartRefreshLayout.finishRefresh();
                }

            }
        });
        smartRefreshLayout.setEnableLoadMore(false);
        //适配器
        mainShopAdapter = new MainShopAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mainShopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                MainShopBean mainShopBean =  (MainShopBean) baseQuickAdapter.getData().get(i);
                String type = mainShopBean.getMainShopDataType();
                if(type.equals("buy")){
                    ShopBuyBean.DataBean.PacksBean bean = (ShopBuyBean.DataBean.PacksBean)mainShopBean.getObjectBean();
                    String otype = bean.getOprice();
                    String item_id = bean.getId();
                    int is_buy = bean.getIs_buy();
                    int is_sell = bean.getIs_sell();
                    if (is_sell == -1) {
                        showMessage("如需购买商品，请联系商家扫描商家二维码购买~");
                    } else if (is_sell == 1) {
                        if ("230".equals(otype) || "231".equals(otype) || "232".equals(otype)) {
                            if (is_buy == 1) {
                                Intent intent = new Intent(getActivity(), Newshoppay_.class);
                                intent.putExtra("id", item_id);
                                intent.putExtra("from", "product");
                                getActivity().startActivity(intent);
                            } else {
                                showMessage("每个用户年卡过期前，仅限购买同类型年卡~");
                            }
                        } else {
                            Intent intent = new Intent(getActivity(), Newshoppay_.class);
                            intent.putExtra("id", item_id);
                            intent.putExtra("from", "product");
                            getActivity().startActivity(intent);
                        }
                    } else {
                        showMessage("is_sell错误，必须是1和-1");
                    }
                }else if(type.equals("rent")){
                    ShopRentBean.DataBean.PacksBean bean =  (ShopRentBean.DataBean.PacksBean)mainShopBean.getObjectBean();
                    int is_sell = bean.getIs_sell();
                    String id_ = bean.getId();
                    if (is_sell == -1) {
                        showMessage("如需租赁商品，请联系商家扫描商家二维码租赁~");
                    } else if (is_sell == 1) {
                        if (getActivity() != null) {
                            if ("已认证".equals(idAuthTip)) {
                                Intent intent = new Intent(getActivity(), ShopRentZhima_.class);
                                intent.putExtra("id", id_);
                                intent.putExtra("from", "product");
                                getActivity().startActivity(intent);
                            } else {
                                LayoutInflater inflater = LayoutInflater.from(getActivity());
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                final AlertDialog mAlertDialog = builder.create();
                                View view1 = inflater.inflate(R.layout.alertdialog_is_auth, null);
                                TextView success_t = (TextView) view1.findViewById(R.id.success);
                                success_t.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getActivity().startActivity(new Intent(getActivity(), MainAuthentication_.class));
                                    }
                                });
                                TextView error_t = (TextView) view1.findViewById(R.id.error);
                                error_t.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mAlertDialog.dismiss();
                                    }
                                });

                                mAlertDialog.show();
                                mAlertDialog.getWindow().setContentView(view1);
                            }
                        }
                    } else {
                        MyToast.showTheToast(requireActivity(), "is_sell错误，必须是1和-1");
                    }
                }
            }
        });
        recyclerView.setAdapter(mainShopAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        //获取数据
        requestData();
    }

    private void requestData() {
        arrayList.clear();
        mPresenter.requestShopBuy(mLng,mLat);
        mPresenter.requestShopRent(mLng,mLat);
        mPresenter.requestUserPersonal(UserInfoHelper.getInstance().getUid());
    }

    //设置坐标
    public void setFragmentPosition(String lng, String lat) {
        this.mLng = lng;
        this.mLat = lat;
    }

    @Override
    public void requestShopBuySuccess(ShopBuyBean bean) {
        smartRefreshLayout.finishRefresh();
        if (bean.getData() != null) {
            if (bean.getData().getPacks() != null) {
                for(int i = 0 ; i < bean.getData().getPacks().size() ; i++){
                    arrayList.add(new MainShopBean("buy",bean.getData().getPacks().get(i)));
                }
                ArrayList<MainShopBean> tempArrayList = new ArrayList<>();
                for(int i = 0 ; i < arrayList.size() ; i++){
                    if(arrayList.get(i).getMainShopDataType().equals("rent")){
                        tempArrayList.add(arrayList.get(i));
                    }
                }
                for(int i = 0 ; i < arrayList.size() ; i++){
                    if(arrayList.get(i).getMainShopDataType().equals("buy")){
                        tempArrayList.add(arrayList.get(i));
                    }
                }
                arrayList = tempArrayList;
                recyclerView.setVisibility(View.VISIBLE);
                mullDataPanel.setVisibility(View.GONE);
                mainShopAdapter.setNewData(arrayList);
            } else {
            }
        }
        dataNull();
    }

    @Override
    public void requestShopRentSuccess(ShopRentBean bean) {
        smartRefreshLayout.finishRefresh();
        if (bean.getData() != null) {
            if (bean.getData().getPacks() != null) {
                for(int i = 0 ; i < bean.getData().getPacks().size() ; i++){
                    arrayList.add(new MainShopBean("rent",bean.getData().getPacks().get(i)));
                }
                ArrayList<MainShopBean> tempArrayList = new ArrayList<>();
                for(int i = 0 ; i < arrayList.size() ; i++){
                    if(arrayList.get(i).getMainShopDataType().equals("rent")){
                        tempArrayList.add(arrayList.get(i));
                    }
                }
                for(int i = 0 ; i < arrayList.size() ; i++){
                    if(arrayList.get(i).getMainShopDataType().equals("buy")){
                        tempArrayList.add(arrayList.get(i));
                    }
                }
                arrayList = tempArrayList;
                recyclerView.setVisibility(View.VISIBLE);
                mullDataPanel.setVisibility(View.GONE);
                mainShopAdapter.setNewData(arrayList);
            } else {
            }
        }
        dataNull();
    }

    @Override
    public void requestUserPersonalSuccess(UserPersonalBean bean) {
        if (bean.getData() != null) {
            idAuthTip = bean.getData().getId_auth_tip();
        }
    }


    @Override
    public void requestShopFail(String msg) {
        smartRefreshLayout.finishRefresh();
        showMessage(msg);
    }

    private void dataNull() {
        if(arrayList.size() == 0){
            recyclerView.setVisibility(View.GONE);
            mullDataPanel.setVisibility(View.VISIBLE);
        }else{
            mullDataPanel.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
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
        showMessage("无网络链接，请检查您的网络设置！");
//        shSwipeRefreshLayout.finishRefresh();
    }

    @Override
    public void onDestroy() {
        arrayList.clear();
        super.onDestroy();
    }

}
