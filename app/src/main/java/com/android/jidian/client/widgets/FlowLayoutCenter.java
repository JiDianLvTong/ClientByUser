package com.android.jidian.client.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.android.jidian.client.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fight runningpig66@163.com
 */
public class FlowLayoutCenter extends ViewGroup {
    //存储行的集合，管理行
    private List<Line> mLines = new ArrayList<>();
    //水平和垂直间距，通过自定义属性获取
    private float vertical_space;
    private float horizontal_space;

    public FlowLayoutCenter(Context context) {
        this(context, null);
    }

    public FlowLayoutCenter(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        horizontal_space = array.getDimension(R.styleable.FlowLayout_width_space, 0);
        vertical_space = array.getDimension(R.styleable.FlowLayout_height_space, 0);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //每次测量之前都先清空集合，不然会覆盖掉以前
        mLines.clear();
        //当前行的指针
        Line mCurrentLine = null;
        //获取总宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        //计算最大可用的宽度，减去自定义view设置的左右padding值
        //行的最大宽度，除去边距的宽度
        int mMaxWidth = width - getPaddingLeft() - getPaddingRight();
        // ******************** 测量孩子 ********************
        //遍历获取孩子
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //测量孩子
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            //测量完需要将孩子添加到管理行的孩子的集合中，将行添加到管理行的集合中
            if (mCurrentLine == null) {
                //初次添加第一个孩子的时候
                mCurrentLine = new Line(mMaxWidth, horizontal_space);
                //添加孩子
                mCurrentLine.addView(childView);
                //添加行
                mLines.add(mCurrentLine);
            } else {
                //行中有孩子的时候，判断后才能添加
                if (mCurrentLine.canAddView(childView)) {
                    //行内的空闲width可以容纳该childView，继续往改行添加childView
                    mCurrentLine.addView(childView);
                } else {
                    //该行的width无法容纳childView，所以添加到下一行
                    mCurrentLine = new Line(mMaxWidth, horizontal_space);
                    mCurrentLine.addView(childView);
                    mLines.add(mCurrentLine);
                }
            }
        }//至此，mLines中记录了该自定义控件的所有需要绘制的所有行，mLines.size()就是行数
        // ******************** 测量自己 ********************
        //测量自己只需要计算高度，宽度肯定会被填充满
        int height = getPaddingTop() + getPaddingBottom();
        for (int i = 0; i < mLines.size(); i++) {
            //所有行的高度
            height += mLines.get(i).height;
        }
        //加上所有竖直的间距
        height += (mLines.size() - 1) * vertical_space;
        //测量
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        l = getPaddingLeft();
        t = getPaddingTop();
        for (int i = 0; i < mLines.size(); i++) {
            //获取行
            Line line = mLines.get(i);
            //行布局layout
            line.layout(l, t);
            //更新高度
            t += line.height;
            //如果不是最后一行就添加垂直间距
            if (i != mLines.size() - 1) {
                t += vertical_space;
            }
        }
    }

    /**
     * 内部类，行管理器，管理每一行的孩子
     */
    public class Line {
        //定义一个行集合来存放子View
        private List<View> views = new ArrayList<>();
        //行的最大宽度
        private int maxWidth;
        //行中已经使用的宽度
        private int usedWidth;
        //行的高度
        private int height;
        //孩子之间的距离
        private float space;

        //通过构造初始化最大宽度和水平边距
        Line(int maxWidth, float space) {
            this.maxWidth = maxWidth;
            this.space = space;
        }

        /**
         * 往集合中添加孩子view
         * 在添加前因为调用过canAddView()，所以可以保证该行一定可以容纳子view的宽度
         *
         * @param view 需要添加的子view
         */
        void addView(View view) {
            int childWidth = view.getMeasuredWidth();
            int childHeight = view.getMeasuredHeight();
            //更新行的使用宽度和高度
            if (views.size() == 0) {
                //集合里没有孩子的时候
                if (childWidth > maxWidth) {
                    usedWidth = maxWidth;
                } else {
                    usedWidth = childWidth;
                }
                height = childHeight;
            } else {
                usedWidth += space + childWidth;
                height = childHeight > height ? childHeight : height;
            }
            views.add(view);
        }

        /**
         * 判断当前行是否可以添加孩子view
         *
         * @param view 需要添加的子view
         * @return boolean
         */
        boolean canAddView(View view) {
            //集合里没有数据可以添加
            if (views.size() == 0) {
                return true;
            }
            //最后一个孩子的宽度大于剩余宽度就不添加
            return !(view.getMeasuredWidth() > (maxWidth - usedWidth - space));
        }

        /**
         * 指定孩子view显示的位置，布局孩子view
         *
         * @param left_ left
         * @param top_  top
         */
        void layout(int left_, int top_) {
            //该行左右平分空白区域
            int left = left_ + (maxWidth - usedWidth) / 2;
            //循环指定孩子位置
            for (View view : views) {
                //获取宽高
                int measuredWidth = view.getMeasuredWidth();
                int measuredHeight = view.getMeasuredHeight();
                int right = measuredWidth + left;
                int bottom = measuredHeight + top_;
                //指定位置
                view.layout(left, top_, right, bottom);
                //更新left
                left += measuredWidth + space;
            }
        }

    }

}
