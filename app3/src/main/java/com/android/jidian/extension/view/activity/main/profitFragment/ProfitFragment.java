package com.android.jidian.extension.view.activity.main.profitFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.jidian.extension.R;
import com.android.jidian.extension.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfitFragment extends BaseFragment {

    @BindView(R.id.p_1)
    public LinearLayout p_1;
    @BindView(R.id.p_2)
    public LinearLayout p_2;
    @BindView(R.id.t_1)
    public TextView t_1;
    @BindView(R.id.t_2)
    public TextView t_2;

    private Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_fragment_profit,container,false);
        unbinder = ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init(){

    }

    @OnClick({R.id.p_1, R.id.p_2})
    public void onClickFoot(LinearLayout layout) {
        if (layout.getId() == R.id.p_1) {
           t_1.setTextColor(0xffb69873);
           t_2.setTextColor(0xffffffff);
        } else if (layout.getId() == R.id.p_2) {
            t_2.setTextColor(0x00ffb69873);
            t_1.setTextColor(0xffffffff);
        }
    }

    public void setFragmentRefresh(){

    }


}
