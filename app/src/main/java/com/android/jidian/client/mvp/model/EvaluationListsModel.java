package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.EvaluateListsBean;
import com.android.jidian.client.mvp.contract.EvaluationListsContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

public class EvaluationListsModel implements EvaluationListsContract.Model {

    @Override
    public Flowable<EvaluateListsBean> evaluateLists(int cabid, int page, String repair) {
        return RetrofitClient.getInstance().getApiService().evaluateLists(cabid, page, repair);
    }
}
