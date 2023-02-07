package com.android.jidian.repair.mvp.main.UserFragment;

import static com.android.jidian.repair.mvp.main.PatrolFragment.noPartol.PatrolFragmentEvent.LOCATION_SUCCESS;

import android.content.Intent;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseFragment;
import com.android.jidian.repair.base.BindEventBus;
import com.android.jidian.repair.dao.sp.UserInfoSp;
import com.android.jidian.repair.mvp.H5.CommonWebViewActivity;
import com.android.jidian.repair.mvp.user.userLog.OperationLogActivity;
import com.android.jidian.repair.mvp.cabinet.cabAddCab.AddCabActivity;
import com.android.jidian.repair.mvp.cabinet.cabAuditCab.UserAuditCabActivity;
import com.android.jidian.repair.mvp.user.userMyPatrol.patrolList.UserMyPatrolActivity;
import com.android.jidian.repair.mvp.task.userTask.UserTaskList.UserTaskListActivity;
import com.android.jidian.repair.mvp.user.userMessage.UserMessageActivity;
import com.android.jidian.repair.mvp.user.userInfo.UserInfoActivity;

import butterknife.BindView;
import butterknife.OnClick;

@BindEventBus
public class UserFragment extends BaseFragment {

    @BindView(R.id.ll_user_task)
    public LinearLayout llUserTask;
    @BindView(R.id.userName)
    public TextView userName;
    @BindView(R.id.userHead)
    public ImageView userHead;
    @BindView(R.id.userInfoPanel)
    public LinearLayout userInfoPanel;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    public void initView(View view) {
        userName.setText(UserInfoSp.getInstance().getName());
    }

    @OnClick(R.id.userInfoPanel)
    public void onClickUserInfoPanel(){
        getActivity().startActivity(new Intent(getActivity() , UserInfoActivity.class));
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
}