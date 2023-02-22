package com.android.jidian.extension.view.commonPlug.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.jidian.extension.R;

import razerdp.basepopup.BasePopupWindow;

public class DialogByInput extends BasePopupWindow {

    public interface DialogChoiceListener{
        void enterReturn(String str);
        void cancelReturn();
    }

    private Activity activity;
    private String title;
    private DialogByInput dialogChoice;
    private String inputViewStr = "";

    private TextView titleView;
    private TextView enterView;
    private TextView cancelView;
    private EditText inputView;

    private DialogChoiceListener dialogChoiceListener;

    public DialogByInput(Activity activity , String title , DialogChoiceListener dialogChoiceListener) {
        super(activity.getApplicationContext());
        this.activity = activity;
        this.title = title;
        this.dialogChoiceListener = dialogChoiceListener;
        dialogChoice = this;
        setContentView(R.layout.dialog_input);
        init();
    }

    private void init(){
        titleView = this.findViewById(R.id.title);
        titleView.setText(title);
        enterView = this.findViewById(R.id.enterView);
        inputView = this.findViewById(R.id.input);
        enterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChoice.dismiss();
                dialogChoiceListener.enterReturn(inputView.getText().toString());
            }
        });
        cancelView = this.findViewById(R.id.cancelView);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChoice.dismiss();
                dialogChoiceListener.cancelReturn();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
