package com.android.jidian.client.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.jidian.client.R;
import com.android.jidian.client.util.ViewUtil;

public class MyLinesEditView extends LinearLayout {
    private Context mContext;
    private EditText id_et_input;
    private TextView id_tv_input;
    private boolean isInputAble = true;

    private int MAX_COUNT;
    private boolean ignoreCnOrEn;
    private boolean showPositive;
    private String hintText;
    private int hintTextColor;
    private String contentText;
    private int contentTextSize;
    private int contentTextColor;
    private float contentViewHeight;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isInputAble) {
            return super.onInterceptTouchEvent(ev);
        }
        return true;
    }

    public MyLinesEditView(Context context) {
        this(context, null);
    }

    public MyLinesEditView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinesEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyLinesEditView);
        MAX_COUNT = typedArray.getInteger(R.styleable.MyLinesEditView_classic_maxCount, 50);
        ignoreCnOrEn = typedArray.getBoolean(R.styleable.MyLinesEditView_classic_ignoreCnOrEn, true);
        showPositive = typedArray.getBoolean(R.styleable.MyLinesEditView_classic_showPositive, true);
        hintText = typedArray.getString(R.styleable.MyLinesEditView_classic_hintText);
        hintTextColor = typedArray.getColor(R.styleable.MyLinesEditView_classic_hintTextColor, Color.parseColor("#42000000"));
        contentText = typedArray.getString(R.styleable.MyLinesEditView_classic_contentText);
        contentTextSize = typedArray.getDimensionPixelSize(R.styleable.MyLinesEditView_classic_contentTextSize, 11);
        contentTextColor = typedArray.getColor(R.styleable.MyLinesEditView_classic_contentTextColor, Color.parseColor("#8A000000"));
        contentViewHeight = typedArray.getDimensionPixelSize(R.styleable.MyLinesEditView_classic_contentViewHeight, ViewUtil.dp2px(context, 170));
        typedArray.recycle();
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_lines_edit_view, this);
        id_et_input = view.findViewById(R.id.id_et_input);
        id_tv_input = view.findViewById(R.id.id_tv_input);
        if (this.getBackground() == null) {
            this.setBackgroundResource(R.drawable.selector_lines_edit_view_bg);
        }
        id_et_input.addTextChangedListener(mTextWatcher);
        id_et_input.setHint(hintText);
        id_et_input.setHintTextColor(hintTextColor);
        id_et_input.setText(contentText);
        id_et_input.setTextColor(contentTextColor);
        id_et_input.setTextSize(TypedValue.COMPLEX_UNIT_SP, contentTextSize);
        id_et_input.setHeight((int) contentViewHeight);
        /*
         * 配合id_tv_input xml的android:focusable="true"和android:focusableInTouchMode="true"
         * 在id_et_input设置完文本后，不给id_et_input 焦点
         */
        id_tv_input.requestFocus();
        configCount();
        //将光标移动最后一个字符后面
        id_et_input.setSelection(id_et_input.length());
        //focus后给背景设置Selected
        id_et_input.setOnFocusChangeListener((v, hasFocus) -> MyLinesEditView.this.setSaveEnabled(hasFocus));
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        private int editStart;
        private int editEnd;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            editStart = id_et_input.getSelectionStart();
            editEnd = id_et_input.getSelectionEnd();
            //先去掉监听器，否则会出现栈溢出
            id_et_input.removeTextChangedListener(mTextWatcher);
            if (ignoreCnOrEn) {
                //当输入字符个数超过限制的大小时，进行截断操作
                while (calculateLengthIgnoreCnOrEn(editable.toString()) > MAX_COUNT) {
                    editable.delete(editStart - 1, editEnd);
                    editStart--;
                    editEnd--;
                }
            } else {
                //因为是中英文混合，单个字符而言，calculateLength函数都会返回1
                //当输入字符个数超过限制的大小时，进行截断操作
                while (calculateLength(editable.toString()) > MAX_COUNT) {
                    editable.delete(editStart - 1, editEnd);
                    editStart--;
                    editEnd--;
                }
            }
            id_et_input.setSelection(editStart);
            //恢复监听器
            id_et_input.addTextChangedListener(mTextWatcher);
            //update
            configCount();
        }
    };

    @SuppressLint("SetTextI18n")
    private void configCount() {
        long newCount;
        if (ignoreCnOrEn) {
            newCount = calculateLengthIgnoreCnOrEn(id_et_input.getText().toString());
        } else {
            newCount = calculateLength(id_et_input.getText().toString());
        }
        if (showPositive) {
            //正数显示 【当前输入数/总数】
            id_tv_input.setText(newCount + "/" + MAX_COUNT);
        } else {
            //倒数显示 【剩余输入数/总数】
            id_tv_input.setText((MAX_COUNT - newCount) + "/" + MAX_COUNT);
        }
    }

    private int calculateLengthIgnoreCnOrEn(CharSequence charSequence) {
        int len = 0;
        for (int i = 0; i < charSequence.length(); i++) {
            len++;
        }
        return len;
    }

    private long calculateLength(CharSequence charSequence) {
        double len = 0;
        for (int i = 0; i < charSequence.length(); i++) {
            int tmp = charSequence.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    public void setInputAble(boolean inputAble) {
        isInputAble = inputAble;
    }

    public void setMaxCount(int max_count) {
        this.MAX_COUNT = max_count;
        configCount();
    }

    public void setIgnoreCnOrEn(boolean ignoreCnOrEn) {
        this.ignoreCnOrEn = ignoreCnOrEn;
        configCount();
    }

    public void setShowPositive(boolean showPositive) {
        this.showPositive = showPositive;
        configCount();
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
        if (id_et_input != null) {
            id_et_input.setHint(hintText);
        }
    }

    public String getHintText() {
        if (id_et_input != null) {
            hintText = id_et_input.getHint() == null ? "" : id_et_input.getHint().toString();
        }
        return hintText;
    }

    public void setHintColor(int color) {
        if (id_et_input != null) {
            id_et_input.setHintTextColor(color);
        }
    }

    public void setContentText(String content) {
        contentText = content;
        if (id_et_input != null) {
            id_et_input.setText(contentText);
        }
    }

    public String getContentText() {
        if (id_et_input != null) {
            contentText = id_et_input.getText() == null ? "" : id_et_input.getText().toString();
        }
        return contentText;
    }

    public void setContentTextSize(int size) {
        if (id_et_input != null) {
            id_et_input.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        }
    }

    public void setContentTextColor(int color) {
        if (id_et_input != null) {
            id_et_input.setTextColor(color);
        }
    }
}























