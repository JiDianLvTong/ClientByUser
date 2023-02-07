package com.android.jidian.repair.widgets.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.jidian.repair.R;
import com.bumptech.glide.Glide;

import butterknife.BindView;

/**
 * @author : xiaoming
 * date: 2023/1/11 16:53
 * description:
 */
public class TakePhotoDialog extends BaseDialog {

    @BindView(R.id.iv_example)
    public ImageView ivExample;
    @BindView(R.id.btn_take_photo)
    public ConstraintLayout btnTakePhoto;
    private OnDialogClickListener mListener;
    private Context mContext;

    public TakePhotoDialog(Activity activity, OnDialogClickListener listener) {
        super(activity.getApplicationContext());
        this.mContext = activity.getApplicationContext();
        this.mListener = listener;
        setContentView(R.layout.dialog_take_photo);
        init();
    }

    private void init() {
        Glide.with(mContext).load(R.drawable.ic_launcher_background).into(ivExample);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onTakePhoto();
                }
            }
        });
    }

    public interface OnDialogClickListener {
        void onTakePhoto();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
