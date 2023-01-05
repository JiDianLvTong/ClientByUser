package com.android.jidian.client.mvp.model;

import com.android.jidian.client.bean.EvaluateAddsBean;
import com.android.jidian.client.bean.EvaluateLabsBean;
import com.android.jidian.client.mvp.contract.EvaluateAddsContract;
import com.android.jidian.client.net.RetrofitClient;

import io.reactivex.Flowable;

public class EvaluateAddsModel implements EvaluateAddsContract.Model {
    @Override
    public Flowable<EvaluateAddsBean> evaluateAdds(int cabid, int stars, String labids, String content, String repair) {
        return RetrofitClient.getInstance().getApiService().evaluateAdds(cabid, stars, labids, content, repair);
    }

    @Override
    public Flowable<EvaluateLabsBean> evaluateLabs(int cabid, String repair) {
        return RetrofitClient.getInstance().getApiService().evaluateLabs(cabid, repair);
    }
}
