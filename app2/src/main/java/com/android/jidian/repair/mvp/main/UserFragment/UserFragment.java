package com.android.jidian.repair.mvp.main.UserFragment;

import static com.android.jidian.repair.mvp.main.PatrolFragment.PatrolFragmentEvent.LOCATION_SUCCESS;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseFragment;
import com.android.jidian.repair.base.BindEventBus;
import com.android.jidian.repair.mvp.H5.CommonWebViewActivity;
import com.android.jidian.repair.mvp.UserLog.OperationLogActivity;
import com.android.jidian.repair.mvp.addCab.AddCabActivity;
import com.android.jidian.repair.mvp.UserAuditCab.UserAuditCabActivity;
import com.android.jidian.repair.mvp.UserMyPatrol.patrolList.UserMyPatrolActivity;
import com.android.jidian.repair.mvp.UserTask.UserTaskList.UserTaskListActivity;
import com.android.jidian.repair.mvp.login.LoginActivity;
import com.android.jidian.repair.mvp.main.PatrolFragment.PatrolFragmentEvent;
import com.android.jidian.repair.mvp.message.UserMessageActivity;
import com.android.jidian.repair.utils.UserInfoHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@BindEventBus
public class UserFragment extends BaseFragment {

    @BindView(R.id.btn_logout)
    public TextView btnLogout;
    @BindView(R.id.ll_user_task)
    public LinearLayout llUserTask;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mLng;
    private String mLat;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user;
    }

    /**
     * 即时任务列表（有详情）
     * 已巡检列表（有详情）
     * 待审核电柜列表
     * <p>
     * 消息中心
     * 维修手册
     * 操作日志
     * 密码设置
     *
     * @param view view
     */
    @Override
    public void initView(View view) {

    }

    private boolean mHasPiosition = false;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(PatrolFragmentEvent event) {
        if (event != null) {
            if (event.getEvent() == LOCATION_SUCCESS) {
                if (!mHasPiosition) {
                    mLng = event.getLng();
                    mLat = event.getLat();
                    mHasPiosition = true;
                }
            }
        }
    }

    @OnClick(R.id.ll_user_operation_log)
    public void onClickllUserOperationLog() {
        Intent intent = new Intent(requireActivity(), OperationLogActivity.class);
        intent.putExtra(OperationLogActivity.TITLE, "操作日志");
        startActivity(intent);
    }

    @OnClick(R.id.ll_user_repaire_guide)
    public void onClickllUserRepaireGuide() {
        //故障与维修手册
        Intent intent = new Intent(requireActivity(), CommonWebViewActivity.class);
        intent.putExtra(CommonWebViewActivity.SETTITLE, "维修手册");
        intent.putExtra(CommonWebViewActivity.SETURL, "https://apex.mixiangx.com/User/manual.html");
        startActivity(intent);
    }

    @OnClick(R.id.ll_user_message)
    public void onClickllUserMessage() {
        startActivity(new Intent(requireActivity(), UserMessageActivity.class));
    }

    @OnClick(R.id.ll_user_add_cab)
    public void onClickllUserAddCab() {
        startActivity(new Intent(requireActivity(), AddCabActivity.class));
    }

    @OnClick(R.id.ll_user_partol)
    public void onClickllUserPartol() {
        Intent intent = new Intent(requireActivity(), UserMyPatrolActivity.class);
        intent.putExtra("lng", mLng);
        intent.putExtra("lat", mLat);
        startActivity(intent);
    }

    @OnClick(R.id.ll_user_audit)
    public void onClickllUserAudit() {
        startActivity(new Intent(requireActivity(), UserAuditCabActivity.class));
    }

    @OnClick(R.id.ll_user_task)
    public void onClickllUserTask() {
        startActivity(new Intent(requireActivity(), UserTaskListActivity.class));
    }

    @OnClick(R.id.btn_logout)
    public void onClickBtnLogout() {
        UserInfoHelper.getInstance().setUid("");
        UserInfoHelper.getInstance().setAccess("");
        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}