package com.android.jidian.client.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.android.jidian.client.MainShop_;
import com.android.jidian.client.Newpay_;
import com.android.jidian.client.R;
import com.android.jidian.client.adapter.Expense_recyclerView_Adapter;
import com.android.jidian.client.bean.ExpenseBean;
import com.android.jidian.client.mvp.contract.ExpenseContract;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.mvp.presenter.ExpensePresenter;
import com.android.jidian.client.base.BaseFragment;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ExpenseFragment extends BaseFragment<ExpensePresenter> implements ExpenseContract.View {
    @BindView(R.id.no_device)
    LinearLayout noDevice;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.select_order_panel)
    LinearLayout selectOrderPanel;
    @BindView(R.id.data_panel)
    LinearLayout datapanel;
    Unbinder unbinder;
    private String uid;
    private Handler changeDataHandler;
    public static Handler reloadhandler;
    private ReloadListener reloadListener;

    private boolean isRefresh = false;
    private List<ExpenseBean.DataBean.EbikeBean> ebikeBeans;
    private List<ExpenseBean.DataBean.BatteryBean> batteryBeans;
    private List<ExpenseBean.DataBean.UmonthBean.UcardBean> ucardBeans;
    private List<ExpenseBean.DataBean.UmonthBean.PacketsBean> umonthBean;

    @SuppressLint("HandlerLeak")
    private void handler() {
        changeDataHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int ebikecheck = 0;
                int batterycheck = 0;
                int ucardcheck = 0;
                int umonthcheck = 0;
                for (int i = 0; i < ebikeBeans.size(); i++) {
                    if (ebikeBeans.get(i).getIschecked() == 1 || ebikeBeans.get(i).getIschecked() == 2) {
                        ebikecheck = 1;
                        break;
                    }
                }
                for (int i = 0; i < batteryBeans.size(); i++) {
                    if (batteryBeans.get(i).getIschecked() == 1 || batteryBeans.get(i).getIschecked() == 2) {
                        batterycheck = 1;
                        break;
                    }
                }
                for (int i = 0; i < ucardBeans.size(); i++) {
                    if (ucardBeans.get(i).getIschecked() == 1 || ucardBeans.get(i).getIschecked() == 2) {
                        ucardcheck = 1;
                        break;
                    }
                }
                for (int i = 0; i < umonthBean.size(); i++) {
                    if (umonthBean.get(i).getIschecked() == 1 || umonthBean.get(i).getIschecked() == 2) {
                        umonthcheck = 1;
                        break;
                    }
                }
                if (selectOrderPanel != null) {
                    if (ebikecheck == 1 || batterycheck == 1 || ucardcheck == 1 || umonthcheck == 1) {
                        selectOrderPanel.setVisibility(View.VISIBLE);
                    } else {
                        selectOrderPanel.setVisibility(View.GONE);
                    }
                }
            }
        };

        reloadhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mPresenter.expense(uid);
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.expensefragment;
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
    public void initView(View view) {
        handler();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        uid = sharedPreferences.getString("id", "");
        mPresenter = new ExpensePresenter();
        mPresenter.attachView(this);
        refresh.setEnableLoadMore(false);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                mPresenter.expense(uid);
                reloadListener.onExpenseReload(1);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //钱包-续费页访问
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_RENEWAL);
            }
        }, 500);
    }

    @Override
    public void onexpenseSuccess(ExpenseBean expenseBean) {
        hideProgress();
        if (isRefresh) {
            recyclerView.scrollToPosition(0);
        }
        if (expenseBean.getStatus() == 1) {
            ebikeBeans = expenseBean.getData().getEbike();
            batteryBeans = expenseBean.getData().getBattery();
            ucardBeans = expenseBean.getData().getUmonth().getUcard();
            umonthBean = new ArrayList<>();
            if (expenseBean.getData().getUmonth().getPackets().getGid() != null) {
                umonthBean.add(expenseBean.getData().getUmonth().getPackets());
            }

            int itemCount = ebikeBeans == null ? 0 : ebikeBeans.size();
            int itemCount1 = batteryBeans == null ? 0 : batteryBeans.size();
            int itemCount2 = ucardBeans == null ? 0 : ucardBeans.size();
            int itemCount3 = umonthBean == null ? 0 : umonthBean.size();
            if (itemCount > 0 || itemCount1 > 0 || itemCount2 > 0 || itemCount3 > 0) {
                for (int i = 0; i < ebikeBeans.size(); i++) {
                    if (ebikeBeans.get(i).getChecked() == 1 || ebikeBeans.get(i).getChecked() == 2) {
                        ebikeBeans.get(i).setIschecked(1);
                    }
                }
                for (int i = 0; i < batteryBeans.size(); i++) {
                    if (batteryBeans.get(i).getChecked() == 1 || batteryBeans.get(i).getChecked() == 2) {
                        batteryBeans.get(i).setIschecked(1);
                    }
                }
                for (int i = 0; i < ucardBeans.size(); i++) {
                    if (ucardBeans.get(i).getChecked() == 1 || ucardBeans.get(i).getChecked() == 2) {
                        ucardBeans.get(i).setIschecked(1);
                    }
                }
                for (int i = 0; i < umonthBean.size(); i++) {
                    if (umonthBean.get(i).getChecked() == 1 || umonthBean.get(i).getChecked() == 2) {
                        umonthBean.get(i).setIschecked(1);
                    }
                }
                changeDataHandler.sendEmptyMessage(1);
                noDevice.setVisibility(View.GONE);
                datapanel.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
                Expense_recyclerView_Adapter adapter = new Expense_recyclerView_Adapter(requireContext(), ebikeBeans, batteryBeans, ucardBeans, umonthBean);
                adapter.setOnItemClickListener(new Expense_recyclerView_Adapter.OnItemClickListener() {
                    @Override
                    public void onEbikeSelectClick(View view, ExpenseBean.DataBean.EbikeBean bean) {
                        ImageView imageView = view.findViewById(R.id.i_2);
                        if (bean.getChecked() != 2) {
                            if (bean.getIschecked() == 1) {
                                imageView.setImageResource(R.drawable.qianbao_select1);
                                bean.setIschecked(0);
                            } else {
                                imageView.setImageResource(R.drawable.qianbao_select_2);
                                bean.setIschecked(1);
                            }
                        }
                        changeDataHandler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onBatterySelectClick(View view, ExpenseBean.DataBean.BatteryBean bean) {
                        ImageView imageView = view.findViewById(R.id.i_2);
                        if (bean.getChecked() != 2) {
                            if (bean.getIschecked() == 1) {
                                imageView.setImageResource(R.drawable.qianbao_select1);
                                bean.setIschecked(0);
                            } else {
                                imageView.setImageResource(R.drawable.qianbao_select_2);
                                bean.setIschecked(1);
                            }
                        }
                        changeDataHandler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onUcardSelectClick(View view, ExpenseBean.DataBean.UmonthBean.UcardBean bean) {
                        ImageView imageView = view.findViewById(R.id.i_2);
                        if (bean.getChecked() != 2) {
                            if (bean.getIschecked() == 1) {
                                imageView.setImageResource(R.drawable.qianbao_select1);
                                bean.setIschecked(0);
                            } else {
                                imageView.setImageResource(R.drawable.qianbao_select_2);
                                bean.setIschecked(1);
                            }
                        }
                        changeDataHandler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onUmonthSelectClick(View view, ExpenseBean.DataBean.UmonthBean.PacketsBean bean) {
                        ImageView imageView = view.findViewById(R.id.i_2);
                        if (bean.getChecked() != 2) {
                            if (bean.getIschecked() == 1) {
                                imageView.setImageResource(R.drawable.qianbao_select1);
                                bean.setIschecked(0);
                            } else {
                                imageView.setImageResource(R.drawable.qianbao_select_2);
                                bean.setIschecked(1);
                            }
                        }
                        changeDataHandler.sendEmptyMessage(1);
                    }
                });
                recyclerView.setAdapter(adapter);
            } else {
                noDevice.setVisibility(View.VISIBLE);
                datapanel.setVisibility(View.GONE);
            }
            refresh.finishRefresh(true);
        } else {
            MyToast.showTheToast(requireActivity(), expenseBean.getMsg());
            refresh.finishRefresh(false);
        }
        isRefresh = false;
    }

    @Override
    public void onexpenseError(Throwable throwable) {
        onError(throwable);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.go_shoping, R.id.select_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_shoping:
                Intent intent1 = new Intent(requireActivity(), MainShop_.class);
                requireActivity().startActivity(intent1);
                break;
            case R.id.select_order:
                //点击立即续费
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_RENEWAL_IMMEDIATE_RENEWAL);
                JSONArray jsonArray = new JSONArray();
                if (ebikeBeans != null) {
                    for (int i = 0; i < ebikeBeans.size(); i++) {
                        if (ebikeBeans.get(i).getIschecked() == 1 || ebikeBeans.get(i).getIschecked() == 2) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("devid", ebikeBeans.get(i).getDevid());
                                jsonObject.put("otype", ebikeBeans.get(i).getOtype());
                                jsonObject.put("relet", ebikeBeans.get(i).getRelet());
                                jsonObject.put("numt", ebikeBeans.get(i).getNumt());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jsonArray.put(jsonObject);
                        }
                    }
                }

                if (batteryBeans != null) {
                    for (int i = 0; i < batteryBeans.size(); i++) {
                        if (batteryBeans.get(i).getIschecked() == 1 || batteryBeans.get(i).getIschecked() == 2) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("devid", batteryBeans.get(i).getDevid());
                                jsonObject.put("otype", batteryBeans.get(i).getOtype());
                                jsonObject.put("relet", batteryBeans.get(i).getRelet());
                                jsonObject.put("numt", batteryBeans.get(i).getNumt());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jsonArray.put(jsonObject);
                        }
                    }
                }

                if (ucardBeans != null) {
                    for (int i = 0; i < ucardBeans.size(); i++) {
                        if (ucardBeans.get(i).getIschecked() == 1 || ucardBeans.get(i).getIschecked() == 2) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("gid", ucardBeans.get(i).getGid());
                                jsonObject.put("otype", ucardBeans.get(i).getOtype());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jsonArray.put(jsonObject);
                        }
                    }
                }

                if (umonthBean != null) {
                    for (int i = 0; i < umonthBean.size(); i++) {
                        if (umonthBean.get(i).getIschecked() == 1 || umonthBean.get(i).getIschecked() == 2) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("gid", umonthBean.get(i).getGid());
                                jsonObject.put("otype", umonthBean.get(i).getOtype());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jsonArray.put(jsonObject);
                        }
                    }
                }

                List<ParamTypeData> dataList1 = new ArrayList<>();
                dataList1.add(new ParamTypeData("title", "立即续费访问"));
                dataList1.add(new ParamTypeData("sourceType", "5"));
                dataList1.add(new ParamTypeData("sourceId", uid));
                dataList1.add(new ParamTypeData("type", "111"));
                dataList1.add(new ParamTypeData("client", "Android"));
                HttpVisitLogs(dataList1);

                //from=40
                Intent intent = new Intent(getActivity(), Newpay_.class);
                intent.putExtra("mjson", jsonArray.toString());
                intent.putExtra("type", "40");
                requireActivity().startActivity(intent);
                break;
            default:
                break;
        }
    }

    void HttpVisitLogs(List<ParamTypeData> maidian_dataList) {
        new OkHttpConnect(getActivity(), PubFunction.api + "VisitLogs/index.html", maidian_dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(getActivity(), uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onVisitLogs(response, type);
            }
        }).startHttpThread();
    }

    void onVisitLogs(String response, String type) {
        if ("0".equals(type)) {
            System.out.println(response);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String msg = jsonObject.getString("msg");
                System.out.println(msg);
            } catch (JSONException e) {
                System.out.println(e.toString());
            }
        }
    }

    public interface ReloadListener {
        void onExpenseReload(Object object);
    }
}