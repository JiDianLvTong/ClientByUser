package com.android.jidian.client.mvp.ui.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.jidian.client.R;

/**
 * @author : xiaoming
 * date: 2021/3/17 上午11:37
 * company: 兴达智联
 * description:
 */
public class BluetoothDialogFragment extends DialogFragment implements View.OnClickListener {

    private OnDialogItemClickListener listener;
    private String title;

    public static BluetoothDialogFragment newInstance() {
        return new BluetoothDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alertdialog_logout, container, false);
        TextView success = view.findViewById(R.id.success);
        TextView error = view.findViewById(R.id.error);
        TextView tv_text = view.findViewById(R.id.tv_text);
        tv_text.setText(title);
        success.setOnClickListener(this);
        error.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            switch (v.getId()) {
                case R.id.success:
                    listener.onSuccessClick();
                    dismiss();
                    break;
                case R.id.error:
                    listener.onErrorClick();
                    dismiss();
                    break;
            }
        }
    }

    public void setOnDialogItemClickListener(OnDialogItemClickListener listener) {
        this.listener = listener;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public interface OnDialogItemClickListener {
        void onSuccessClick();

        void onErrorClick();
    }

}
