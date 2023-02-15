package com.android.jidian.repair.mvp.main.PatrolFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.View;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseFragmentByMvp;
import com.android.jidian.repair.mvp.main.PatrolFragment.hasPartol.MyPartolFragment;
import com.android.jidian.repair.mvp.main.PatrolFragment.noPartol.PatrolFragment;
import com.android.jidian.repair.mvp.user.userMyPatrol.patrolDetail.MyPartolDetailContract;
import com.android.jidian.repair.mvp.user.userMyPatrol.patrolDetail.MyPartolDetailPresenter;
import com.android.jidian.repair.mvp.user.userMyPatrol.patrolDetail.PatrolDetailBean;
import com.android.jidian.repair.widgets.ViewPagerAdapter;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainPartolFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPartolFragment extends BaseFragmentByMvp<MyPartolDetailPresenter> implements MyPartolDetailContract.View {

    @BindView(R.id.advices_lists_tabLayout)
    SlidingTabLayout advicesListsTabLayout;
    @BindView(R.id.advices_lists_viewPager)
    ViewPager advicesListsViewPager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainPartolFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainPartolFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainPartolFragment newInstance(String param1, String param2) {
        MainPartolFragment fragment = new MainPartolFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_partol;
    }

    public void setFragmentRefresh() {
        if (advicesListsViewPager != null) {
            if (advicesListsViewPager.getCurrentItem() == 0) {
                ((PatrolFragment)mFragmentList.get(0)).setFragmentRefresh();
            }else {
                ((MyPartolFragment)mFragmentList.get(1)).setFragmentRefresh();
            }
        }
    }

    private List<Fragment> mFragmentList;
    @Override
    public void initView(View view) {
        String[] mTabs = new String[]{"巡检列表", "已巡检"};
        mFragmentList = new ArrayList<>();
        mFragmentList.add(PatrolFragment.newInstance(mParam1,mParam2));
        mFragmentList.add(MyPartolFragment.newInstance(mParam1,mParam2));
        ViewPagerAdapter adapter = new ViewPagerAdapter(requireActivity().getSupportFragmentManager(), mFragmentList, mTabs);
        advicesListsViewPager.setAdapter(adapter);
        advicesListsViewPager.setOffscreenPageLimit(1);
        advicesListsTabLayout.setViewPager(advicesListsViewPager, mTabs);
        advicesListsViewPager.setCurrentItem(0, false);
        advicesListsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void requestPatrolDetailSuccess(PatrolDetailBean bean) {

    }

    @Override
    public void requestShowTips(String msg) {

    }
}