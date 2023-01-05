package com.android.jidian.client.mvp.contract;

import com.android.jidian.client.bean.CabinetDetailBean;
import com.android.jidian.client.base.BaseView;
import com.android.jidian.client.bean.LoginCheckAccv2Bean;

import io.reactivex.Flowable;

public interface MainContract {
    interface Model {
        Flowable<CabinetDetailBean> cabinetDeatil(int cabid, String repair, String jingdu, String weidu);
        Flowable<LoginCheckAccv2Bean> requestCheckAccv2(String apptoken, String appsn);
    }

    interface View extends BaseView {
        void onSuccess(CabinetDetailBean bean);

        void requestCheckAccv2Success(LoginCheckAccv2Bean bean);

        void requestCheckAccv2Error(String msg);
    }

    interface Presenter {
        void cabinetDeatil(int cabid, String repair, String jingdu, String weidu);

        void requestCheckAccv2(String apptoken, String appsn);
    }
}