package com.android.jidian.client.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.android.jidian.client.BuildConfig;

import org.w3c.dom.Text;

public class IndexActivity extends U6BaseActivity {

    @BindView(R.id.t_2)
    public TextView t_2;
    @BindView(R.id.t_3)
    public TextView t_3;

    private boolean threadStatus = true;
    private int threadCode = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u6_activity_index);
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

                activity.startActivity(new Intent(activity, MainActivity.class));
//                activity.startActivity(new Intent(activity, TextActivity.class));
                activity.finish();

            }
        }.start();

    }

    @OnClick(R.id.t_2)
    public void onclick_t_2(){
        threadStatus = false;
    }
}
