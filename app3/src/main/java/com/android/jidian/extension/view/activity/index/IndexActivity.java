package com.android.jidian.extension.view.activity.index;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.jidian.extension.BuildConfig;
import com.android.jidian.extension.R;
import com.android.jidian.extension.base.BaseActivity;
import com.android.jidian.extension.view.activity.login.LoginActivity;
import com.android.jidian.extension.view.activity.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class IndexActivity extends BaseActivity {

    @BindView(R.id.t_2)
    public TextView t_2;
    @BindView(R.id.t_3)
    public TextView t_3;

    private boolean threadStatus = true;
    private int threadCode = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_index);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
    }

    private void init(){

        t_2.setText(threadCode+"");
        t_3.setText("Version:" + BuildConfig.VERSION_NAME);

        new Thread(){
            @Override
            public void run() {
                super.run();

                while (threadStatus){
                    try {
                        sleep(1000);
                        threadCode = threadCode - 1;
                        if(threadCode <= 0){
                            threadStatus = false;
                        }else{
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    t_2.setText(threadCode+"");
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                activity.startActivity(new Intent(activity, LoginActivity.class));
//                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.finish();

            }
        }.start();
    }

    @OnClick(R.id.t_2)
    public void onclick_t_2(){
        threadStatus = false;
    }
}
