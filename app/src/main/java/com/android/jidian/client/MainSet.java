package com.android.jidian.client;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.mvp.ui.activity.h5.MainAbout;
import com.android.jidian.client.mvp.ui.activity.h5.MainAgreement;
import com.android.jidian.client.mvp.ui.activity.h5.MainPrivacyClause;
import com.android.jidian.client.mvp.ui.dialog.LogoutDialogFragment;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2017/6/6.
 */
@EActivity(R.layout.main_set)
public class MainSet extends BaseActivity {
    @ViewById
    LinearLayout page_return, about, agreement, cancellation, privacy_clause;
    @ViewById
    TextView logout, now_ver;
    private String message_code_str = "";

    @AfterViews
    void afterview() {
        now_ver.setText("V " + PubFunction.getLocalVersionName(activity));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //系统设置页面访问
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_SYSTEM_SETTINGS);
            }
        }, 500);
    }

    private Dialog dialog;

    @Click({R.id.page_return, R.id.logout, R.id.about, R.id.agreement, R.id.cancellation, R.id.privacy_clause})
    void click(View v) {
        if (v.getId() == R.id.page_return) {
            this.finish();
        } else if (v.getId() == R.id.logout) {
            //点击退出登录
            BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_SYSTEM_SETTINGS_LOG_OUT);
            LogoutDialogFragment logoutDialogFragment = new LogoutDialogFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            logoutDialogFragment.setOnDialogItemClickListener(new LogoutDialogFragment.OnDialogItemClickListener() {
                @Override
                public void onSuccessClick() {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_avatar", "");
                    editor.clear();
                    editor.apply();
                    activity.finish();
//                    startActivity(new Intent(MainSet.this, MainActivity.class));
                    //通知主页刷新数据
//                    Main.handleRefreshMarker.sendEmptyMessage(1);
//                    MyToast.showTheToast(MainSet.this, "正在刷新站点数据，请稍候");
                }

                @Override
                public void onErrorClick() {
                }
            });
            logoutDialogFragment.show(fragmentTransaction, "LOGOUT");
        } else if (v.getId() == R.id.about) {
            //点击关于我们
            BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_SYSTEM_SETTINGS_ABOUT_US);
            startActivity(new Intent(activity, MainAbout.class));
        } else if (v.getId() == R.id.agreement) {
            //点击用户协议
            BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_SYSTEM_SETTINGS_USER_AGREEMENT);
            Intent intent = new Intent(activity, MainAgreement.class);
            activity.startActivity(intent);
        } else if (v.getId() == R.id.cancellation) {
            //点击注销用户
            BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_SYSTEM_SETTINGS_LOG_OFF_USER);
            if (dialog == null) {
                LayoutInflater inflater = LayoutInflater.from(this);
                dialog = new Dialog(this, R.style.Translucent_NoTitle);
                View view = inflater.inflate(R.layout.alertdialog_cancellation, null);
                final EditText input_code = view.findViewById(R.id.input_code);
                final TextView get_code = view.findViewById(R.id.get_code);
                get_code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phone = sharedPreferences.getString("phone", "");
//                        if (TextUtils.isEmpty(phone)) {
//                            MyToast.showTheToast(activity, "手机参数错误！");
//                        } else {
                            if (PubFunction.isConnect(activity, true)) {
                                HttpGetCode(phone, get_code);
//                                setCountDown(get_code);
                            }
//                        }
                    }
                });
                TextView success_t = view.findViewById(R.id.success);
                success_t.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String code_str = input_code.getText().toString().trim();
                        message_code_str = code_str;
                        if (code_str.isEmpty()) {
                            MyToast.showTheToast(MainSet.this,"验证码不能为空");
                            return;
                        }
                        HttpUserCancel(code_str, "0");
                        input_code.setText("");
                        dialog.dismiss();
                        PubFunction.hide_keyboard_from(activity, input_code);
                    }
                });
                TextView error_t = (TextView) view.findViewById(R.id.error);
                error_t.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        PubFunction.hide_keyboard_from(activity, input_code);
                    }
                });
                dialog.setCancelable(false);
                dialog.setContentView(view);
            }
            dialog.show();
        } else if (v.getId() == R.id.privacy_clause) {
            //点击法律声明
            BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_SYSTEM_SETTINGS_LEGAL_STATEMENT);
            startActivity(new Intent(activity, MainPrivacyClause.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void setCountDown(TextView textView) {
        CountDownTimer timer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                textView.setText("倒计时" + millisUntilFinished / 1000 + "秒");
                textView.setClickable(false);
                textView.setBackground(getResources().getDrawable(R.drawable.button_corners_gray_radius_5));
            }

            public void onFinish() {
                textView.setText("获取验证码");
                textView.setClickable(true);
                textView.setBackground(getResources().getDrawable(R.drawable.button_corners_red_radius_5));
            }
        };
        timer.start();
    }

    /**
     * http接口：App/version.html   获取当前版本号
     */
    @Background
    void HttpGetVer() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        new OkHttpConnect(activity, PubFunction.api + "AppVer/version.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetVer(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpGetVer(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
                System.out.println(jsonObject_response);
                if ("1".equals(status)) {
                    JSONObject jsonObject = jsonObject_response.getJSONObject("data");
                    now_ver.setText("V" + jsonObject.getString("Android_name"));
                } else {
                    MyToast.showTheToast(activity, msg);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    /**
     * http接口：Sms/msgSend.html    获取短信验证码
     */
    @Background
    void HttpGetCode(String mobile, TextView textView) {
        List<ParamTypeData> dataList = new ArrayList<>();

        new OkHttpConnect(activity, PubFunction.app + "User/smsCancel.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetCode(response, type, textView);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpGetCode(String response, String type, TextView textView) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equals("1")) {
                    setCountDown(textView);
                }
                System.out.println(jsonObject);
                MyToast.showTheToast(activity, jsonObject.getString("msg"));
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    /**
     * http接口：User/cancel.html    注销用户
     */
    @Background
    void HttpUserCancel(String code_str, String confirm) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("code", code_str));
        if ("0".equals(confirm)) {
        } else if ("1".equals(confirm)) {
            dataList.add(new ParamTypeData("confirm", "1"));
        }
        new OkHttpConnect(activity, PubFunction.app + "User/cancel.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpUserCancel(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpUserCancel(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String messageString = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
                System.out.println(jsonObject_response);
                if ("2".equals(status)) {
                    if (messageString.indexOf("如果确定注销将丢失") != -1 || messageString.indexOf("您确定注销") != -1) {
                        LayoutInflater inflater = LayoutInflater.from(activity);
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        final AlertDialog mAlertDialog = builder.create();
                        View view = inflater.inflate(R.layout.alertdialog_cancellation_2, null);

                        TextView alertDialogTitle = (TextView) view.findViewById(R.id.alertDialogTitle);
                        alertDialogTitle.setText(messageString);

                        TextView success_t = (TextView) view.findViewById(R.id.success);
                        success_t.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (PubFunction.isConnect(activity, true)) {
                                    HttpUserCancel(message_code_str, "1");
                                    //点击确定
                                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_LOG_OFF_USER_SURE);
                                    mAlertDialog.dismiss();

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("user_avatar", "");
                                    editor.clear();
                                    editor.commit();
                                    activity.finish();
                                    //通知主页刷新数据
                                    Main.handleRefreshMarker.sendEmptyMessage(1);
                                    MyToast.showTheToast(MainSet.this, "正在刷新站点数据，请稍候");
                                }
                            }
                        });
                        TextView error_t = (TextView) view.findViewById(R.id.error);
                        error_t.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mAlertDialog.dismiss();
                            }
                        });

                        mAlertDialog.setCancelable(false);
                        mAlertDialog.show();
                        mAlertDialog.getWindow().setContentView(view);
                    } else {
                        MyToast.showTheToast(activity, messageString);
                    }
                } else {
                    MyToast.showTheToast(activity, messageString);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
}