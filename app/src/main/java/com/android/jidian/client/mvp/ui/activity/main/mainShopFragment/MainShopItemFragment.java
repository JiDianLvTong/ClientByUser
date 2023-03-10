package com.android.jidian.client.mvp.ui.activity.main.mainShopFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.jidian.client.R;
import com.android.jidian.client.base.BaseFragment;
import com.android.jidian.client.bean.ShopBuyBean;
import com.android.jidian.client.bean.ShopRentBean;
import com.android.jidian.client.bean.UserPersonalBean;
import com.android.jidian.client.mvp.contract.MainShopContract;
import com.android.jidian.client.mvp.presenter.MainFindPresenter;
import com.android.jidian.client.mvp.presenter.MainShopPresenter;
import com.android.jidian.client.mvp.ui.activity.pay.PayByCreateOrderActivity;
import com.android.jidian.client.mvp.ui.activity.pay.PayByCreateOrderZhiMaActivity;
import com.android.jidian.client.mvp.ui.activity.userInfo.PersonalInfoAuthentication;
import com.android.jidian.client.mvp.ui.dialog.DialogByChoiceType2;
import com.android.jidian.client.util.UserInfoHelper;
import com.android.jidian.client.widgets.MyToast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

public class MainShopItemFragment extends BaseFragment<MainShopPresenter> implements MainShopContract.View {


    @BindView(R.id.smartRefreshLayout)
    public SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.mullDataPanel)
    public LinearLayout mullDataPanel;

    private int type = -1;

    private ArrayList<MainShopBean> arrayList = new ArrayList<>();
    private MainShopItemAdapter mainShopAdapter;

    //??????????????????
    private String mLng = "";
    private String mLat = "";

    //?????????????????? - ??????????????????
    private String idAuthTip = "";

    public MainShopItemFragment(int type, String mLng, String mLat) {
        this.type = type;
        this.mLng = mLng;
        this.mLat = mLat;
    }

    @Override
    public int getLayoutId() {
        return R.layout.u6_activity_main_fragment_shop_item;
    }

    //????????????
    public void setFragmentRefresh() {
        if (mPresenter == null) {
            mPresenter = new MainShopPresenter();
            mPresenter.attachView(this);
        }
        requestData();
    }

    @Override
    public void initView(View view) {

        //mvp?????????
        mPresenter = new MainShopPresenter();
        mPresenter.attachView(this);

        //????????????
        MaterialHeader materialHeader = new MaterialHeader(getActivity());
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"), Color.parseColor("#D7A64A"));
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

        //?????????
        mainShopAdapter = new MainShopItemAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainShopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (baseQuickAdapter.getData().size() == 0) {
                    return;
                }
                MainShopBean mainShopBean = (MainShopBean) baseQuickAdapter.getData().get(i);
                String type = mainShopBean.getMainShopDataType();
                if (type.equals("buy")) {
                    ShopBuyBean.DataBean.PacksBean bean = (ShopBuyBean.DataBean.PacksBean) mainShopBean.getObjectBean();
                    String otype = bean.getOprice();
                    String item_id = bean.getId();
                    int is_buy = bean.getIs_buy();
                    int is_sell = bean.getIs_sell();
                    if (is_sell == -1) {
                        showMessage("???????????????????????????????????????????????????????????????~");
                    } else if (is_sell == 1) {
                        if ("230".equals(otype) || "231".equals(otype) || "232".equals(otype)) {
                            if (is_buy == 1) {
                                Intent intent = new Intent(getActivity(), PayByCreateOrderActivity.class);
                                intent.putExtra("id", item_id);
                                intent.putExtra("from", "product");
                                getActivity().startActivity(intent);
                            } else {
                                showMessage("?????????????????????????????????????????????????????????~");
                            }
                        } else {
                            Intent intent = new Intent(getActivity(), PayByCreateOrderActivity.class);
                            intent.putExtra("id", item_id);
                            intent.putExtra("from", "product");
                            getActivity().startActivity(intent);
                        }
                    } else {
                        showMessage("is_sell??????????????????1???-1");
                    }
                } else if (type.equals("rent")) {
                    ShopRentBean.DataBean.PacksBean bean = (ShopRentBean.DataBean.PacksBean) mainShopBean.getObjectBean();
                    int is_sell = bean.getIs_sell();
                    String id_ = bean.getId();
                    if (is_sell == -1) {
                        showMessage("???????????????????????????????????????????????????????????????~");
                    } else if (is_sell == 1) {
                        if (getActivity() != null) {
                            if ("?????????".equals(idAuthTip)) {
                                Intent intent = new Intent(getActivity(), PayByCreateOrderZhiMaActivity.class);
                                intent.putExtra("id", id_);
                                intent.putExtra("from", "product");
                                getActivity().startActivity(intent);
                            } else {
                                new DialogByChoiceType2(getActivity(), "????????????????????????", "????????????????????????", new DialogByChoiceType2.DialogChoiceListener() {
                                    @Override
                                    public void enterReturn() {
                                        getActivity().startActivity(new Intent(getActivity(), PersonalInfoAuthentication.class));
                                    }

                                    @Override
                                    public void cancelReturn() {

                                    }
                                }).showPopupWindow();
                            }
                        }
                    } else {
                        MyToast.showTheToast(requireActivity(), "is_sell??????????????????1???-1");
                    }
                }
            }
        });
        recyclerView.setAdapter(mainShopAdapter);
        //????????????
        showProgress();
        requestData();
    }

    private void requestData() {
        if (UserInfoHelper.getInstance().getUid().isEmpty()) {
            return;
        }
        mPresenter.requestUserPersonal(UserInfoHelper.getInstance().getUid());
    }


    @Override
    public void showProgress() {
        dialogByLoading.show();
    }

    @Override
    public void hideProgress() {
        dialogByLoading.dismiss();
    }

    @Override
    public void onError(Throwable throwable) {
        showMessage("????????????????????????????????????????????????");
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void requestShopBuySuccess(ShopBuyBean bean) {
        hideProgress();
        smartRefreshLayout.finishRefresh();
        if (bean.getData() != null) {
            if (bean.getData().getPacks() != null) {
                for (int i = 0; i < bean.getData().getPacks().size(); i++) {
                    arrayList.add(new MainShopBean("buy", bean.getData().getPacks().get(i)));
                }
                ArrayList<MainShopBean> tempArrayList = new ArrayList<>();
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getMainShopDataType().equals("rent")) {
                        tempArrayList.add(arrayList.get(i));
                    }
                }
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getMainShopDataType().equals("buy")) {
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
        hideProgress();
        smartRefreshLayout.finishRefresh();
        if (bean.getData() != null) {
            if (bean.getData().getPacks() != null) {
                for (int i = 0; i < bean.getData().getPacks().size(); i++) {
                    arrayList.add(new MainShopBean("rent", bean.getData().getPacks().get(i)));
                }
                ArrayList<MainShopBean> tempArrayList = new ArrayList<>();
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getMainShopDataType().equals("rent")) {
                        tempArrayList.add(arrayList.get(i));
                    }
                }
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getMainShopDataType().equals("buy")) {
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

    private void dataNull() {
        if (arrayList.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            mullDataPanel.setVisibility(View.VISIBLE);
        } else {
            mullDataPanel.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void requestShopFail(String msg) {
        hideProgress();
        smartRefreshLayout.finishRefresh();
        showMessage(msg);
    }

    @Override
    public void requestUserPersonalSuccess(UserPersonalBean bean) {
        hideProgress();
        if (bean.getData() != null) {
            idAuthTip = bean.getData().getId_auth_tip();
            arrayList.clear();
            if (type == 1) {
                mPresenter.requestShopBuy(mLng, mLat);
            } else if (type == 2) {
                mPresenter.requestShopRent(mLng, mLat);
            }
        }
    }

    //????????????
    public void setFragmentPosition(String lng, String lat) {
        this.mLng = lng;
        this.mLat = lat;
    }

    @Override
    public void onDestroy() {
        arrayList.clear();
        super.onDestroy();
    }
}
