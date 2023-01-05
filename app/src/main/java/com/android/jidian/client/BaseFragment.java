package com.android.jidian.client;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jidian.client.widgets.ProgressDialog;

public class BaseFragment extends Fragment {

    protected ProgressDialog progressDialog;
    protected SharedPreferences sharedPreferences;
    protected String uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("BaseActivity", getClass().getSimpleName());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void initParam() {
        sharedPreferences = requireActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        uid = sharedPreferences.getString("id", "");
        progressDialog = new ProgressDialog(requireActivity());
    }
}
