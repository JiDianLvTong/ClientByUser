package com.android.jidian.client.base;

import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.android.jidian.client.R;
import com.android.jidian.client.util.ViewUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author : PTT
 * date: 2020/7/24 10:56
 * company: 兴达智联
 * description:弹出框统一样式
 */
public abstract class AbstractXdzlDialog extends DialogFragment implements DialogInterface.OnKeyListener {
    private static final String MARGIN = "margin";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String DIM = "dim_amount";
    private static final String POSITION = "position";
    private static final String CANCEL = "out_cancel";
    private static final String KEYBACKCANCEL = "key_back_cancel";
    private static final String ANIM = "anim_style";
    private static final String LAYOUT = "layout_id";

    /**
     * 左右边距
     */
    private int margin;
    /**
     * 宽度
     */
    private int width;
    /**
     * 高度
     */
    private int height;
    private int heightPx, widthPx;
    private int offsetX, offsetY;
    /**
     * 灰度深浅
     */
    private float dimAmount = 0.5f;
    /**
     * 显示的位置
     */
    private int position;
    /**
     * 是否点击外部取消
     */
    private boolean outCancel = true;
    /**
     * 是否点击返回键取消
     */
    private boolean keyBackCancel = true;
    @StyleRes
    private int animStyle;
    @LayoutRes
    protected int layoutId;
    private Unbinder mBind;


    @Nullable
    @Override
    public View getView() {
        return mView;
    }

    public void setView(View view) {
        mView = view;
    }

    protected View mView;

    /**
     * 初始化布局
     *
     * @return id
     */
    public abstract int intLayoutId();

    /**
     * convertView
     *
     * @param holder holder
     * @param dialog dialog
     */
    public abstract void convertView(ViewHolder holder, AbstractXdzlDialog dialog);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialogPopStyle);
        layoutId = intLayoutId();
        //恢复保存的数据
        if (savedInstanceState != null) {
            margin = savedInstanceState.getInt(MARGIN);
            width = savedInstanceState.getInt(WIDTH);
            height = savedInstanceState.getInt(HEIGHT);
            dimAmount = savedInstanceState.getFloat(DIM);
            position = savedInstanceState.getInt(POSITION);
            outCancel = savedInstanceState.getBoolean(CANCEL);
            keyBackCancel = savedInstanceState.getBoolean(KEYBACKCANCEL);
            animStyle = savedInstanceState.getInt(ANIM);
            layoutId = savedInstanceState.getInt(LAYOUT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(layoutId, container, false);
        mBind = ButterKnife.bind(this, mView);
        convertView(ViewHolder.create(mView), this);
        this.getDialog().setOnKeyListener(this);
        return mView;
    }

    /**
     * 获取内容区域
     */
    private int getContextRect(Window window) {
        //应用区域
        Rect outRect1 = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(outRect1);
        return outRect1.height();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBind != null) {
            mBind.unbind();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }


    public void showDialog(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commit();
        } catch (IllegalStateException e) {
            Log.d("ABSDIALOGFRAG", "Exception", e);
        }
    }

    /**
     * 屏幕旋转等导致DialogFragment销毁后重建时保存数据
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MARGIN, margin);
        outState.putInt(WIDTH, width);
        outState.putInt(HEIGHT, height);
        outState.putFloat(DIM, dimAmount);
        outState.putInt(POSITION, position);
        outState.putBoolean(CANCEL, outCancel);
        outState.putBoolean(KEYBACKCANCEL, keyBackCancel);
        outState.putInt(ANIM, animStyle);
        outState.putInt(LAYOUT, layoutId);
    }


    private void initParams() {
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            //调节灰色背景透明度[0-1]，默认0.5f
            lp.dimAmount = dimAmount;
            lp.gravity = position;
            if (position == Gravity.BOTTOM) {
                if (animStyle == 0) {
                    animStyle = R.style.DefaultAnimation;
                }
            }

            //设置dialog宽度
            if (width == 0) {
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            } else if (width == -1) {
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            } else if (width == 1) {
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            } else if (width == 1550) {
                lp.width = 1550;
            } else {
                if (getContext() != null) {
                    lp.width = ViewUtil.dp2px(getContext(), width);
                }
            }

            //设置dialog高度
            if (height == 0) {
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            } else if (height == 1017) {
                lp.height = 1017;
            } else if (height == 1) {
                int dialogHeight = getContextRect(window);
                lp.height = dialogHeight == 0 ? WindowManager.LayoutParams.MATCH_PARENT : dialogHeight;
            } else if (height == 737) {
                lp.height = 737;
            } else {
                if (getContext() != null) {
                    lp.height = ViewUtil.dp2px(getContext(), height);
                }
            }
            //修复对话框显示问题，设置height,width,未初始化密度为0 导致lp.height =0
            if (widthPx != 0) {
                lp.width = widthPx;
            }
            if (heightPx != 0) {
                lp.height = heightPx;
            }
            if (offsetY != 0) {
                lp.y = offsetY;
            }

            //设置dialog进入、退出的动画
            window.setWindowAnimations(animStyle);
            window.setAttributes(lp);
        }
        setCancelable(outCancel);
    }

    public AbstractXdzlDialog setHeightPx(int heightPx) {
        this.heightPx = heightPx;
        return this;
    }

    public AbstractXdzlDialog setWidthPx(int widthPx) {
        this.widthPx = widthPx;
        return this;
    }

    public AbstractXdzlDialog setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    public AbstractXdzlDialog setWidth(int width) {
        this.width = width;
        return this;
    }

    public AbstractXdzlDialog setHeight(int height) {
        this.height = height;
        return this;
    }

    public AbstractXdzlDialog setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

    public AbstractXdzlDialog setPosition(int position) {
        this.position = position;
        return this;
    }

    public AbstractXdzlDialog setOutCancel(boolean outCancel) {
        this.outCancel = outCancel;
        return this;
    }

    public AbstractXdzlDialog setKeyBackCancel(boolean keyBackCancel) {
        this.keyBackCancel = keyBackCancel;
        return this;
    }

    public AbstractXdzlDialog setAnimStyle(@StyleRes int animStyle) {
        this.animStyle = animStyle;
        return this;
    }

    public AbstractXdzlDialog setOffsetY(int offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public AbstractXdzlDialog show(FragmentManager manager) {
        //TODO ！！！！！！！注意此处，dialogfragment，
        // 不同的dialog有不同的tag，相同的dialog有相同的tag！！！！！！！
        String tag = getClass().getSimpleName();
        if (!isAdded()) {
            if (manager.findFragmentByTag(tag) == null) {
                FragmentTransaction ft = manager.beginTransaction();
                ft.add(this, tag);
                ft.commitAllowingStateLoss();
            }
        }
        return this;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyBackCancel) {
                dismiss();
            }
            return true;
        } else {
            //这里注意当不是返回键时需将事件扩散，否则无法处理其他点击事件
            return false;
        }
    }
}