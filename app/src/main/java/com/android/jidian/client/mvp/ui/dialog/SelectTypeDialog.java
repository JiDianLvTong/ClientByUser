package com.android.jidian.client.mvp.ui.dialog;

import android.text.TextUtils;

import com.android.jidian.client.bean.DialogListBean;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.view.WheelView;
import com.android.jidian.client.R;
import com.android.jidian.client.base.AbstractXdzlDialog;
import com.android.jidian.client.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : PTT
 * date: 2021/09/27 13:56
 * company: 兴达智联
 * description: 滚轴选择框
 */
public class SelectTypeDialog extends AbstractXdzlDialog {
    public OnDismissListener onDismissListener;
    private DialogListBean selectItem = null;
    private List<DialogListBean> mOptionsItems;
    private String mSelectValue = "";

    public SelectTypeDialog init(List<DialogListBean> list, OnDismissListener listener) {
        this.mOptionsItems = list;
        this.onDismissListener = listener;
        return this;
    }

    public SelectTypeDialog init(String selectValue, List<DialogListBean> list, OnDismissListener listener) {
        this.mSelectValue = selectValue;
        this.mOptionsItems = list;
        this.onDismissListener = listener;
        return this;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_select_type_layout;
    }

    @Override
    public void convertView(ViewHolder holder, AbstractXdzlDialog dialog) {
        if (mOptionsItems != null) {
            if (mOptionsItems.size() > 0) {
                selectItem = mOptionsItems.get(0);
                WheelView wvSelectSiteType = holder.getView(R.id.wheelview_select_site_type);
                wvSelectSiteType.setCyclic(false);
                List<String> list = new ArrayList<>();
                int selectPosition = 0;
                for (int i = 0; i < mOptionsItems.size(); i++) {
                    list.add(mOptionsItems.get(i).getName());
                    if (!TextUtils.isEmpty(mSelectValue)) {
                        if (mSelectValue.equals(mOptionsItems.get(i).getValue())) {
                            selectPosition = i;
                            selectItem = mOptionsItems.get(i);
                        }
                    }
                }
                wvSelectSiteType.setAdapter(new ArrayWheelAdapter(list));
                wvSelectSiteType.setTextSize(20f);
                wvSelectSiteType.setCurrentItem(selectPosition);
                wvSelectSiteType.setOnItemSelectedListener(index -> {
                    selectItem = mOptionsItems.get(index);
                });

                holder.getView(R.id.btn_select_type_cancel).setOnClickListener(view -> {
                    dismiss();
                });
                holder.getView(R.id.btn_select_type_sure).setOnClickListener(view -> {
                    dismiss();
                    if (onDismissListener != null) {
                        onDismissListener.onSure(selectItem);
                    }
                });
            }
        }
    }

    public interface OnDismissListener {
        void onSure(DialogListBean item);
    }
}