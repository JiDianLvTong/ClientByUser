package com.android.jidian.repair.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.jidian.repair.widgets.dialog.DialogByLoading;
import com.android.jidian.repair.widgets.dialog.DialogByToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment{

    protected DialogByLoading progressDialog;
    protected Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        progressDialog = new DialogByLoading(requireActivity());
        //判断是否需要注册EventBus
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 显示消息
     */
    protected void showMessage(String msg) {
        DialogByToast.showTheToast(requireActivity(), msg);
    }

    /**
     * 获取布局id
     *
     * @return layout id
     */
    public abstract int getLayoutId();

    /**
     * 初始化视图
     *
     * @param view view
     */
    public abstract void initView(View view);

    /**
     * 默认绑定一个事件，防止源码里面去找方法的时候找不到报错。
     */
    @Subscribe
    public void onEvent(BaseFragment activity) {
    }
}