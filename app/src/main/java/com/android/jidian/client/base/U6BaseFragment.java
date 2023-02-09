package com.android.jidian.client.base;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jidian.client.mvp.ui.dialog.DialogByLoading;
import com.android.jidian.client.widgets.MyToast;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class U6BaseFragment extends Fragment{

    protected DialogByLoading progressDialog;
    protected Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        progressDialog = new DialogByLoading(requireActivity());
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
    }

    /**
     * 显示消息
     */
    protected void showMessage(String msg) {
        MyToast.showTheToast(requireActivity(), msg);
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
}