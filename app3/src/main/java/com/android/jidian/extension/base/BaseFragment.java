package com.android.jidian.extension.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.jidian.extension.view.commonPlug.dialog.DialogByLoading;

public class BaseFragment extends Fragment {

    protected View view;
    protected DialogByLoading dialogLoading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        dialogLoading = new DialogByLoading(getActivity());
    }

}
