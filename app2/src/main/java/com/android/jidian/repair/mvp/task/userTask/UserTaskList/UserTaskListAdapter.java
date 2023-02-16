package com.android.jidian.repair.mvp.task.userTask.UserTaskList;

import android.view.View;

import com.android.jidian.repair.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author : xiaoming
 * date: 2023/1/12 14:02
 * description:
 */
public class UserTaskListAdapter extends BaseQuickAdapter<WorktaskMylistsBean.DataBean.ListsBean, BaseViewHolder> {

    private OnItemViewClickListener mListener;

    public UserTaskListAdapter() {
        super(R.layout.item_user_time_task);
    }

    public void setListener(OnItemViewClickListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder holder, WorktaskMylistsBean.DataBean.ListsBean bean) {
        if ("10".equals(bean.getWtype())) {//电柜故障
            holder.setText(R.id.tv_task_item_num, "电柜编号:" + bean.getCabid())
                    .setText(R.id.tv_task_item_address, "地址:" + bean.getAddress())
                    .setText(R.id.tv_task_item_date_1, "创建时间: " +  bean.getCreate_time())
                    .setText(R.id.tv_test_item_content, bean.getContent());
            holder.getView(R.id.tv_task_item_guide).setVisibility(View.VISIBLE);
            holder.getView(R.id.tv_task_item_address).setVisibility(View.VISIBLE);
            if(bean.getUstatus().equals("1")){
                holder.getView(R.id.tv_task_item_type).setBackgroundResource(R.drawable.shape_d7a64a_corner_top_right_10);
                holder.setText(R.id.tv_task_item_type, "电柜故障: 已完成");
            }else{
                holder.getView(R.id.tv_task_item_type).setBackgroundResource(R.drawable.shape_cccccc_corner_top_right_10);
                holder.setText(R.id.tv_task_item_type, "电柜故障: 无法完成");
            }
        } else if ("20".equals(bean.getWtype())) {//救援任务
            holder.setText(R.id.tv_task_item_num, "手机号码：" + bean.getPhone())
                    .setText(R.id.tv_task_item_date_1, "创建时间: " +  bean.getCreate_time())
                    .setText(R.id.tv_test_item_content, bean.getContent());
            holder.getView(R.id.tv_task_item_guide).setVisibility(View.GONE);
            holder.getView(R.id.tv_task_item_address).setVisibility(View.GONE);
            if(bean.getUstatus().equals("1")){
                holder.getView(R.id.tv_task_item_type).setBackgroundResource(R.drawable.shape_d7a64a_corner_top_right_10);
                holder.setText(R.id.tv_task_item_type, "救援任务: 已完成");
            }else{
                holder.getView(R.id.tv_task_item_type).setBackgroundResource(R.drawable.shape_cccccc_corner_top_right_10);
                holder.setText(R.id.tv_task_item_type, "救援任务: 未完成");
            }
        } else {//其他任务
            holder.setText(R.id.tv_task_item_num, bean.getTitle())
                    .setText(R.id.tv_task_item_date_1, "创建时间: " +  bean.getCreate_time())
                    .setText(R.id.tv_test_item_content, bean.getContent());
            holder.getView(R.id.tv_task_item_guide).setVisibility(View.GONE);
            holder.getView(R.id.tv_task_item_address).setVisibility(View.GONE);
            if(bean.getUstatus().equals("1")){
                holder.getView(R.id.tv_task_item_type).setBackgroundResource(R.drawable.shape_d7a64a_corner_top_right_10);
                holder.setText(R.id.tv_task_item_type, "其他任务: 已完成");
            }else{
                holder.getView(R.id.tv_task_item_type).setBackgroundResource(R.drawable.shape_cccccc_corner_top_right_10);
                holder.setText(R.id.tv_task_item_type, "其他任务: 未完成");
            }
        }
        holder.getView(R.id.tv_task_item_guide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.OnClickGuide(bean);
                }
            }
        });
        holder.getView(R.id.tv_task_item_num).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    if ("20".equals(bean.getWtype())) {
                        mListener.OnClickPhone(bean);
                    }
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.OnClickItem(bean);
                }
            }
        });
    }

    public interface OnItemViewClickListener {
        void OnClickGuide(WorktaskMylistsBean.DataBean.ListsBean bean);

        void OnClickPhone(WorktaskMylistsBean.DataBean.ListsBean bean);

        void OnClickItem(WorktaskMylistsBean.DataBean.ListsBean bean);
    }
}
