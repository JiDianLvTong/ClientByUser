package com.android.jidian.repair.mvp.patrol;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.net.RetrofitClient;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/2/1 17:05
 * description:
 */
public class PatrolDetailModel implements PatrolDetailContract.Model {
    @Override
    public Flowable<BaseBean> requestPatrolAddpatrol(String cabid, String img1, String img2, String img3, String img4_1, String img4_2, String img4_3, String img4_4) {
        return RetrofitClient.getInstance().getApeService().requestPatrolAddpatrol(cabid, img1, img2, img3, img4_1, img4_2, img4_3, img4_4);
    }
}
