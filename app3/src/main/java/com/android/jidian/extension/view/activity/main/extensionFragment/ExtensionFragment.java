package com.android.jidian.extension.view.activity.main.extensionFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.jidian.extension.R;
import com.android.jidian.extension.base.BaseFragment;
import com.android.jidian.extension.view.activity.main.profitFragment.ProfitFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ExtensionFragment extends BaseFragment {

    private Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_fragment_extension,container,false);
        unbinder = ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init(){

    }

    public void setFragmentRefresh(){

    }

}
