package com.android.jidian.client.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.android.jidian.client.R;
import com.android.jidian.client.mvp.bean.ChargeSiteMapBean;

import java.util.List;

public class MakerUtils {

    public BitmapDescriptor localMarker(Context context){

        View localView = LayoutInflater.from(context).inflate(R.layout.activity_charge_site_map_loacal_view, null);
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        localView.measure(width, width);
        localView.layout(0, 0, localView.getMeasuredWidth(), localView.getMeasuredHeight());
        localView.setDrawingCacheEnabled(true);
        localView.buildDrawingCache();
        BitmapDescriptor bitmapDescriptor =  BitmapDescriptorFactory.fromBitmap(localView.getDrawingCache());
        return bitmapDescriptor;
    }

    public BitmapDescriptor chargeSiteMarker(Activity activity , List<ChargeSiteMapBean.DataBean.ListBean> items) {

        int batteryCount = 0 ;
        int cabCount = items.size();
        for(int i = 0 ; i < cabCount ; i ++){
            ChargeSiteMapBean.DataBean.ListBean mapSiteItem = items.get(i);
            batteryCount = batteryCount + Integer.parseInt(mapSiteItem.getBattery_num());
        }

        View markerView = LayoutInflater.from(activity).inflate(R.layout.activity_charge_site_map_marker_view, null);
        TextView t_1 = markerView.findViewById(R.id.t_1);
        TextView t_2 = markerView.findViewById(R.id.t_2);
        RelativeLayout l_2 = markerView.findViewById(R.id.l_2);
        t_1.setText(batteryCount+"");
        if(cabCount > 1){
            t_2.setText(cabCount+"");
            l_2.setVisibility(View.VISIBLE);
        }

        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        markerView.measure(width, width);
        markerView.layout(0, 0, markerView.getMeasuredWidth(), markerView.getMeasuredHeight());
        markerView.setDrawingCacheEnabled(true);
        markerView.buildDrawingCache();

        BitmapDescriptor bitmapDescriptor =  BitmapDescriptorFactory.fromBitmap(markerView.getDrawingCache());
        return bitmapDescriptor;

    }

}
