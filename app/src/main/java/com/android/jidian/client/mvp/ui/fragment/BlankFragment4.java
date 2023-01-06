package com.android.jidian.client.mvp.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jidian.client.BaseFragment;
import com.android.jidian.client.Main;
import com.android.jidian.client.MainShopFromCabinet_;
import com.android.jidian.client.R;
import com.android.jidian.client.bean.MapListsBean;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.mvp.ui.activity.login.LoginActivity;
import com.android.jidian.client.net.RetrofitClient;
import com.android.jidian.client.net.RxScheduler;
import com.android.jidian.client.util.MapUtil;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
import com.bumptech.glide.Glide;
import com.permissionx.guolindev.PermissionX;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

import static com.android.jidian.client.Main.handleViewpagerVisiable;

public class BlankFragment4 extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView blank1Textview1;
    ImageView blank1Imageview1;
    RecyclerView blank1Recyclerview1;
    SmartRefreshLayout blank1SmartRefreshLayout;
    TextView infoNone;
    TextView noneDataPhone;
    TextView noneDataPhone1;
    Unbinder unbinder;

    private String mParam1;
    private String mParam2;
    private int page = 1;
    private boolean isRefresh = false;
    private boolean isFirstIn = true;
    private List<MapListsBean.DataBean> dataList = new ArrayList<>();
    private View none_layout;
    private RecyclerView_Adapter adapter;
    private boolean invalidPosition = true;//记录传入的坐标是否是0
    private int is_skip;
    private String tips;

    public BlankFragment4() {
    }

    public static BlankFragment4 newInstance(String param1, String param2) {
        BlankFragment4 fragment = new BlankFragment4();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            initData();
        }
    }

    public void refreshData() {
        double longitude = Main.longitude;
        double latitude = Main.latitude;
        if (invalidPosition && longitude != 0 && latitude != 0) {
            isRefresh = true;
            initData();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank1, container, false);
        unbinder = ButterKnife.bind(this, view);
        none_layout = view.findViewById(R.id.view_empty);
        blank1Textview1 = view.findViewById(R.id.blank1_textview1);
        blank1Imageview1 = view.findViewById(R.id.blank1_imageview1);
        blank1Recyclerview1 = view.findViewById(R.id.blank1_recyclerview1);
        blank1SmartRefreshLayout = view.findViewById(R.id.blank1_smartRefreshLayout);
        infoNone = view.findViewById(R.id.info_none);
        noneDataPhone = view.findViewById(R.id.none_data_phone);
        noneDataPhone1 = view.findViewById(R.id.none_data_phone1);
        infoNone.setText("附近暂无车行");
        blank1SmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                page = 1;
                initData();
            }
        });
        blank1SmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initData();
            }
        });
        DividerItemDecoration divider = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.recyclerview_decoration2)));
        blank1Recyclerview1.addItemDecoration(divider);

        SharedPreferences sharedPreferencesAlias = requireActivity().getSharedPreferences("contact_Info", Activity.MODE_PRIVATE);
        String phone0 = sharedPreferencesAlias.getString("phone0", "");
        if (!TextUtils.isEmpty(phone0)) {
            noneDataPhone.setVisibility(View.VISIBLE);
            noneDataPhone.setText(phone0);
        } else {
            noneDataPhone.setVisibility(View.GONE);
        }

        String phone1 = sharedPreferencesAlias.getString("phone1", "");
        if (!TextUtils.isEmpty(phone1)) {
            noneDataPhone1.setVisibility(View.VISIBLE);
            noneDataPhone1.setText(phone1);
        } else {
            noneDataPhone1.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //还车列表页访问
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_RETURN_CAR_LIST);
                }
            }, 500);
        }
    }

    private void initData() {
        mParam1 = Main.longitude.toString();
        mParam2 = Main.latitude.toString();
        if ("0.0".equals(mParam1)) {
            mParam1 = "";
        }
        if ("0.0".equals(mParam2)) {
            mParam2 = "";
        }
        Disposable disposable = RetrofitClient.getInstance().getApiService().mapLists(mParam1, mParam2, "0", "4")
                .compose(RxScheduler.Flo_io_main())
                .subscribe(this::onSuccess, this::onError);
    }

    private void onSuccess(MapListsBean bean) {
        if (isRefresh) {
            dataList = new ArrayList<>();
        }
        if (bean.getStatus() == 1) {
            List<MapListsBean.DataBean> dataBeans = bean.getData();
            is_skip = bean.getIs_skip();
            tips = bean.getTips();
            int itemCount = dataBeans == null ? 0 : dataBeans.size();
            if (itemCount > 0) {
                dataList.addAll(dataBeans);
            }
            if (dataList.size() > 0) {
                blank1Textview1.setVisibility(View.VISIBLE);
                blank1Recyclerview1.setVisibility(View.VISIBLE);
                none_layout.setVisibility(View.GONE);
                if (isFirstIn) {
                    blank1Recyclerview1.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
                    adapter = new RecyclerView_Adapter(requireContext(), dataList);
                    blank1Recyclerview1.setAdapter(adapter);
                    isFirstIn = false;
                } else {
                    adapter.setList(dataList);
                    adapter.notifyDataSetChanged();
                }
            } else {
                blank1Textview1.setVisibility(View.GONE);
                blank1Recyclerview1.setVisibility(View.GONE);
                none_layout.setVisibility(View.VISIBLE);
            }
            blank1SmartRefreshLayout.finishRefresh(true);
            blank1SmartRefreshLayout.finishLoadMore(0, true, false);
        } else {
            if (true) {
                //两种情况：第一页无数据，及最后上拉加载最后一页无数据
                if (page == 1 && dataList.isEmpty()) {//如果是请求第一页无数据，清空界面
                    if (adapter != null) {
                        adapter.setList(dataList);
                        adapter.notifyDataSetChanged();
                    }
                    none_layout.setVisibility(View.VISIBLE);
                    blank1Textview1.setVisibility(View.GONE);
                    blank1Recyclerview1.setVisibility(View.GONE);
                } else {//否则是上啦加载页，不能清空数据
                    blank1SmartRefreshLayout.finishLoadMore(0, true, false);
                }
                if (getUserVisibleHint()) {
                    MyToast.showTheToast(requireContext(), bean.getMsg());
                }
            } else {
                none_layout.setVisibility(View.VISIBLE);
                blank1Textview1.setVisibility(View.GONE);
                blank1Recyclerview1.setVisibility(View.GONE);
                blank1SmartRefreshLayout.finishLoadMore(false);
            }
            blank1SmartRefreshLayout.finishRefresh(false);
        }
        isRefresh = false;
    }

    public void onError(Throwable throwable) {
        Log.e("error", "onError: " + throwable.toString());
        if (none_layout != null) {
            none_layout.setVisibility(View.VISIBLE);
            blank1Textview1.setVisibility(View.GONE);
            blank1Recyclerview1.setVisibility(View.GONE);
            blank1SmartRefreshLayout.finishRefresh(false);
            blank1SmartRefreshLayout.finishLoadMore(false);
            ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {//onNetConnected
                    Toast.makeText(requireContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                } else {//onNetDisconnected
                    Toast.makeText(requireContext(), "无网络连接，请检查您的网络设置！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @OnClick({R.id.blank1_textview1, R.id.blank1_imageview1, R.id.none_data_phone, R.id.none_data_phone1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.blank1_textview1:
//                Intent intent00 = new Intent(requireContext(), CustomH5Page.class);
//                intent00.putExtra("url", "https://h5x.mixiangx.com/Help/guide2.html");
//                startActivity(intent00);
                break;
            case R.id.blank1_imageview1:
                handleViewpagerVisiable.sendEmptyMessage(1);
                break;
            case R.id.none_data_phone:
                String phone0_contact = requireActivity().getSharedPreferences("contact_Info", Activity.MODE_PRIVATE).getString("phone0_contact", "");
                if (!TextUtils.isEmpty(phone0_contact)) {
                    callPhone(phone0_contact);
                }
                break;
            case R.id.none_data_phone1:
                String phone1_contact = requireActivity().getSharedPreferences("contact_Info", Activity.MODE_PRIVATE).getString("phone1_contact", "");
                if (!TextUtils.isEmpty(phone1_contact)) {
                    callPhone(phone1_contact);
                }
                break;
            default:
                break;
        }
    }

    private class RecyclerView_Adapter extends RecyclerView.Adapter<RecyclerView_Adapter.ViewHolder> {
        private LayoutInflater inflater;
        private List<MapListsBean.DataBean> dataList;

        public RecyclerView_Adapter(Context context, List<MapListsBean.DataBean> dataList) {
            inflater = LayoutInflater.from(context);
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.recy_item0603, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            MapListsBean.DataBean dataBean = dataList.get(position);
            Glide.with(requireContext()).load(dataBean.getIcon()).into(holder.imageViewbg);
            holder.textViewtitle.setText(dataBean.getName());
            holder.textViewtime.setText(dataBean.getStart_time() + "-" + dataBean.getEnd_time());
            holder.textViewAddress.setText(dataBean.getAddress() + "    " + dataBean.getDistance() + "km");
            int volt = dataBean.getVolt();
            if (volt == 0) {
                holder.imageView48v.setVisibility(View.INVISIBLE);
                holder.imageView68v.setVisibility(View.INVISIBLE);
            } else if (volt == 48) {
                holder.imageView48v.setVisibility(View.VISIBLE);
                holder.imageView68v.setVisibility(View.INVISIBLE);
            } else if (volt == 60) {
                holder.imageView48v.setVisibility(View.INVISIBLE);
                holder.imageView68v.setVisibility(View.VISIBLE);
            } else {
                holder.imageView48v.setVisibility(View.VISIBLE);
                holder.imageView68v.setVisibility(View.VISIBLE);
            }
            holder.call.setOnClickListener(v -> callPhone(dataBean.getPhone()));
            holder.sign.setOnClickListener(v -> {
                MapUtil.showNavigationDialog(requireContext(), getFragmentManager(), dataBean.getName(), dataBean.getWeidu(), dataBean.getJingdu());
                uid = sharedPreferences.getString("id", "");
                if (!uid.isEmpty()) {
                    List<ParamTypeData> dataList1 = new ArrayList<>();
                    dataList1.add(new ParamTypeData("title", "站点详情-导航"));
                    dataList1.add(new ParamTypeData("sourceType", "5"));
                    dataList1.add(new ParamTypeData("sourceId", uid));
                    dataList1.add(new ParamTypeData("type", "107"));
                    dataList1.add(new ParamTypeData("client", "Android"));
                    HttpVisitLogs(dataList1);
                }
            });
            holder.itemView.setOnClickListener(v -> {
                uid = sharedPreferences.getString("id", "");
                if (!uid.isEmpty()) {
                    List<ParamTypeData> dataList1 = new ArrayList<>();
                    dataList1.add(new ParamTypeData("title", "商城访问"));
                    dataList1.add(new ParamTypeData("sourceType", "5"));
                    dataList1.add(new ParamTypeData("sourceId", uid));
                    dataList1.add(new ParamTypeData("type", "101"));
                    dataList1.add(new ParamTypeData("client", "Android"));
                    HttpVisitLogs(dataList1);
                    if (is_skip == 0) {
                        //不扫码
                        Intent intent = new Intent(requireActivity(), MainShopFromCabinet_.class);
                        intent.putExtra("froms", "20");
                        intent.putExtra("is_skip", "0");
                        intent.putExtra("merid", dataBean.getMer_id());
                        requireActivity().startActivity(intent);
                    } else if (is_skip == 1) {
                        //扫码
                        Intent intent = new Intent(requireActivity(), MainShopFromCabinet_.class);
                        intent.putExtra("froms", "20");
                        intent.putExtra("is_skip", "1");
                        intent.putExtra("merid", dataBean.getMer_id());
                        requireActivity().startActivity(intent);
                    } else if (is_skip == 2) {
                        //不跳转，给提示
                        MyToast.showTheToast(requireActivity(), tips);
                    }
                } else {
                    Toast.makeText(requireContext(), "请先进行登录！", Toast.LENGTH_SHORT).show();
                    requireActivity().startActivity(new Intent(requireActivity(), LoginActivity.class));
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList == null ? 0 : dataList.size();
        }

        public void setList(List<MapListsBean.DataBean> dataList) {
            this.dataList = dataList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView48v;
            ImageView imageView68v;
            ImageView imageViewbg;
            TextView textViewtitle;
            TextView textViewtime;
            TextView textViewAddress;
            TextView sign;
            LinearLayout call;

            ViewHolder(View itemView) {
                super(itemView);
                imageView48v = itemView.findViewById(R.id.imageView48v);
                imageView68v = itemView.findViewById(R.id.imageView68v);
                imageViewbg = itemView.findViewById(R.id.imageViewbg);
                textViewtitle = itemView.findViewById(R.id.textViewtitle);
                textViewtime = itemView.findViewById(R.id.textViewtime);
                textViewAddress = itemView.findViewById(R.id.textViewAddress);
                sign = itemView.findViewById(R.id.sign);
                call = itemView.findViewById(R.id.call);
            }
        }

    }

    void HttpVisitLogs(List<ParamTypeData> maidian_dataList) {
        new OkHttpConnect(requireActivity(), PubFunction.api + "VisitLogs/index.html", maidian_dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(requireActivity(), uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onVisitLogs(response, type);
            }
        }).startHttpThread();
    }

    void onVisitLogs(String response, String type) {
        if (type.equals("0")) {
            System.out.println(response);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
//                int status = jsonObject.getInt("status");
                String msg = jsonObject.getString("msg");
//                if (status == 1) {//统计成功
//
//                } else {//统计失败
//
//                }
                System.out.println(msg);
            } catch (JSONException e) {
                System.out.println(e.toString());
//                MyToast.showTheToast(this, e.getLocalizedMessage());
//                e.printStackTrace();
            }
        }
    }

    private void callPhone(String phoneNumber) {
        PermissionX.init(requireActivity())
                .permissions(Manifest.permission.CALL_PHONE)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        startActivity(intent);
                    } else {
                        Toast.makeText(requireActivity(), "您拒绝了如下权限： " + deniedList, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}