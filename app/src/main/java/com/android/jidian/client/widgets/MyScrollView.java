package com.android.jidian.client.widgets;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.jidian.client.R;

/**
 * modified by fight runningpig66@163.com 20191108
 */
public class MyScrollView extends ScrollView {
    private MyScrollView myScrollView;
    private Context context;
    private DisplayMetrics dm;
    private Point size;
    private int contentHeight;//this的高度
    private int type = 0;
    private static final int DEFAULT_DETAIL_HEIGHT = 275;//站点详情默认展示的高度

    public MyScrollView(Context context) {
        this(context, null);
        this.context = context;
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void init() {
        size = new Point();
        dm = new DisplayMetrics();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        myScrollView = this;
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        assert windowManager != null;
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        display.getSize(size);
        //contentHeight变量标记的高度=屏幕高度-状态栏高度-title的45dp高度（title就是吉电出行那个标题栏），也就是剩余可用的高度
        contentHeight = (int) (size.y - statusBarHeight1 - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, dm));
        FrameLayout view_1 = findViewById(R.id.panel_1);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view_1.getLayoutParams();
        //panel_1的高度为上面计算你的剩余可用高度-300dp，这里300dp就是siteinfo_layout默认显示的高度
        params.height = (int) (contentHeight - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_DETAIL_HEIGHT, dm));
        view_1.setLayoutParams(params);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int y = (int) ev.getRawY();
        LinearLayout view = (LinearLayout) this.getChildAt(0);
        FrameLayout view_1 = (FrameLayout) view.getChildAt(0);//panel_1
        int[] location = new int[2];
        view_1.getLocationOnScreen(location);
        int view_1_bottom = location[1];
        //这里应该减去实际的siteinfo_layout默认显示的高度，也就是DEFAULT_DETAIL_HEIGHT
        if (y < view_1_bottom + (contentHeight - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_DETAIL_HEIGHT, dm))) {
            return false;
        } else {
            return super.onTouchEvent(ev);//ScrollView拦截事件
        }
    }

    //点击marker
    public void A_1() {
        //scroollView已经展示时再次点击某个marker会执行这里
        if (type == 1) {//type标记scrollView是否“不可见”，这里的“不可见”仅仅表示scrollView是否已经向下移动了300单位，type为1时表示scrollView可见
            //下面的Y轴平移表示：将scroolView从0移动到300单位，也就是向下移动300单位，这300单位正好是默认情况下的panel_1展示的大小
            //这意味着，如果panel_1是默认展示300dp，那么它将向下移动直至不可见，效果就是scrollView“向下移动隐藏”，CLOSE
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "translationY", 0, dip2px(context, DEFAULT_DETAIL_HEIGHT));
            objectAnimator.setDuration(200);
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    A_1_open();//CLOSE之后再OPEN
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            objectAnimator.start();
        } else {//在scrollView未展示时，点击marker会执行这里，OPEN
            A_1_open();
        }
    }

    public void A_1_open() {
        //将scrollView向上移动300单位回到原来的位置，效果就是scrollView“向上移动展现”
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "translationY", dip2px(context, DEFAULT_DETAIL_HEIGHT), 0);
        objectAnimator.setDuration(200);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                myScrollView.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                myScrollView.post(() -> myScrollView.fullScroll(ScrollView.FOCUS_DOWN));
                type = 1;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        objectAnimator.start();
        myScrollView.post(() -> myScrollView.fullScroll(ScrollView.FOCUS_DOWN));
    }

    public void A_1_close() {
        if (type == 1) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "translationY", 0, dip2px(context, DEFAULT_DETAIL_HEIGHT));
            objectAnimator.setDuration(200);
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    myScrollView.setVisibility(INVISIBLE);
                    myScrollView.post(() -> myScrollView.fullScroll(ScrollView.FOCUS_UP));
                    type = 0;
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            objectAnimator.start();
        }
    }
}