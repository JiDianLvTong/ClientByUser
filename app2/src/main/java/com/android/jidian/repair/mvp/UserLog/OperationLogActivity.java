package com.android.jidian.repair.mvp.UserLog;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.dialog.SelectTimeBoxDialog;
import com.android.jidian.repair.utils.DisplayUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class OperationLogActivity extends BaseActivityByMvp<OperationLogPresenter> implements OperationLogContract.View {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.edt_operation_log_search)
    EditText edtSearch;
    @BindView(R.id.tv_operation_log_time)
    TextView tvOperationLogTime;
    @BindView(R.id.iv_operation_log_time)
    TextView ivOperationLogTime;
    @BindView(R.id.rv_operation_log)
    RecyclerView rvOperationLog;
    @BindView(R.id.srf_operation_log)
    SmartRefreshLayout srfOperationLog;
    @BindView(R.id.nullDataPanel)
    LinearLayout nullDataPanel;

    private OperationLogAdapter mAdapter;
    private BatteryInquiryAdapter mBatteryInquiryAdapter;
    private MeterReadingAdapter mMeterReadingAdapter;
    private SelectTimeBoxDialog mSelectTimeBoxDialog;
    private String selectTime;

    /**
     * 服务器返回的page，加载下一页的时候不用自己+1 ，默认第一页
     */
    private int pageNum = 1;

    private String mTitle = "操作日志";
    public static final String TITLE = "this_activity_title_str";

    private String mCabid = "";
    public static final String CABID = "this_activity_cabid";

    @Override
    public int getLayoutId() {
        return R.layout.activity_operation_log;
    }

    @Override
    public void initView() {
        mTitle = getIntent().getStringExtra(TITLE);
        mCabid = getIntent().getStringExtra(CABID);
        //下拉刷新
        MaterialHeader materialHeader = new MaterialHeader(this);
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"), Color.parseColor("#D7A64A"));
        srfOperationLog.setRefreshHeader(materialHeader);
        srfOperationLog.setEnableHeaderTranslationContent(true);
        ClassicsFooter classicsFooter = new ClassicsFooter(this);
        srfOperationLog.setRefreshFooter(classicsFooter);
        tvTitle.setText(mTitle);
        mPresenter = new OperationLogPresenter();
        mPresenter.attachView(this);
        selectTime = DisplayUtils.getTime(new Date(System.currentTimeMillis()), "yyyy-MM");
        rvOperationLog.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if ("操作日志".equals(mTitle)) {
            mAdapter = new OperationLogAdapter();
            rvOperationLog.setAdapter(mAdapter);
        } else if ("电池查询".equals(mTitle) || "租电信息".equals(mTitle)) {
            if (TextUtils.isEmpty(mCabid)) {
                edtSearch.setHint("请输入电池编号");
            } else {
                findViewById(R.id.edt_operation_log_search).setVisibility(View.GONE);
                findViewById(R.id.btn_operation_log_search).setVisibility(View.GONE);
                findViewById(R.id.line_operation_log_time).setVisibility(View.GONE);
            }
            mBatteryInquiryAdapter = new BatteryInquiryAdapter(this, mTitle);
            rvOperationLog.setAdapter(mBatteryInquiryAdapter);
        } else {
            findViewById(R.id.edt_operation_log_search).setVisibility(View.GONE);
            findViewById(R.id.btn_operation_log_search).setVisibility(View.GONE);
            findViewById(R.id.line_operation_log_time).setVisibility(View.GONE);
            mMeterReadingAdapter = new MeterReadingAdapter();
            rvOperationLog.setAdapter(mMeterReadingAdapter);
        }
        rvOperationLog.setFocusableInTouchMode(false);
        rvOperationLog.setNestedScrollingEnabled(false);
        srfOperationLog.setEnableAutoLoadMore(true);
        srfOperationLog.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                requestData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                /*selectTime = nowTime;
                setSelectTime(selectTime);
                edtSearch.setText("");*/
                pageNum = 1;
                requestData();
            }
        });
        setSelectTime(selectTime);
        requestData();
    }

    @OnClick({R.id.pageReturn, R.id.btn_operation_log_search, R.id.tv_operation_log_time, R.id.iv_operation_log_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pageReturn:
                finish();
                break;
            case R.id.btn_operation_log_search:
                //搜索
                srfOperationLog.autoRefresh();
                break;
            case R.id.tv_operation_log_time:
            case R.id.iv_operation_log_time:
                if (getSupportFragmentManager() != null) {
                    ivOperationLogTime.setBackgroundResource(R.drawable.icon_top_arrow);
                    if (mSelectTimeBoxDialog == null) {
                        mSelectTimeBoxDialog = new SelectTimeBoxDialog(OperationLogActivity.this, new SelectTimeBoxDialog.OnDialogClickListener() {
                            @Override
                            public void onSure(String time) {
                                if (!TextUtils.isEmpty(time)) {
                                    if (mPresenter != null) {
                                        selectTime = time;
                                        setSelectTime(selectTime);
                                        srfOperationLog.autoRefresh();
                                    }
                                }
                            }

                            @Override
                            public void onDismiss() {
                                ivOperationLogTime.setBackgroundResource(R.drawable.icon_bottom_arrow);

                            }
                        });
                    }
                    mSelectTimeBoxDialog.showPopupWindow();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置请求日志时间
     */
    private void setSelectTime(String str) {
        if (!TextUtils.isEmpty(str)) {
            tvOperationLogTime.setText(selectTime);
        }
    }

    /**
     * 请求日志数据
     */
    private void requestData() {
        if (mPresenter != null) {
            if ("操作日志".equals(mTitle)) {
                //操作日志
                mPresenter.requestDwLogsList(selectTime, uid, edtSearch.getText().toString().trim(), pageNum + "");
            } else if ("电池查询".equals(mTitle) || "租电信息".equals(mTitle)) {
                //电池查询
                if (TextUtils.isEmpty(mCabid)) {
                    if (TextUtils.isEmpty(selectTime) || TextUtils.isEmpty(edtSearch.getText().toString().trim())) {
                        showMessage("请输入电池编号");
                        stop();
                        return;
                    }
                    mPresenter.requestBtyHistoryList(selectTime, uid, edtSearch.getText().toString().trim(), pageNum + "");
                } else {
                    mPresenter.requestCabBatteryList(selectTime, uid, mCabid, pageNum + "");
                }
            } else {
                //电表读数
                mPresenter.requestCabEleList(selectTime, uid, mCabid, pageNum + "");
            }
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
        stop();
        showMessage("无网络链接，请检查您的网络设置！");
//        MyToast.showTheToast(this, throwable.getLocalizedMessage());
    }

    @Override
    public void requestDwLogsListSuccess(OperationLogBean bean) {
        stop();
        if (bean != null) {
            if (bean.getData() != null) {
                if (bean.getData().size() != 0) {
                    rvOperationLog.setVisibility(View.VISIBLE);
                    rvOperationLog.setVisibility(View.GONE);
                    mAdapter.setTotalLength(0);
                    if (pageNum == 1) {
                        mAdapter.setNewData(bean.getData());
                        srfOperationLog.setEnableLoadMore(true);
                    } else {
                        mAdapter.addData(bean.getData());
                    }
                    pageNum = bean.getLastid();
                } else {
                    dataNull();
                }
            } else {
                dataNull();
            }
        } else {
            dataNull();
        }
    }

    @Override
    public void requestBtyHistoryListSuccess(BatteryInquiryBean bean) {
        stop();
        if (bean != null) {
            if (bean.getData() != null) {
                if (bean.getData().size() != 0) {
                    rvOperationLog.setVisibility(View.VISIBLE);
                    rvOperationLog.setVisibility(View.GONE);
                    mBatteryInquiryAdapter.setTotalLength(0);
                    if (pageNum == 1) {
                        mBatteryInquiryAdapter.setNewData(bean.getData());
                        srfOperationLog.setEnableLoadMore(true);
                    } else {
                        mBatteryInquiryAdapter.addData(bean.getData());
                    }
                    pageNum = bean.getLastid();
                } else {
                    dataNull();
                }
            } else {
                dataNull();
            }
        } else {
            dataNull();
        }
    }

    @Override
    public void requestCabEleListSuccess(MeterReadingBean bean) {
        stop();
        if (bean != null) {
            if (bean.getData() != null) {
                if (bean.getData().size() != 0) {
                    rvOperationLog.setVisibility(View.VISIBLE);
                    rvOperationLog.setVisibility(View.GONE);
                    mMeterReadingAdapter.setTotalLength(0);
                    if (pageNum == 1) {
                        mMeterReadingAdapter.setNewData(bean.getData());
                        srfOperationLog.setEnableLoadMore(true);
                    } else {
                        mMeterReadingAdapter.addData(bean.getData());
                    }
                    pageNum = bean.getLastid();
                } else {
                    dataNull();
                }
            } else {
                dataNull();
            }
        } else {
            dataNull();
        }
    }

    @Override
    public void requestDwLogsListFail(String msg) {
        stop();
        showMessage(msg);
        if ("操作日志".equals(mTitle)) {
            mAdapter.notifyDataSetChanged();
        } else if ("电池查询".equals(mTitle) || "租电信息".equals(mTitle)) {
            mBatteryInquiryAdapter.notifyDataSetChanged();
        } else {
            mMeterReadingAdapter.notifyDataSetChanged();
        }
        showNodata();
    }

    /**
     * 列表数据为空逻辑处理
     */
    private void dataNull() {
        stop();
        if (pageNum > 1) {
            srfOperationLog.setEnableLoadMore(false);
            if ("操作日志".equals(mTitle)) {
                mAdapter.setTotalLength(mAdapter.getItemCount());
                mAdapter.notifyDataSetChanged();
            } else if ("电池查询".equals(mTitle) || "租电信息".equals(mTitle)) {
                mBatteryInquiryAdapter.setTotalLength(mBatteryInquiryAdapter.getItemCount());
                mBatteryInquiryAdapter.notifyDataSetChanged();
            } else {
                mMeterReadingAdapter.setTotalLength(mMeterReadingAdapter.getItemCount());
                mMeterReadingAdapter.notifyDataSetChanged();
            }
        } else {
            showNodata();
        }
    }

    /**
     * 停止刷新或者加载
     */
    private void stop() {
        if (srfOperationLog != null) {
            srfOperationLog.finishRefresh();
            srfOperationLog.finishLoadMore();
        }
    }

    /**
     * 显示无数据布局
     */
    private void showNodata() {
        nullDataPanel.setVisibility(View.VISIBLE);
        rvOperationLog.setVisibility(View.GONE);
    }
}