package com.android.jidian.repair.dialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;

import butterknife.ButterKnife;
import razerdp.basepopup.BasePopupWindow;

public class BaseDialog extends BasePopupWindow {

    public BaseDialog(Context context) {
        super(context);
        setClipChildren(false);
    }

    @Override
    protected Animator onCreateShowAnimator() {
        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(getDisplayAnimateView(), View.TRANSLATION_Y, getHeight() * 0.25f, 0);
        showAnimator.setDuration(500);
        showAnimator.setInterpolator(new OvershootInterpolator(6));
        return showAnimator;
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        ButterKnife.bind(this, contentView);
    }
}
