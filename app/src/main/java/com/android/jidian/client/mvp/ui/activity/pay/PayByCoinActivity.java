package com.android.jidian.client.mvp.ui.activity.pay;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivity;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
import com.android.jidian.client.widgets.MyToast;
import com.bumptech.glide.Glide;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PayByCoinActivity extends U6BaseActivity {

    @BindView(R.id.gridView)
    public GridView gridView;
    @BindView(R.id.statusBarView)
    public SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.bottomImage)
    public ImageView bottomImage;
    @BindView(R.id.bottomPanel)
    public FrameLayout bottomPanel;

    @BindView(R.id.t_1)
    public TextView t_1;
    @BindView(R.id.t_2)
    public TextView t_2;
    @BindView(R.id.t_3)
    public TextView t_3;
    @BindView(R.id.t_4)
    public TextView t_4;

    @BindView(R.id.t_5_1)
    public TextView t_5_1;
    @BindView(R.id.t_5_2)
    public TextView t_5_2;

    @BindView(R.id.t_6_1)
    public TextView t_6_1;
    @BindView(R.id.t_6_2)
    public TextView t_6_2;

    @BindView(R.id.t_7_1)
    public TextView t_7_1;
    @BindView(R.id.t_7_2)
    public TextView t_7_2;

    @BindView(R.id.i_6)
    public ImageView i_6;
    @BindView(R.id.i_7)
    public ImageView i_7;

    private MyGridAdapter myGridAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_pay_coin);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        HttpGetShopInfoItem();
        MaterialHeader materialHeader = new MaterialHeader(activity);
        materialHeader.setColorSchemeColors(Color.parseColor("#D7A64A"),Color.parseColor("#D7A64A"));
        smartRefreshLayout.setRefreshHeader(materialHeader);
        smartRefreshLayout.setEnableHeaderTranslationContent(true);
        smartRefreshLayout.setOnRefreshListener(new com.scwang.smart.refresh.layout.listener.OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull com.scwang.smart.refresh.layout.api.RefreshLayout refreshLayout) {
                HttpGetShopInfoItem();
            }
        });
        smartRefreshLayout.setEnableLoadMore(false);
    }

    private void HttpGetShopInfoItem() {
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

    void onDataHttpGetShopInfoItem(String response, String type) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                smartRefreshLayout.finishRefresh();
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
                                gridView.setAdapter(myGridAdapter);
                            } else {
                                myGridAdapter.notifyDataSetChanged();
                            }
                            PubFunction.setGridViewHeight(gridView, hello_JsonArray.length(), 2);
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                                    Intent intent = new Intent(activity, PayByCreateOrderActivity.class);
                                    intent.putExtra(PubFunction.FROM_ACTOVITY, PubFunction.RECHARGE_HELLO_COIN);
                                    intent.putExtra("id", item_id);
                                    intent.putExtra("from", "product");
                                    activity.startActivity(intent);
                                }
                            });
                            //bottom数据
                            if (!"[]".equals(jsonObject.getString("packs"))) {
                                bottomPanel.setVisibility(View.VISIBLE);
                                JSONObject packs_jsonObject = jsonObject.getJSONObject("packs");
                                final String id = packs_jsonObject.getString("id");
                                String name = packs_jsonObject.getString("name");
                                final String rprice = packs_jsonObject.getString("rprice");
                                JSONObject packs_jsonObject_item = packs_jsonObject.getJSONObject("descs");
                                String bottom_image_str = packs_jsonObject_item.getString("bg_img");
                                JSONArray packs_jsonObject_item_left_JsonArray = packs_jsonObject_item.getJSONArray("left");
                                JSONArray packs_jsonObject_item_right_JsonArray = packs_jsonObject_item.getJSONArray("right");
                                Glide.with(activity).load(bottom_image_str).into(bottomImage);
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
                                bottomPanel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(activity, PayByCreateOrderActivity.class);
                                        intent.putExtra("id", id);
                                        intent.putExtra("from", "product");
                                        activity.startActivity(intent);
                                    }
                                });
                            }else{
                                bottomPanel.setVisibility(View.GONE);
                            }
                        } else {
                            MyToast.showTheToast(activity, msg);
                        }
                    } catch (Exception e) {
                        MyToast.showTheToast(activity, "JSON：" + e.toString());
                    }
                }
            }
        });
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
            viewHolder.t_3.setText("原价 " + item.get("oprice"));
            viewHolder.t_3.getPaint().setAntiAlias(true);
            viewHolder.t_3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            return convertView;
        }
    }

    @OnClick(R.id.pageReturn)
    void onClickPageReturn(){
        activity.finish();
    }

}
