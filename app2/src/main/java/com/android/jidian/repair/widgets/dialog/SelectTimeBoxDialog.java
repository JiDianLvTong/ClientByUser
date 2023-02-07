package com.android.jidian.repair.widgets.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.android.jidian.repair.R;
import com.android.jidian.repair.utils.DisplayUtils;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author : xiaoming
 * date: 2023/2/6 14:44
 * description:
 */
public class SelectTimeBoxDialog extends BaseDialog{

    private Date startDate = null;
    private TimePickerView pvTime;
    public OnDialogClickListener onDismissListener;
    private Context mContext;

    @BindView(R.id.framelayout)
    FrameLayout mFramelayout;
    @BindView(R.id.btn_select_time_sure)
    TextView btnSure;

    public SelectTimeBoxDialog(Activity activity, OnDialogClickListener listener) {
        super(activity.getApplicationContext());
        this.mContext = activity.getApplicationContext();
        this.onDismissListener = listener;
        setContentView(R.layout.common_dialog_select_year_month);
        init();
    }

    private void init() {
        if (pvTime != null && pvTime.isShowing()) {
            pvTime.dismiss();
        }
        startDate = new Date(System.currentTimeMillis());
        initTimePicker();
    }

    @OnClick({R.id.btn_select_time_sure, R.id.btn_select_time_cancel})
    public void onViewClicked(View v) {
        int id = v.getId();
        if (id == R.id.btn_select_time_sure) {
            dismiss();
            if (onDismissListener != null) {
                onDismissListener.onSure(DisplayUtils.getTime(startDate, "yyyy-MM"));
            }
        } else if (id == R.id.btn_select_time_cancel) {
            dismiss();
            if (onDismissListener != null) {
                onDismissListener.onDismiss();
            }
        }
    }

    private void initTimePicker() {
        try {
            Calendar startDateCale = Calendar.getInstance();
            Calendar endDateCale = Calendar.getInstance();
            startDateCale.set(2015, 0, 1);
            //选中事件回调
            pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                }
            }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                        @Override
                        public void onTimeSelectChanged(Date date) {
                            startDate = date;
                        }
                    }).setLayoutRes(R.layout.common_dialog_select_time_box, new CustomListener() {
                        @Override
                        public void customLayout(View v) {
                        }
                    }).setType(new boolean[]{true, true, false, false, false, false})
                    //设置空字符串以隐藏单位提示   hide label
                    .setLabel("年", "月", "", "", "", "")
                    .setDividerColor(ContextCompat.getColor(mContext, R.color.color_999999))
                    .setContentTextSize(20)
                    .setDate(endDateCale)
                    .setRangDate(startDateCale, endDateCale)
                    //非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中
                    .setDecorView(mFramelayout)
                    .setOutSideCancelable(false)
                    .build();
            if (pvTime != null && !pvTime.isShowing()) {
                //弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
                pvTime.show(btnSure, false);
            }
        } catch (Exception ignored) {
//            LogUtils.e("enen-error");
        }
    }

    public interface OnDialogClickListener {
        void onSure(String time);

        void onDismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
