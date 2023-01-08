package com.android.jidian.client;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.util.BuryingPointManager;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.bumptech.glide.Glide;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 充值换电币界面 activity
 */
@EActivity(R.layout.main_hello)
public class MainHello extends BaseActivity {
    @ViewById
    GridView gridview;
    @ViewById
    LinearLayout bottom_panel;
    @ViewById
    PullToRefreshLayout refresh;
    @ViewById
    ImageView bottom_image;
    @ViewById
    TextView t_1, t_2, t_3, t_4;
    @ViewById
    TextView t_5_1, t_5_2, t_6_1, t_6_2, t_7_1, t_7_2;
    @ViewById
    ImageView i_6, i_7;
    private MyGridAdapter myGridAdapter = null;

    @AfterViews
    void afterViews() {
        HttpGetShopInfoItem();
        refresh.setCanLoadMore(false);
        refresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            System.out.println(e.getLocalizedMessage());
                        }
                        if (PubFunction.isConnect(activity, true)) {
                            HttpGetShopInfoItem();
                        } else {
                            if (refresh != null) {
                                refresh.finishRefresh();
                            }
                        }
                    }
                }.start();
            }

            @Override
            public void loadMore() {
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //充值换电币页面访问
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_RECHARGE_HELLO_COIN);
            }
        }, 500);
    }

    @Click
    void page_return() {
        this.finish();
    }

    @Background
    void HttpGetShopInfoItem() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        new OkHttpConnect(activity, PubFunction.app + "Goodsv2/hello.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetShopInfoItem(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpGetShopInfoItem(String response, String type) {
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
                    JSONObject jsonObject = jsonObject_response.getJSONObject("data");
                    //top数据
                    JSONArray hello_JsonArray = jsonObject.getJSONArray("hello");
                    final List<Map<String, String>> gridListData = new ArrayList<>();
                    for (int i = 0; i < hello_JsonArray.length(); i++) {
                        JSONObject hello_Object_item = hello_JsonArray.getJSONObject(i);
                        Map<String, String> map = new HashMap<>();
                        map.put("id", hello_Object_item.getString("id"));
                        map.put("rprice", hello_Object_item.getString("rprice"));
                        map.put("oprice", hello_Object_item.getString("oprice"));
                        map.put("fvalue", hello_Object_item.getString("fvalue"));
                        gridListData.add(map);
                    }
                    if (myGridAdapter == null) {
                        myGridAdapter = new MyGridAdapter(activity, gridListData, R.layout.u6_activity_pay_coin_item);
                        gridview.setAdapter(myGridAdapter);
                    } else {
                        myGridAdapter.notifyDataSetChanged();
                    }
                    PubFunction.setGridViewHeight(gridview, hello_JsonArray.length(), 2);
                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (gridListData != null) {
                                String priceStr = gridListData.get(position).get("rprice");
                                if ("5".equals(priceStr)) {
                                    //点击5元
                                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_RECHARGE_HELLO_COIN_5);
                                } else if ("50".equals(priceStr)) {
                                    //点击50元
                                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_RECHARGE_HELLO_COIN_50);
                                } else if ("100".equals(priceStr)) {
                                    //点击100元
                                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_RECHARGE_HELLO_COIN_100);
                                } else if ("200".equals(priceStr)) {
                                    //点击200元
                                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_RECHARGE_HELLO_COIN_200);
                                } else if ("400".equals(priceStr)) {
                                    //点击400元
                                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_RECHARGE_HELLO_COIN_400);
                                }
                            }
                            String item_id = gridListData.get(position).get("id");
                            Intent intent = new Intent(activity, Newshoppay_.class);
                            intent.putExtra(PubFunction.FROM_ACTOVITY, PubFunction.RECHARGE_HELLO_COIN);
                            intent.putExtra("id", item_id);
                            intent.putExtra("from", "product");
                            activity.startActivity(intent);
                        }
                    });
                    //bottom数据
                    if (!"[]".equals(jsonObject.getString("packs"))) {
                        JSONObject packs_jsonObject = jsonObject.getJSONObject("packs");
                        final String id = packs_jsonObject.getString("id");
                        String name = packs_jsonObject.getString("name");
                        final String rprice = packs_jsonObject.getString("rprice");
                        JSONObject packs_jsonObject_item = packs_jsonObject.getJSONObject("descs");
                        String bottom_image_str = packs_jsonObject_item.getString("bg_img");
                        JSONArray packs_jsonObject_item_left_JsonArray = packs_jsonObject_item.getJSONArray("left");
                        JSONArray packs_jsonObject_item_right_JsonArray = packs_jsonObject_item.getJSONArray("right");
                        Glide.with(activity).load(bottom_image_str).into(bottom_image);
                        t_1.setText(name);
                        t_2.setText(rprice);
                        int packs_jsonObject_item_left_JsonArray_len = packs_jsonObject_item_left_JsonArray.length();
                        if (packs_jsonObject_item_left_JsonArray_len == 1) {
                            t_3.setText(packs_jsonObject_item_left_JsonArray.getString(0));
                        }
                        if (packs_jsonObject_item_left_JsonArray_len == 2) {
                            t_3.setText(packs_jsonObject_item_left_JsonArray.getString(1));
                            t_4.setText(packs_jsonObject_item_left_JsonArray.getString(0));
                        }
                        int packs_jsonObject_item_right_JsonArray_len = packs_jsonObject_item_right_JsonArray.length();
                        if (packs_jsonObject_item_right_JsonArray_len > 0) {
                            JSONObject jsonObject_item_1 = packs_jsonObject_item_right_JsonArray.getJSONObject(0);
                            t_5_1.setText(jsonObject_item_1.getString("title"));
                            t_5_2.setText(jsonObject_item_1.getString("price") + "元");
                        }
                        if (packs_jsonObject_item_right_JsonArray_len > 1) {
                            JSONObject jsonObject_item_2 = packs_jsonObject_item_right_JsonArray.getJSONObject(1);
                            t_6_1.setText(jsonObject_item_2.getString("title"));
                            t_6_2.setText(jsonObject_item_2.getString("price") + "元");
                            i_6.setVisibility(View.VISIBLE);
                        }
                        if (packs_jsonObject_item_right_JsonArray_len > 2) {
                            JSONObject jsonObject_item_3 = packs_jsonObject_item_right_JsonArray.getJSONObject(2);
                            t_7_1.setText(jsonObject_item_3.getString("title"));
                            t_7_2.setText(jsonObject_item_3.getString("price") + "元");
                            i_7.setVisibility(View.VISIBLE);
                        }
                        bottom_panel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(activity, Newshoppay_.class);
                                intent.putExtra("id", id);
                                intent.putExtra("from", "product");
                                activity.startActivity(intent);
                            }
                        });
                    }
                } else {
                    MyToast.showTheToast(activity, msg);
                }
            } catch (Exception e) {
                MyToast.showTheToast(activity, "JSON：" + e.toString());
            }
        }
    }

    static class ViewHolder {
        TextView t_1;
        TextView t_2;
        TextView t_3;
        TextView t_4;
    }

    class MyGridAdapter extends BaseAdapter {
        private List<Map<String, String>> data;
        private int layout;
        private LayoutInflater inflater;

        MyGridAdapter(Activity activity, List<Map<String, String>> data, int layout) {
            this.data = data;
            this.layout = layout;
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Map<String, String> getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Map<String, String> item = getItem(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(layout, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.t_1 = (TextView) convertView.findViewById(R.id.t_1);
                viewHolder.t_2 = (TextView) convertView.findViewById(R.id.t_2);
                viewHolder.t_3 = (TextView) convertView.findViewById(R.id.t_3);
                viewHolder.t_4 = (TextView) convertView.findViewById(R.id.t_4);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            AssetManager assetManager = getAssets();
            Typeface typeface = Typeface.createFromAsset(assetManager, "fonts/DIN-Bold.otf");
            viewHolder.t_1.setTypeface(typeface);
            viewHolder.t_4.setTypeface(typeface);
            viewHolder.t_1.setText(item.get("fvalue"));
            viewHolder.t_2.setText("售价 " + item.get("rprice") + "元");
            viewHolder.t_3.setText(item.get("oprice"));
            viewHolder.t_3.getPaint().setAntiAlias(true);
            viewHolder.t_3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            return convertView;
        }
    }
}