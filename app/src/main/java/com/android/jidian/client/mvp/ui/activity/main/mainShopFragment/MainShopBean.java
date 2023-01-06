package com.android.jidian.client.mvp.ui.activity.main.mainShopFragment;

public class MainShopBean {

    private String MainShopDataType = "";
    private Object ObjectBean = null;

    public MainShopBean(String MainShopDataType , Object ObjectBean){
        this.MainShopDataType = MainShopDataType;
        this.ObjectBean = ObjectBean;
    };

    public String getMainShopDataType() {
        return MainShopDataType;
    }

    public void setMainShopDataType(String mainShopDataType) {
        MainShopDataType = mainShopDataType;
    }

    public Object getObjectBean() {
        return ObjectBean;
    }

    public void setObjectBean(Object objectBean) {
        ObjectBean = objectBean;
    }
}
