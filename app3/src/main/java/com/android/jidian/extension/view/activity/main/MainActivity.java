package com.android.jidian.extension.view.activity.main;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.jidian.extension.R;
import com.android.jidian.extension.base.BaseActivity;
import com.android.jidian.extension.view.activity.main.extensionFragment.ExtensionFragment;
import com.android.jidian.extension.view.activity.main.profitFragment.ProfitFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.mainPanel)
    public LinearLayout mainPanel;

    @BindViews({R.id.t_1, R.id.t_2})
    public List<TextView> footTextViewList;

    @BindViews({R.id.i_1, R.id.i_2})
    public List<ImageView> footImageViewList;
    private int[] isSelectIcons = new int[]{R.drawable.ic_launcher_background, R.drawable.ic_launcher_background};
    private int[] notSelectIcons = new int[]{R.drawable.ic_launcher_background, R.drawable.ic_launcher_background};
    private int localPage = -1;

    private ExtensionFragment extensionFragment;
    private ProfitFragment profitFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(activity);
        initView();
    }

    private void initView(){
        //页卡设置
        changeMain(0);
    }

    @OnClick({R.id.p_1, R.id.p_2})
    public void onClickFoot(LinearLayout layout) {
        if (layout.getId() == R.id.p_1) {
            changeMain(0);
        } else if (layout.getId() == R.id.p_2) {
            changeMain(1);
        }
    }

    private void changeMain(int page) {
        if (page == localPage) {
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (extensionFragment != null) {
            ft.hide(extensionFragment);
        }
        if (profitFragment != null) {
            ft.hide(profitFragment);
        }
        for (int i = 0; i < footTextViewList.size(); i++) {
            footTextViewList.get(i).setTextColor(0xffffffff);
            footImageViewList.get(i).setImageResource(notSelectIcons[i]);
        }
        footTextViewList.get(page).setTextColor(activity.getResources().getColor(R.color.orange_b69873));
        footImageViewList.get(page).setImageResource(isSelectIcons[page]);
        if (page == 0) {
            if (extensionFragment == null) {
                extensionFragment = new ExtensionFragment();
                ft.add(R.id.mainPanel, extensionFragment);
            } else {
                ft.show(extensionFragment);
                extensionFragment.setFragmentRefresh();
            }
        } else if (page == 1) {
            if (profitFragment == null) {
                profitFragment = new ProfitFragment();
                ft.add(R.id.mainPanel, profitFragment);
            } else {
                ft.show(profitFragment);
                profitFragment.setFragmentRefresh();
            }
        }
        ft.commitAllowingStateLoss();
        localPage = page;
    }

}
