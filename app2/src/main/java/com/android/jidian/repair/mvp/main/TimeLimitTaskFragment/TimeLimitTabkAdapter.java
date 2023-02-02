package com.android.jidian.repair.mvp.main.TimeLimitTaskFragment;

import android.view.View;

import com.android.jidian.repair.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author : xiaoming
 * date: 2023/1/12 14:02
 * description:
 */
public class TimeLimitTabkAdapter extends BaseQuickAdapter<WorktaskListsBean.DataBean.ListsBean, BaseViewHolder> {

    private OnItemViewClickListener mListener;

    public TimeLimitTabkAdapter() {
        super(R.layout.item_time_task);
    }

    public void setListener(OnItemViewClickListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder holder, WorktaskListsBean.DataBean.ListsBean bean) {
        if ("10".equals(bean.getWtype())) {//电柜故障
            holder.setText(R.id.tv_task_item_type, "电柜故障")
                    .setText(R.id.tv_task_item_num, "电柜编号:" + bean.getCabid())
                    .setText(R.id.tv_task_item_address, "地址:" + bean.getAddress())
                    .setText(R.id.tv_test_item_content, bean.getContent());
            holder.getView(R.id.tv_task_item_guide).setVisibility(View.VISIBLE);
            holder.getView(R.id.tv_task_item_address).setVisibility(View.VISIBLE);
        } else if ("20".equals(bean.getWtype())) {//救援任务
            holder.setText(R.id.tv_task_item_type, "救援任务")
                    .setText(R.id.tv_task_item_num, "手机号码：" + bean.getPhone())
                    .setText(R.id.tv_test_item_content, bean.getContent());
            holder.getView(R.id.tv_task_item_guide).setVisibility(View.GONE);
            holder.getView(R.id.tv_task_item_address).setVisibility(View.GONE);
        } else {//其他任务
            holder.setText(R.id.tv_task_item_type, "其他任务")
                    .setText(R.id.tv_task_item_num, bean.getTitle())
                    .setText(R.id.tv_test_item_content, bean.getContent());
            holder.getView(R.id.tv_task_item_guide).setVisibility(View.GONE);
            holder.getView(R.id.tv_task_item_address).setVisibility(View.GONE);
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
        void OnClickGuide(WorktaskListsBean.DataBean.ListsBean bean);

        void OnClickPhone(WorktaskListsBean.DataBean.ListsBean bean);

        void OnClickItem(WorktaskListsBean.DataBean.ListsBean bean);
    }
}
