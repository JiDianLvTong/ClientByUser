package com.android.jidian.client.mvp.ui.fragment.mainShopFragment;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.MainAuthentication_;
import com.android.jidian.client.MainShopFromCabinet;
import com.android.jidian.client.MainShopItem_2FromCabinet_;
import com.android.jidian.client.MainShopItem_3FromCabinet_;
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
import com.android.jidian.client.util.BuryingPointManager;
import com.android.jidian.client.util.UserInfoHelper;
import com.android.jidian.client.widgets.MyFragmentPagerAdapter;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.widgets.ViewPagerAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainShopFragment extends BaseFragment<MainShopPresenter> implements MainShopContract.View, ViewPager.OnPageChangeListener {

//    private Unbinder unbinder;

    //    @BindView(R.id.recyclerView)
//    public RecyclerView recyclerView;
//    @BindView(R.id.mullDataPanel)
//    public LinearLayout mullDataPanel;
//    @BindView(R.id.shSwipeRefreshLayout)
//    public SmartRefreshLayout shSwipeRefreshLayout;
    @BindView(R.id.tl_main_shop)
    SlidingTabLayout tl_main_shop;
    @BindView(R.id.view_pager)
    public ViewPager view_pager;

    //    private BaseHttp baseHttp;
    private ArrayList<LocalShopActivityBean.Data> dataList = new ArrayList<>();
    private MainShopBuyAdapter mMainShopBuyAdapter;
    private MainShopRentAdapter mMainShopRentAdapter;
    //存放Fragment
    private ArrayList<Fragment> fragmentList;
    //管理Fragment
    private FragmentManager fragmentManager;

    private String lng = "", lat = "";
    private int mCurrentPage = 1;
    private int local_is_skip;
    private String tip;
    private String id_auth_tip = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_main_fragment_shop;
    }

    @Override
    public void initView(View view) {
        mPresenter = new MainShopPresenter();
        mPresenter.attachView(this);
//        shSwipeRefreshLayout.setEnableLoadMore(false);
//        mMainShopBuyAdapter = new MainShopBuyAdapter();
//        mMainShopRentAdapter = new MainShopRentAdapter();
//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        recyclerView.setAdapter(mMainShopBuyAdapter);
//        mMainShopBuyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                ShopBuyBean.DataBean.PacksBean bean = (ShopBuyBean.DataBean.PacksBean) baseQuickAdapter.getData().get(i);
//                String otype = bean.getOprice();
//                String item_id = bean.getId();
//                int is_buy = bean.getIs_buy();
//                int is_sell = bean.getIs_sell();
//                if (is_sell == -1) {
//                    showMessage("如需购买商品，请联系商家扫描商家二维码购买~");
//                } else if (is_sell == 1) {
//                    if (local_is_skip == 0) {
//                        if ("230".equals(otype) || "231".equals(otype) || "232".equals(otype)) {
//                            if (is_buy == 1) {
//                                Intent intent = new Intent(getActivity(), Newshoppay_.class);
//                                intent.putExtra("id", item_id);
//                                intent.putExtra("from", "product");
//                                getActivity().startActivity(intent);
//                            } else {
//                                showMessage("每个用户年卡过期前，仅限购买同类型年卡~");
//                            }
//                        } else {
//                            Intent intent = new Intent(getActivity(), Newshoppay_.class);
//                            intent.putExtra("id", item_id);
//                            intent.putExtra("from", "product");
//                            getActivity().startActivity(intent);
//                        }
//                    } else if (local_is_skip == 1) {
//                        //暂时不会返回这个
//                    } else {
//                        MyToast.showTheToast(requireActivity(), tip);
//                    }
//                } else {
//                    showMessage("is_sell错误，必须是1和-1");
//                }
//            }
//        });
//        mMainShopRentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                ShopRentBean.DataBean.PacksBean bean = (ShopRentBean.DataBean.PacksBean) baseQuickAdapter.getData().get(i);
//                int is_sell = bean.getIs_sell();
//                String id_ = bean.getId();
//                if (is_sell == -1) {
//                    MyToast.showTheToast(requireActivity(), "如需购买商品，请联系商家扫描商家二维码购买~");
//                } else if (is_sell == 1) {
//                    if (local_is_skip == 0) {
//                        if (getActivity() != null) {
//                            if ("已认证".equals(id_auth_tip)) {
//                                Intent intent = new Intent(getActivity(), ShopRentZhima_.class);
//                                intent.putExtra("id", id_);
//                                intent.putExtra("from", "product");
//                                getActivity().startActivity(intent);
//                            } else {
//                                LayoutInflater inflater = LayoutInflater.from(getActivity());
//                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                final AlertDialog mAlertDialog = builder.create();
//                                View view1 = inflater.inflate(R.layout.alertdialog_is_auth, null);
//                                TextView success_t = (TextView) view1.findViewById(R.id.success);
//                                success_t.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        getActivity().startActivity(new Intent(getActivity(), MainAuthentication_.class));
//                                    }
//                                });
//                                TextView error_t = (TextView) view1.findViewById(R.id.error);
//                                error_t.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        mAlertDialog.dismiss();
//                                    }
//                                });
//
//                                mAlertDialog.show();
//                                mAlertDialog.getWindow().setContentView(view1);
//                            }
//                        }
//                    } else if (local_is_skip == 1) {
//                        //暂时不会返回1
//                    } else {
//                        MyToast.showTheToast(requireActivity(), tip);
//                    }
//                } else {
//                    MyToast.showTheToast(requireActivity(), "is_sell错误，必须是1和-1");
//                }
//            }
//        });
//        shSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                requestData();
//            }
//        });
//        shSwipeRefreshLayout.setEnableLoadMore(false);
        initViewPager();
//        requestData();
    }

    private void initViewPager() {
        view_pager.setAdapter(new MyFragmentPagerAdapter(fragmentManager, fragmentList));
        //让ViewPager缓存2个页面
        view_pager.setOffscreenPageLimit(1);
        //设置默认打开第一页
        view_pager.setCurrentItem(0);
        //设置viewpager页面滑动监听事件
        view_pager.addOnPageChangeListener(this);

        fragmentList = new ArrayList<>();
        MainShopFragmentItem item = MainShopFragmentItem.newInstance(lng, lat, "1");
        fragmentList.add(item);

        MainShopFragmentItem item1 = MainShopFragmentItem.newInstance(lng, lat, "2");
        fragmentList.add(item1);
        fragmentManager = getActivity().getSupportFragmentManager();
        String[] s = new String[]{"购买","租赁"};
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), fragmentList, s);
        view_pager.setAdapter(adapter);
        tl_main_shop.setViewPager(view_pager);
    }

    /**
     * 刷新界面数据
     */
    public void setFragmentPosition(int currentPage, String lng, String lat) {
        this.mCurrentPage = currentPage;
        this.lng = lng;
        this.lat = lat;
//        shSwipeRefreshLayout.autoRefresh();
//        requestData();
    }

    private void requestData() {
//        if (mPresenter != null) {
//            //1=购买 2=租赁
//            if (1 == mCurrentPage) {
//                tv_buy_text.setTextColor(Color.parseColor("#2ba245"));
//                tv_rent_text.setTextColor(Color.parseColor("#cccccc"));
//                mPresenter.requestShopBuy(lng, lat);
//            } else {
//                tv_buy_text.setTextColor(Color.parseColor("#cccccc"));
//                tv_rent_text.setTextColor(Color.parseColor("#2ba245"));
//                mPresenter.requestShopRent(lng, lat);
//            }
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (!UserInfoHelper.getInstance().getUid().isEmpty()) {
//            shSwipeRefreshLayout.autoRefresh();
//        }
    }

//    @OnClick(R.id.tv_buy_text)
//    public void onClickTvBuyText() {
//        mCurrentPage = 1;
//        requestData();
//        tv_buy_text.setTextColor(Color.parseColor("#FFFFFF"));
//        tv_rent_text.setTextColor(Color.parseColor("#666666"));

//    }

//    @OnClick(R.id.tv_rent_text)
//    public void onClickTvRentyText() {
//        mCurrentPage = 2;
//        requestData();
////        tv_buy_text.setTextColor(Color.parseColor("#666666"));
////        tv_rent_text.setTextColor(Color.parseColor("#FFFFFF"));
//    }

    @Override
    public void requestShopBuySuccess(ShopBuyBean bean) {
//        if (bean.getData() != null) {
//            local_is_skip = bean.getIs_skip();
//            tip = bean.getTips();
//            if (bean.getData().getPacks() != null) {
//                shSwipeRefreshLayout.finishRefresh();
//                recyclerView.setVisibility(View.VISIBLE);
//                mullDataPanel.setVisibility(View.GONE);
//                recyclerView.setAdapter(mMainShopBuyAdapter);
//                mMainShopBuyAdapter.setNewData(bean.getData().getPacks());
//            } else {
//                dataNull();
//            }
//        } else {
//            dataNull();
//        }
    }

    @Override
    public void requestShopRentSuccess(ShopRentBean bean) {
//        if (bean.getData() != null) {
//            local_is_skip = bean.getIs_skip();
//            tip = bean.getTips();
//            if (bean.getData().getPacks() != null) {
//                shSwipeRefreshLayout.finishRefresh();
//                recyclerView.setVisibility(View.VISIBLE);
//                mullDataPanel.setVisibility(View.GONE);
//                recyclerView.setAdapter(mMainShopRentAdapter);
//                mMainShopRentAdapter.setNewData(bean.getData().getPacks());
//            } else {
//                dataNull();
//            }
//        } else {
//            dataNull();
//        }
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
        super.onDestroy();
        dataList.clear();
//        recyclerView.setAdapter(null);
//        if (unbinder != null) {
//            unbinder.unbind();
//        }
    }

//    /**
//     * 列表数据为空逻辑处理
//     */
//    private void dataNull() {
//        recyclerView.setVisibility(View.GONE);
//        mullDataPanel.setVisibility(View.VISIBLE);
//    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
//        view_pager.setCurrentItem(i);
//        Animation animation;
//        for (int i = 0; i < textViews.length; i++) {
//            if (position == i) {
//                animation = new TranslateAnimation(now_position, positions[i], 0, 0);
//                now_position = positions[i];
//                textViews[i].setTextColor(colors[0]);
//                //当前页卡编号
//                animation.setFillAfter(true);// true:图片停在动画结束位置
//                animation.setDuration(300);
//                cursor.startAnimation(animation);
//            } else {
//                textViews[i].setTextColor(colors[1]);
//            }
//        }
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
