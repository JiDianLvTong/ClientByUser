package com.android.jidian.repair.mvp.main.UserFragment;

import com.android.jidian.repair.mvp.main.PatrolFragment.noPartol.PatrolIndexBean;

import io.reactivex.Flowable;

/**
 * @author : xiaoming
 * date: 2023/1/13 11:54
 * description:
 */
public class UserModel implements UserContract.Model{
    @Override
    public Flowable<PatrolIndexBean> requestPatrolIndex(String lng, String lat, String page) {
        return null;
    }
}
