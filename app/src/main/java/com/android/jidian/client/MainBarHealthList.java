package com.android.jidian.client;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by hasee on 2017/6/6.
 */
@EActivity(R.layout.main_bar_health_list)
public class MainBarHealthList extends BaseActivity implements AbsListView.OnScrollListener {
    @ViewById
    LinearLayout page_return, none_panel, history_linearLayout;
    @ViewById
    ListView listview;
    @ViewById
    PullToRefreshLayout refresh;
    @ViewById
    TextView timetext;
    private List<Map<String, String>> dataList = new ArrayList<>();
    private MySimpleAdapter_message_1 mySimpleAdapter_message_1;
    private String lastid = "";
    private int count_page = 2;
    private int current_page = 1;
    private String time = "";

    @AfterViews
    void afterviews() {
        init();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //换电记录页面访问
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_POWER_CHANGE_RECORD);
            }
        }, 500);
    }

    @Click
    void page_return() {
        this.finish();
    }

    private void init() {
        refresh.setCanLoadMore(false);
        refresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                lastid = "";
                count_page = 2;
                current_page = 1;
                dataList.clear();
                if (PubFunction.isConnect(activity, true)) {
                    progressDialog.show();
                    HttpMessage_1();
                } else {
                    if (refresh != null) {
                        refresh.finishRefresh();
                    }
                }
            }

            @Override
            public void loadMore() {
            }
        });
        listview.setOnScrollListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lastid = "";
        count_page = 2;
        current_page = 1;
        dataList.clear();

        if (PubFunction.isConnect(activity, true)) {
            progressDialog.show();
            HttpMessage_1();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if ((view.getLastVisiblePosition() == view.getCount() - 1) && current_page < count_page) {
                HttpMessage_1_RE(lastid);
                progressDialog.show();
                current_page = count_page;
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
    }

    /**
     * http接口：Advices/type.html    获取消息信息
     */
    @Background
    void HttpMessage_1() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        if (!TextUtils.isEmpty(time)) {
            dataList.add(new ParamTypeData("time", time));
        }
        new OkHttpConnect(activity, PubFunction.app + "User/swapListsv2.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpMessage_1(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpMessage_1(String response, String type) {
        if (refresh != null) {
            refresh.finishRefresh();
        }
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
                if ("1".equals(status)) {
                    none_panel.setVisibility(View.GONE);
                    JSONObject jsonObject_data = jsonObject_response.getJSONObject("data");
                    JSONArray jSONArray = jsonObject_data.getJSONArray("lists");
                    this.lastid = jsonObject_data.getString("lastid");
                    if (jSONArray.equals("null") || jSONArray.equals("") || jSONArray.equals("[]")) {
                        none_panel.setVisibility(View.VISIBLE);
                    } else {
                        for (int i = 0; i < jSONArray.length(); i++) {
                            JSONObject jsonObject = jSONArray.getJSONObject(i);
                            Map<String, String> map = new HashMap<>();
                            map.put("id", jsonObject.getString("id"));
                            map.put("name", jsonObject.getString("name"));
                            map.put("number", jsonObject.getString("number"));
                            map.put("in_electric", jsonObject.getString("in_electric"));
                            map.put("out_electric", jsonObject.getString("out_electric"));
                            map.put("electric", jsonObject.getString("electric"));
                            map.put("cycles", jsonObject.getString("cycles"));
                            map.put("tips", jsonObject.getString("tips"));
                            map.put("create_time", jsonObject.getString("create_time"));
                            map.put("ctype_info", jsonObject.optString("ctype_info"));
                            dataList.add(map);
                        }
                        mySimpleAdapter_message_1 = new MySimpleAdapter_message_1(activity, dataList, R.layout.main_bar_health_list_item1);
                        listview.setAdapter(mySimpleAdapter_message_1);
                        listview.setDividerHeight(0);
                    }
                } else if ("0".equals(status)) {
                    none_panel.setVisibility(View.VISIBLE);
                } else {
                    MyToast.showTheToast(activity, msg);
                }
            } catch (Exception e) {
                MyToast.showTheToast(activity, "JSON：" + e.toString());
            }
        }
    }

    /**
     * http接口：Advices/type.html    获取消息信息
     */
    @Background
    void HttpMessage_1_RE(String lastid_str) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("lastid", lastid_str));
        if (!TextUtils.isEmpty(time)) {
            dataList.add(new ParamTypeData("time", time));
        }
        new OkHttpConnect(activity, PubFunction.app + "User/swapListsv2.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpMessage_1_RE(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpMessage_1_RE(String response, String type) {
        if (refresh != null) {
            refresh.finishRefresh();
        }
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
                if ("1".equals(status)) {
                    JSONObject jsonObject_data = jsonObject_response.getJSONObject("data");
                    JSONArray jsonArray = jsonObject_data.getJSONArray("lists");
                    String lastid = jsonObject_data.getString("lastid");
                    this.lastid = lastid;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Map<String, String> map = new HashMap<>();
                        map.put("id", jsonObject.getString("id"));
                        map.put("name", jsonObject.getString("name"));
                        map.put("number", jsonObject.getString("number"));
                        map.put("in_electric", jsonObject.getString("in_electric"));
                        map.put("out_electric", jsonObject.getString("out_electric"));
                        map.put("electric", jsonObject.getString("electric"));
                        map.put("cycles", jsonObject.getString("cycles"));
                        map.put("tips", jsonObject.getString("tips"));
                        map.put("create_time", jsonObject.getString("create_time"));
                        map.put("ctype_info", jsonObject.optString("ctype_info"));
                        dataList.add(map);
                    }
                    mySimpleAdapter_message_1.notifyDataSetChanged();
                    count_page = count_page + 1;
                } else {
                    MyToast.showTheToast(activity, msg);
                }
            } catch (Exception e) {
                MyToast.showTheToast(activity, "JSON：" + e.toString());
            }
        }
    }

    class MySimpleAdapter_message_1 extends BaseAdapter {
        private List<? extends Map<String, ?>> data;
        private int resource;
        private LayoutInflater mInflater;

        MySimpleAdapter_message_1(Context context, List<? extends Map<String, ?>> data, int resource) {
            this.data = data;
            this.resource = resource;
            mInflater = LayoutInflater.from(context);
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
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(resource, null);
                viewHolder = new ViewHolder();
                viewHolder.t_2 = convertView.findViewById(R.id.t_2);
                viewHolder.t_3 = convertView.findViewById(R.id.t_3);
                viewHolder.t_4 = convertView.findViewById(R.id.t_4);
                viewHolder.t_5 = convertView.findViewById(R.id.t_5);
                viewHolder.ctype_info_textView = convertView.findViewById(R.id.ctype_info_textView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Map<String, ?> map = data.get(position);
            viewHolder.t_2.setText("ID:" + map.get("number").toString());
            viewHolder.t_3.setText(map.get("create_time").toString());
            viewHolder.t_4.setText(map.get("tips").toString());
            viewHolder.t_5.setText(map.get("cycles").toString() + " 循环");
            String ctype_info = (String) map.get("ctype_info");
            if (ctype_info != null) {
                viewHolder.ctype_info_textView.setText(((String) map.get("ctype_info")));
            } else {
                viewHolder.ctype_info_textView.setText("");
            }
            return convertView;
        }

        class ViewHolder {
            TextView t_2;
            TextView t_3;
            TextView t_4;
            TextView t_5;
            TextView ctype_info_textView;
        }
    }

    private TimePickerView pvCustomTime;

    private void initCustomTimePicker() {
        /*
         * 注意事项：
         * 1.自定义布局中，id为optionspicker或者timepicker的布局以及其子控件必须要有，否则会报空指针
         * 2.因为系统Calendar的月份是从0-11的，所以如果是调用Calendar的set方法来设置时间，月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017, 0, 1);
        Calendar endDate = Calendar.getInstance();
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
                time = format.format(date);
                timetext.setText(time);
                lastid = "";
                count_page = 2;
                current_page = 1;
                dataList.clear();
                if (PubFunction.isConnect(activity, true)) {
                    progressDialog.show();
                    HttpMessage_1();
                } else {
                    if (refresh != null) {
                        refresh.finishRefresh();
                    }
                }
            }
        })
                .setContentTextSize(20)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                        final TextView tv_cancel = v.findViewById(R.id.tv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        tv_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setContentTextSize(20)
                .setType(new boolean[]{true, true, false, false, false, false})
                .setLabel("", "", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.5f)
                .setTextXOffset(40, -40, 40, 0, 0, 0)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.parseColor("#e6e6e6"))
                .build();
    }

    @Click
    void history_linearLayout() {
        if (pvCustomTime == null) {
            initCustomTimePicker();
        }
        pvCustomTime.show();
    }
}