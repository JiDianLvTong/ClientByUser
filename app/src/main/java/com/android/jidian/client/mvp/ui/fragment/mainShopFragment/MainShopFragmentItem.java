package com.android.jidian.client.mvp.ui.fragment.mainShopFragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.MainAuthentication_;
import com.android.jidian.client.Newshoppay_;
import com.android.jidian.client.R;
import com.android.jidian.client.ShopRentZhima_;
import com.android.jidian.client.base.BaseFragment;
import com.android.jidian.client.bean.LocalShopActivityBean;
import com.android.jidian.client.bean.ShopBuyBean;
import com.android.jidian.client.bean.ShopRentBean;
import com.android.jidian.client.bean.UserPersonalBean;
import com.android.jidian.client.mvp.contract.MainShopContract;
import com.android.jidian.client.mvp.presenter.MainShopPresenter;
import com.android.jidian.client.mvp.ui.adapter.MainShopBuyAdapter;
import com.android.jidian.client.mvp.ui.adapter.MainShopRentAdapter;
import com.android.jidian.client.util.UserInfoHelper;
import com.android.jidian.client.widgets.MyToast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainShopFragmentItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainShopFragmentItem extends BaseFragment<MainShopPresenter> implements MainShopContract.View {

    @BindView(R.id.srl_shop_item)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mullDataPanel)
    LinearLayout mMullDataPanel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mLng;
    private String mLat;
    private String mType;// 1=买  2=租赁

    private int local_is_skip;
    private String tip;
    private String id_auth_tip = "";

    private ArrayList<LocalShopActivityBean.Data> dataList = new ArrayList<>();
    private MainShopBuyAdapter mMainShopBuyAdapter;
    private MainShopRentAdapter mMainShopRentAdapter;

    public MainShopFragmentItem() {
        // Required empty public constructor
    }

    public static MainShopFragmentItem newInstance(String param1, String param2, String param3) {
        MainShopFragmentItem fragment = new MainShopFragmentItem();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLng = getArguments().getString(ARG_PARAM1);
            mLat = getArguments().getString(ARG_PARAM2);
            mType = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_shop_item;
    }

    @Override
    public void initView(View view) {
        mPresenter = new MainShopPresenter();
        mPresenter.attachView(this);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                requestData();
            }
        });
        mSmartRefreshLayout.setEnableLoadMore(false);
        requestData();
        if ("2".equals(mType)) {
            mPresenter.requestUserPersonal(UserInfoHelper.getInstance().getUid());
        }
        mMainShopBuyAdapter = new MainShopBuyAdapter();
        mMainShopRentAdapter = new MainShopRentAdapter();
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mMainShopBuyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                ShopBuyBean.DataBean.PacksBean bean =  (ShopBuyBean.DataBean.PacksBean)baseQuickAdapter.getData().get(i);
                String otype = bean.getOprice();
                String item_id = bean.getId();
                int is_buy = bean.getIs_buy();
                int is_sell = bean.getIs_sell();
                if (is_sell == -1) {
                    showMessage("如需购买商品，请联系商家扫描商家二维码购买~");
                } else if (is_sell == 1) {
                    if (local_is_skip == 0) {
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
                    } else if (local_is_skip == 1) {
                        //暂时不会返回这个
                    } else {
                        MyToast.showTheToast(requireActivity(), tip);
                    }
                } else {
                    showMessage("is_sell错误，必须是1和-1");
                }
            }
        });
        mMainShopRentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                ShopRentBean.DataBean.PacksBean bean =  (ShopRentBean.DataBean.PacksBean)baseQuickAdapter.getData().get(i);
                int is_sell = bean.getIs_sell();
                String id_ = bean.getId();
                if (is_sell == -1) {
                    MyToast.showTheToast(requireActivity(), "如需购买商品，请联系商家扫描商家二维码购买~");
                } else if (is_sell == 1) {
                    if (local_is_skip == 0) {
                        if (getActivity() != null) {
                            if ("已认证".equals(id_auth_tip)) {
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
                    } else if (local_is_skip == 1) {
                        //暂时不会返回1
                    } else {
                        MyToast.showTheToast(requireActivity(), tip);
                    }
                } else {
                    MyToast.showTheToast(requireActivity(), "is_sell错误，必须是1和-1");
                }
            }
        });
    }

    private void requestData() {
        if ("1".equals(mType)) {
            mPresenter.requestShopBuy(mLng,mLat);
        }else {
            mPresenter.requestShopRent(mLng,mLat);
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
    }

    @Override
    public void requestShopBuySuccess(ShopBuyBean bean) {
        mSmartRefreshLayout.finishRefresh();
        if (bean.getData() != null) {
            local_is_skip = bean.getIs_skip();
            tip = bean.getTips();
            if (bean.getData().getPacks() != null) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mMullDataPanel.setVisibility(View.GONE);
                mRecyclerView.setAdapter(mMainShopBuyAdapter);
                mMainShopBuyAdapter.setNewData(bean.getData().getPacks());
            }else {
                dataNull();
            }
        } else {
            dataNull();
        }
    }

    @Override
    public void requestShopRentSuccess(ShopRentBean bean) {
        if (bean.getData() != null) {
            local_is_skip = bean.getIs_skip();
            tip = bean.getTips();
            if (bean.getData().getPacks() != null) {
                mSmartRefreshLayout.finishRefresh();
                mRecyclerView.setVisibility(View.VISIBLE);
                mMullDataPanel.setVisibility(View.GONE);
                mRecyclerView.setAdapter(mMainShopRentAdapter);
                mMainShopRentAdapter.setNewData(bean.getData().getPacks());
            } else {
                dataNull();
            }
        } else {
            dataNull();
        }
    }

    @Override
    public void requestShopFail(String msg) {
        showMessage(msg);
    }

    @Override
    public void requestUserPersonalSuccess(UserPersonalBean bean) {
        if (bean.getData() != null) {
            id_auth_tip = bean.getData().getId_auth_tip();
        }
    }

    private void dataNull() {
        mRecyclerView.setVisibility(View.GONE);
        mMullDataPanel.setVisibility(View.VISIBLE);
    }
}