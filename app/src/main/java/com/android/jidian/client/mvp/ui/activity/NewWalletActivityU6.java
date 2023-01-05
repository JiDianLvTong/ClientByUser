package com.android.jidian.client.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.android.jidian.client.MainBarHealthList_;
import com.android.jidian.client.MainBarHealth_;
import com.android.jidian.client.R;
import com.android.jidian.client.application.MyApplication;
import com.android.jidian.client.base.U6BaseActivityByMvp;
import com.android.jidian.client.bean.ExpenseBean;
import com.android.jidian.client.mvp.ui.fragment.WalletDepositFragment;
import com.android.jidian.client.mvp.ui.fragment.WallteHelloFragment;
import com.android.jidian.client.mvp.ui.fragment.WallteHelloFragment_;
import com.android.jidian.client.net.RetrofitClient;
import com.android.jidian.client.net.RxScheduler;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.util.BuryingPointManager;
import com.android.jidian.client.mvp.ui.fragment.ExpenseFragment;
import com.android.jidian.client.mvp.ui.fragment.MyEbikeBtyFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * @author : PTT
 * date: 9/18/21 2:26 PM
 * company: 兴达智联
 * description: 我的钱包
 */
public class NewWalletActivityU6 extends U6BaseActivityByMvp implements ExpenseFragment.ReloadListener, MyEbikeBtyFragment.ReloadListener {
    @BindView(R.id.tv_wallet_renew)
    TextView tvWalletRenew;
    @BindView(R.id.tv_wallet_hello_recharge)
    TextView tvWalletHelloRecharge;
    @BindView(R.id.tv_wallet_bind)
    TextView tvWalletBind;
    @BindView(R.id.tv_wallet_deposit)
    TextView tvWalletDeposit;
    @BindView(R.id.gv_wallet_info)
    GridView gvWalletInfo;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private Fragment wallteHelloFragment, myEbikeBtyFragment, expenseFragment;
    private WalletDepositFragment mWalletDepositFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_new_wallet);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        tvTitle.setText("我的钱包");
        HttpGetWalletInfo();
        showFragment(1);

        findViewById(R.id.btn_wallet_renew).setOnClickListener(v -> {
            //点击续费
            MyApplication.myWalletPanelSelect = 1;
            showFragment(1);
        });

        findViewById(R.id.btn_wallet_hello_recharge).setOnClickListener(v -> {
            //点击换电币充值
            MyApplication.myWalletPanelSelect = 2;
            showFragment(2);
        });

        findViewById(R.id.btn_wallet_bind).setOnClickListener(v -> {
            //点击绑定
            MyApplication.myWalletPanelSelect = 3;
            showFragment(3);
        });

        findViewById(R.id.li_wallet_deposit).setOnClickListener(v -> {
            //点击押金
            MyApplication.myWalletPanelSelect = 4;
            showFragment(4);
        });

        findViewById(R.id.btn_wallet_battery_life).setOnClickListener(v -> {
            //点击电池寿命
            BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_RENEWAL_BATTTERY_LIFE);
            Intent intent = new Intent(this, MainBarHealth_.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_wallet_power_exchange_ecord).setOnClickListener(v -> {
            //点击换电记录
            BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_RENEWAL_POWER_CHANGE_RECORD);
            Intent intent = new Intent(this, MainBarHealthList_.class);
            startActivity(intent);
        });

        findViewById(R.id.iv_back).setOnClickListener(v -> {
            this.finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.myWalletPanelSelect = -1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        int a = MyApplication.myWalletPanelSelect;
        if (a == 1 || a == -1) {
            showFragment(1);
        }
        if (a == 2) {
            showFragment(2);
        }
        if (a == 3) {
            showFragment(3);
        }
    }

    /**
     * 获取钱包信息
     */
    void HttpGetWalletInfo() {
        Disposable disposable = RetrofitClient.getInstance().getApiService().requestWalletInfo(uid)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(this::onSuccess, this::onError, () -> progressDialog.dismiss());
    }

    private void onSuccess(ExpenseBean bean) {
        if (bean.getStatus() == 1) {
            if (bean.getData() != null) {
                if (bean.getData().getTop() != null) {
                    if (bean.getData().getTop().getCList() != null) {
                        GridAdapter gridAdapter = new GridAdapter(activity, bean.getData().getTop().getCList(), R.layout.main_wallte_top_item);
                        gvWalletInfo.setAdapter(gridAdapter);
                        gvWalletInfo.setHorizontalSpacing(10);
                        gvWalletInfo.setVerticalSpacing(20);
                    }
                }
            }
        } else {
            MyToast.showTheToast(activity, bean.getMsg());
        }
    }

    public void onError(Throwable throwable) {
        Log.e("error", "onError: " + throwable.toString());
    }

    public void hintFragment(FragmentTransaction ft) {
        if (expenseFragment != null) {
            ft.hide(expenseFragment);
        }
        if (wallteHelloFragment != null) {
            ft.hide(wallteHelloFragment);
        }
        if (myEbikeBtyFragment != null) {
            ft.hide(myEbikeBtyFragment);
        }
        if (mWalletDepositFragment != null) {
            ft.hide(mWalletDepositFragment);
        }
    }

    public void showFragment(int index) {
        // Fragment事务管理器
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // 隐藏已加载过的Fragment
        hintFragment(ft);
        tvWalletRenew.setTextColor(Color.parseColor("#555555"));
        tvWalletHelloRecharge.setTextColor(Color.parseColor("#555555"));
        tvWalletBind.setTextColor(Color.parseColor("#555555"));
        tvWalletDeposit.setTextColor(Color.parseColor("#555555"));
        switch (index) {
            // 判断Fragment是否实例化，实例化过直接显示出来，否者实例化
            case 1:
                if (expenseFragment != null) {
                    ft.show(expenseFragment);
                    ExpenseFragment.reloadhandler.sendEmptyMessage(1);
                } else {
                    expenseFragment = new ExpenseFragment();
                    ft.add(R.id.fl_wallet, expenseFragment);
                }
                tvWalletRenew.setTextColor(Color.parseColor("#2ba245"));
                break;
            case 2:
                if (wallteHelloFragment != null) {
                    ft.show(wallteHelloFragment);
                    WallteHelloFragment.wallteHelloReloadHandler.sendEmptyMessage(1);
                } else {
                    wallteHelloFragment = new WallteHelloFragment_();
                    ft.add(R.id.fl_wallet, wallteHelloFragment);
                }
                tvWalletHelloRecharge.setTextColor(Color.parseColor("#2ba245"));
                break;
            case 3:
                if (myEbikeBtyFragment != null) {
                    ft.show(myEbikeBtyFragment);
                    MyEbikeBtyFragment.myEbikeReloadHandler.sendEmptyMessage(1);
                } else {
                    myEbikeBtyFragment = new MyEbikeBtyFragment();
                    ft.add(R.id.fl_wallet, myEbikeBtyFragment);
                }
                tvWalletBind.setTextColor(Color.parseColor("#2ba245"));
                break;
            case 4:
                if (mWalletDepositFragment != null) {
                    ft.show(mWalletDepositFragment);
                    mWalletDepositFragment.requestData();
                } else {
                    mWalletDepositFragment = new WalletDepositFragment();
                    ft.add(R.id.fl_wallet, mWalletDepositFragment);
                }
                tvWalletDeposit.setTextColor(Color.parseColor("#2ba245"));
                break;
            default:
                break;
        }
        ft.commit();
    }

    class GridAdapter extends BaseAdapter {
        private List<ExpenseBean.DataBean.TopBean.CListBean> data = new ArrayList<>();
        private int layout;
        private LayoutInflater inflater;

        GridAdapter(Activity activity, List<ExpenseBean.DataBean.TopBean.CListBean> data, int layout) {
            this.data = data;
            this.layout = layout;
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(layout, parent, false);
            TextView t_1 = convertView.findViewById(R.id.t_1);
            TextView t_2 = convertView.findViewById(R.id.t_2);
            TextView t_3 = convertView.findViewById(R.id.t_3);
            AssetManager assetManager = getAssets();
            Typeface typeface = Typeface.createFromAsset(assetManager, "fonts/DIN-Bold.otf");
            t_1.setTypeface(typeface);
            if (data != null) {
                ExpenseBean.DataBean.TopBean.CListBean mBean = data.get(position);
                if (mBean != null) {
                    t_1.setText(mBean.getNums() + "");
                    t_2.setText(mBean.getUnit());
                    t_3.setText(mBean.getName());
                }
            }
            return convertView;
        }
    }

    @Override
    public void onExpenseReload(Object object) {
        HttpGetWalletInfo();
    }

    @Override
    public void onEbikeReload(Object object) {
        HttpGetWalletInfo();
    }
}