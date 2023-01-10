package com.android.jidian.repair.net;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava线程调度，统一线程处理
 *
 * @author fight runningpig66@163.com
 * 2019/10/25
 */
public class RxScheduler {
    public static <T> FlowableTransformer<T, T> Flo_io_main() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<T, T> Obs_io_main() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}