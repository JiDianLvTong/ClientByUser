package com.android.jidian.client.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.MainShop_;
import com.android.jidian.client.R;
import com.android.jidian.client.bean.ScanCodeEventBean;
import com.android.jidian.client.mvp.ui.activity.ScanCodeNewActivity;
import com.android.jidian.client.mvp.ui.adapter.MyEbikeBty_recyclerView_Adapter;
import com.android.jidian.client.bean.MyEbikeBtyBean;
import com.android.jidian.client.bean.UserConfirmBean;
import com.android.jidian.client.mvp.contract.MyEbikeBtyContract;
import com.android.jidian.client.mvp.presenter.MyEbikeBtyPresenter;
import com.android.jidian.client.base.BaseFragment;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class MyEbikeBtyFragment extends BaseFragment<MyEbikeBtyPresenter> implements MyEbikeBtyContract.View {
    @BindView(R.id.go_shoping)
    TextView goShoping;
    @BindView(R.id.no_device)
    LinearLayout noDevice;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.wallte_device_empty)
    LinearLayout wallteDeviceEmpty;
    @BindView(R.id.data_panel)
    LinearLayout datapanel;
    Unbinder unbinder;
    @BindView(R.id.wallte_empyt_img)
    ImageView wallteEmpytImg;
    @BindView(R.id.no_content_text)
    TextView noContentText;
    private String cha_id = "";
    private SharedPreferences sharedPreferences;
    private String uid;
    public static Handler myEbikeReloadHandler;
    private Handler scanHandler;
    private ReloadListener reloadListener;

    private boolean isRefresh = false;
    private MyEbikeBty_recyclerView_Adapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.myebikebtyfragment;
    }

    @SuppressLint("HandlerLeak")
    private void handler() {
        myEbikeReloadHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mPresenter.myEbikeBty(uid);
            }
        };
        scanHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mPresenter.myEbikeBty(uid);
            }
        };
    }

    @Override
    public void initView(View view) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        handler();
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        uid = sharedPreferences.getString("id", "");
        mPresenter = new MyEbikeBtyPresenter();
        mPresenter.attachView(this);
        mPresenter.myEbikeBty(uid);
        refresh.setEnableLoadMore(false);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                mPresenter.myEbikeBty(uid);
                reloadListener.onEbikeReload(1);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //钱包-绑定页访问
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_BINDING);
            }
        }, 500);
    }

    @Override
    public void onmyEbikeBtySuccess(MyEbikeBtyBean bean) {
        hideProgress();
        if (isRefresh) {
            recyclerView.scrollToPosition(0);
        }
        if (bean.getStatus() == 1) {
            List<MyEbikeBtyBean.DataBean.EbikeBean> ebikeBeans = bean.getData().getEbike();
            List<MyEbikeBtyBean.DataBean.BatteryBean> batteryBeans = bean.getData().getBattery();
            int itemCount = ebikeBeans == null ? 0 : ebikeBeans.size();
            int itemCount1 = batteryBeans == null ? 0 : batteryBeans.size();
            if (itemCount > 0 || itemCount1 > 0) {
                noDevice.setVisibility(View.GONE);
                datapanel.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
                adapter = new MyEbikeBty_recyclerView_Adapter(requireContext(), ebikeBeans, batteryBeans);
                adapter.setOnItemClickListener(new MyEbikeBty_recyclerView_Adapter.OnItemClickListener() {
                    @Override
                    public void onEBikeBindClick(View view, MyEbikeBtyBean.DataBean.EbikeBean bean) {
                        if ("2".equals(bean.getFade_status())) {
                            String id = bean.getDevid();
                            int shop_type = bean.getShop_type();
                            mPresenter.userConfirm(uid, id, shop_type, "1");
                            //点击确定解绑
                            BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_BINDING_DEFINE_UNBINDING);
                        } else {//fade_status = 0 正常  fade_status = 3 已退
                            if ("2".equals(bean.getIs_bind())) {//待绑定
                                //点击待绑定
                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_BINDING_TO_BE_BOUND);
                                if (PubFunction.isConnect(getActivity(), true)) {
                                    Intent intent = new Intent(getActivity(), ScanCodeNewActivity.class);
                                    intent.putExtra(ScanCodeNewActivity.SCAN_CODE_IS_INPUT_BOX, "1");
                                    intent.putExtra(ScanCodeNewActivity.TYPE, "4");
                                    startActivity(intent);
                                }
                            }
                        }
                    }

                    @Override
                    public void onBatteryBindClick(View view, MyEbikeBtyBean.DataBean.BatteryBean bean) {
                        if ("2".equals(bean.getFade_status())) {
                            String id = bean.getDevid();
                            int shop_type = bean.getShop_type();
                            mPresenter.userConfirm(uid, id, shop_type, "1");
                            //点击确定解绑
                            BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_BINDING_DEFINE_UNBINDING);
                        } else {//fade_status = 0 正常  fade_status = 3 已退
                            if ("2".equals(bean.getIs_bind())) {//待绑定
                                //点击待绑定
                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_BINDING_TO_BE_BOUND);
                                Intent intent = new Intent(getActivity(), ScanCodeNewActivity.class);
                                intent.putExtra(ScanCodeNewActivity.SCAN_CODE_IS_INPUT_BOX, "1");
                                intent.putExtra(ScanCodeNewActivity.TYPE, "2");
                                startActivity(intent);
                            }
                        }
                    }
                });
                recyclerView.setAdapter(adapter);
            } else {
                noDevice.setVisibility(View.VISIBLE);
                datapanel.setVisibility(View.GONE);
            }
            refresh.finishRefresh(true);
        } else {
            MyToast.showTheToast(requireActivity(), bean.getMsg());
            refresh.finishRefresh(false);
        }
        isRefresh = false;
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
        refresh.finishRefresh(false);
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {//onNetConnected
                showMessage(throwable.toString());
            } else {//onNetDisconnected
                showMessage("无网络连接，请检查您的网络设置！");
            }
        }
    }

    @Override
    public void onmyEbikeBtyError(Throwable throwable) {
        onError(throwable);
    }

    @Override
    public void onuserConfirmSuccess(UserConfirmBean userConfirmBean, String id, int type) {
        hideProgress();
        if (userConfirmBean.getStatus() == 2) {
            String msg = userConfirmBean.getMsg();
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            final Dialog dialog = new Dialog(requireActivity(), R.style.Translucent_NoTitle);
            View view = inflater.inflate(R.layout.alertdialog_bind, null);

            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(msg);

            TextView success_t = (TextView) view.findViewById(R.id.success);
            success_t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.userConfirm(uid, id, type, "0");
                    dialog.dismiss();
                }
            });
            TextView error_t = (TextView) view.findViewById(R.id.error);
            error_t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(false);
            dialog.setContentView(view);
            dialog.show();
        } else if (userConfirmBean.getStatus() == 1) {
            MyToast.showTheToast(requireActivity(), userConfirmBean.getMsg());
            mPresenter.myEbikeBty(uid);
        } else if (userConfirmBean.getStatus() == 3) {
            //时租不处理了
        } else {
            MyToast.showTheToast(requireActivity(), userConfirmBean.getMsg());
        }
    }

    @Override
    public void onuserConfirmError(Throwable throwable) {
        onError(throwable);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ReloadListener) {
            reloadListener = (ReloadListener) context;
        } else {
            throw new IllegalArgumentException("Activity must implements FragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    private String mType = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //绑定充电器
        if (requestCode == 0x00061 && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra("codedContent");
                if (PubFunction.isConnect(getActivity(), true)) {
                    if (!cha_id.equals("")) {
//                        HttpBindCha(cha_id, content);
                        cha_id = "";
                        progressDialog.dismiss();
                    }
                }

            }
        }
    }

    @OnClick(R.id.go_shoping)
    public void onViewClicked() {
        Intent intent1 = new Intent(requireActivity(), MainShop_.class);
        requireActivity().startActivity(intent1);
    }

    public interface ReloadListener {
        void onEbikeReload(Object object);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ScanCodeEventBean bean) {
        if (bean != null) {
            if (ScanCodeEventBean.REFRESH_E_BIKE == bean.getEventMode()) {
                requestSuccessResult();
            }
        }
    }

    private void requestSuccessResult() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    scanHandler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}