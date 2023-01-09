package com.android.jidian.client.mvp.ui.activity;

import android.os.Bundle;

import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivityByMvp;
//import com.android.mixiang.client.net.RetrofitClient;
//import com.android.mixiang.client.net.RxScheduler;
//import com.android.mixiang.client.widgets.MyToast;
//import com.uuzuche.lib_zxing.activity.CaptureFragment;
//import com.uuzuche.lib_zxing.activity.CodeUtils;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import io.reactivex.disposables.Disposable;

/**
 * @author : PTT
 * date: 2020/9/23 14:04
 * company: 兴达智联
 * description: 二维码扫描
 */
public class ScanCodeActivity extends U6BaseActivityByMvp {
//    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.tv_scan_qr_hint)
//    TextView tvScanQrHint;
//    @BindView(R.id.edt_scan_qr_code)
//    EditText edtInputCode;
//
//    private int lightType = 0;
//    private String mFromActivityStr = "";
    public static final String IS_FROM = "from";
//    private String mIsInputBox = "0";
    public static final String SCAN_CODE_IS_INPUT_BOX = "SCAN_CODE_IS_INPUT_BOX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_pub_scan_code);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
//        tvTitle.setText("扫一扫");
//
//        mFromActivityStr = getIntent().getStringExtra(IS_FROM);
//        mIsInputBox = getIntent().getStringExtra(SCAN_CODE_IS_INPUT_BOX);
//        if ("MyEbikeBtyFragment".equals(mFromActivityStr)) {
//            tvScanQrHint.setText("请将取景框对准设备号\n扫描后即可绑定车辆");
//        }
//        if ("1".equals(mIsInputBox)) {
//            //首页扫一扫、我的钱包-绑定模块 这两个地方进扫描界面可带输入框，其他则不显示
//            findViewById(R.id.edt_scan_qr_code).setVisibility(View.VISIBLE);
//            findViewById(R.id.btn_scan_qr_sure).setVisibility(View.VISIBLE);
//        } else {
//            findViewById(R.id.edt_scan_qr_code).setVisibility(View.GONE);
//            findViewById(R.id.btn_scan_qr_sure).setVisibility(View.GONE);
//        }
//
//        //扫描二维码返回值
//        CaptureFragment captureFragment = new CaptureFragment();
//        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
//        captureFragment.setAnalyzeCallback(new CodeUtils.AnalyzeCallback() {
//            @Override
//            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
//                sendSure(result);
//            }
//
//            @Override
//            public void onAnalyzeFailed() {
//                Log.d("ScanCodeActivity", "onAnalyzeFailed: ");
//                if (!"MainDiscount".equals(mFromActivityStr)) {
//                    sendJumpResult("");
//                }
//            }
//        });
//        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

//    @OnClick({R.id.iv_back, R.id.btn_scan_qr_light, R.id.btn_scan_qr_sure})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.iv_back:
//                finish();
//                break;
//            case R.id.btn_scan_qr_light:
//                //点击闪光灯
//                if (lightType == 0) {
//                    lightType = 1;
//                    CodeUtils.isLightEnable(true);
//                } else if (lightType == 1) {
//                    lightType = 0;
//                    CodeUtils.isLightEnable(false);
//                }
//                break;
//            case R.id.btn_scan_qr_sure:
//                //点击确定
//                String str = edtInputCode.getText().toString().trim();
//                if (TextUtils.isEmpty(str)) {
//                    MyToast.showTheToast(this, "输入信息不能为空！");
//                } else {
//                    isEdtSure = true;
//                    sendSure(str);
//                }
//                break;
//            default:
//                break;
//        }
//    }

//    /**
//     * 是否是手动输入的扫框
//     */
//    private boolean isEdtSure = false;
//
//    /**
//     * 查询该二维码是否存在
//     */
//    private void couponScan(String result) {
//        String uid = getSharedPreferences("userInfo", Activity.MODE_PRIVATE).getString("id", "");
//        Disposable disposable = RetrofitClient.getInstance().getApiService().sendScan(uid, result)
//                .compose(RxScheduler.Flo_io_main()).subscribe(mBean -> {
//                    if (mBean != null) {
//                        if (mBean.isOk()) {
//                            sendJumpResult(result);
//                        } else {//码存在问题
//                            MyToast.showTheToast(this, mBean.msg);
//                        }
//                    }
//                }, throwable -> {
//                    MyToast.showTheToast(this, "网络异常");
//                });
//    }
//
//    /**
//     * 点击确定和二维码扫描回调
//     */
//    private void sendSure(String result) {
//        if ("MainDiscount".equals(mFromActivityStr)) {
//            couponScan(result);
//        } else {
//            sendJumpResult(result);
//        }
//        /*由于骑呗后台不稳定暂时下架，暂删
//        else if ("MyApplication".equals(mFromActivityStr)) {
//            intent.putExtra(QiBeiConstant.EXTRA_SCAN_ACTIVITY_SCAN_RESULT, result);
//            setResult(RESULT_OK, intent);
//            finish();
//        }*/
//    }
//
//    /**
//     * 点击确定和二维码扫描回调
//     * codedContent 通过扫描返回值
//     * entercode 新增手动输入的扫框
//     */
//    private void sendJumpResult(String result) {
//        Intent intent = getIntent();
//        if ("1".equals(mIsInputBox)) {
//            if (isEdtSure) {
//                intent.putExtra("codedContent", "");
//                intent.putExtra("entercode", result);
//            } else {
//                intent.putExtra("codedContent", result);
//                intent.putExtra("entercode", "");
//            }
//        } else {
//            intent.putExtra("codedContent", result);
//        }
//        setResult(RESULT_OK, intent);
//        finish();
//    }
}